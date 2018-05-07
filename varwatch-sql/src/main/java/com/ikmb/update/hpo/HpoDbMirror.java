/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.update.hpo;

import com.ikmb.varwatchsql.data.hpo.HPOAlternativeTermSQL;
import com.ikmb.varwatchsql.data.hpo.HPOTermSQL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author bfredrich
 */
public class HpoDbMirror {

    public static void main(String[] args) {
        HpoDbMirror mirror = new HpoDbMirror();
        String database = "";
        String dbUser = "";
        String dbPw = "";
        mirror.run(database, dbUser, dbPw);
    }
    long altIds = 1;

    private void run(String hpoDb, String dbUser, String dbPw) {
        List<HPOTermSQL> hpoTerms = getHpoTermsFromDb(hpoDb, dbUser, dbPw);
        Long newHpoId = addNewHpoRefDatabaseEntry(hpoDb, dbUser, dbPw);
        importHpoTerms(hpoTerms, newHpoId, dbUser, dbPw);
        updateVarWatchHpo(newHpoId, dbUser, dbPw);
        System.out.println("finish importing");

    }

    private void importHpoTerms(List<HPOTermSQL> hpoTerms, Long hpoVersion, String dbUser, String dbPw) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/varwatch?"
                    + "user=" + dbUser + "&password=" + dbPw);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            int nrInserts = 0;
            for (HPOTermSQL currentHpo : hpoTerms) {
                nrInserts++;
                Long currentId = currentHpo.getId();
                String hpoIdentifier = currentHpo.getIdentifier();
                String description = currentHpo.getDescription();
                String subontology = currentHpo.getSubontology();
                Boolean is_root = currentHpo.isRoot();
                Boolean is_obsolete = currentHpo.isObsolete();
                String insertdist = "INSERT INTO hpo (id,identifier,description,subontology,is_root,is_obsolete,hpo_version) "
                        + "VALUES(" + currentId + ",\"" + hpoIdentifier + "\",\"" + description.replaceAll("\"", "\\\'") + "\",\"" + subontology + "\"," + is_root + "," + is_obsolete + "," + hpoVersion + ") ON DUPLICATE KEY UPDATE id=VALUES(id),identifier=VALUES(identifier),description=VALUES(description),subontology=VALUES(subontology),is_root=VALUES(is_root),is_obsolete=VALUES(is_obsolete),hpo_version=VALUES(hpoVersion);";
//                System.out.println(insertdist);
                statement.addBatch(insertdist);
                for (HPOAlternativeTermSQL altHpo : currentHpo.getAlternativeHpos()) {
                    Long id = altHpo.getId();
                    String alternativeIdentifier = altHpo.getIdentifier();
                    String dsInsert = "INSERT INTO hpo_alternative (id,term_id,alternative_identifier) VALUES(" + id + "," + currentId + ",\"" + alternativeIdentifier + "\") ON DUPLICATE KEY UPDATE id=VALUES(id),term_id=VALUES(term_id),alternative_identifier=VALUES(alternative_identifier);";
//                    System.out.println(dsInsert);
                    statement.addBatch(dsInsert);
                }

                if (nrInserts % 1000 == 0) {
                    int[] rows = statement.executeBatch();
                    System.out.println(nrInserts + " rows inserted");
                    connection.commit();
                }
            }
            int[] rows = statement.executeBatch();
            System.out.println(nrInserts + " rows inserted");
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(HpoDbMirror.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<HPOTermSQL> getHpoTermsFromDb(String database, String user, String password) {
        Connection _conn = null;
        Map<Long, HPOTermSQL> hpoTerms = new TreeMap<Long, HPOTermSQL>();
        int nr = 0;
        try {
            _conn = DriverManager.getConnection("jdbc:mysql://localhost/" + database + "?user=" + user + "&password=" + password);

            Statement stmt = _conn.createStatement();
            //am besten mit join und dann die HPOTerm Map füllen
            String select = "SELECT * FROM term left join term_alternative_id alt on term.id=alt.term_id";
//            String select = "SELECT term.id FROM term where subontology=\"O\"";
            System.out.println(select);
            ResultSet rs = stmt.executeQuery(select);
//| id | name                                     | is_obsolete | is_root | subontology | comment | acc        |

            while (rs.next()) {
                Long id = Long.parseLong(rs.getString("term.id"));
                nr++;
//                ids.add(id);
                String altIdString = rs.getString("alt.alternative_id");
                if (hpoTerms.containsKey(id)) {
                    HPOTermSQL get = hpoTerms.get(id);
                    addAlternativeId(get, altIdString);
                } else {
                    String accesionNumber = rs.getString("term.acc");
                    String name = rs.getString("term.name");
                    Boolean isRoot = rs.getBoolean("term.is_root");
                    Boolean isObsolete = rs.getBoolean("term.is_obsolete");
                    String subontology = rs.getString("term.subontology");
                    HPOTermSQL termSql = new HPOTermSQL();
                    termSql.setDescription(name);
                    termSql.setId(id);
                    termSql.setIdentifier(accesionNumber);
                    termSql.setIsObsolete(isObsolete);
                    termSql.setIsRoot(isRoot);
                    termSql.setSubontology(subontology);
                    termSql.setAlternativeHpos(new HashSet<HPOAlternativeTermSQL>());
                    addAlternativeId(termSql, altIdString);
                    hpoTerms.put(id, termSql);
                }

            }

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        System.out.println(nr);
        return new ArrayList<>(hpoTerms.values());
    }

    private void addAlternativeId(HPOTermSQL hpoTerm, String altTermString) {
        if (altTermString != null) {
            Long altTermId = Long.parseLong(altTermString);
            HPOAlternativeTermSQL altTerm = new HPOAlternativeTermSQL();
            Formatter formatter = new Formatter();
            String altIDString = formatter.format("%07d%n", altTermId).toString();
            altTerm.setIdentifier("HP:" + altIDString.trim());
            altTerm.setId(altIds);
            hpoTerm.getAlternativeHpos().add(altTerm);
            altIds++;
        }
    }

    private Long addNewHpoRefDatabaseEntry(String hpoDb, String dbUser, String dbPw) {
        Connection _conn = null;
        Long id = null;
        try {
            _conn = DriverManager.getConnection("jdbc:mysql://localhost/varwatch?user=" + dbUser + "&password=" + dbPw);
            Statement stmt = _conn.createStatement();
            String timestamp = new DateTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
            String insert = "Insert into ref_database (name,path,is_active,updated,timestamp) VALUES(\"HPO\",\"" + hpoDb + "\",0,0,\"" + timestamp + "\")";
            stmt.execute(insert);
            ResultSet rs = stmt.executeQuery("select last_insert_id() as last_id");
            rs.next();
            id = Long.parseLong(rs.getString("last_id"));
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return id;

    }

    private void updateVarWatchHpo(Long newHpoId, String dbUser, String dbPw) {
        Connection _conn = null;
        int nr = 0;
        try {
            _conn = DriverManager.getConnection("jdbc:mysql://localhost/varwatch?user=" + dbUser + "&password=" + dbPw);

            Statement stmt = _conn.createStatement();
            String select = "SELECT * from hpo where hpo_version!=" + newHpoId;
            System.out.println(select);
            ResultSet rs = stmt.executeQuery(select);

            while (rs.next()) {
                Long hpoId = rs.getLong("id");
                Formatter formatter = new Formatter();
                String altIDString = "HP:" + formatter.format("%07d", hpoId).toString();
                String altSelect = "Select * from hpo h join hpo_alternative a on h.id=a.term_id where a.alternative_identifier=\"" + altIDString + "\";";
                Statement stmtAlt = _conn.createStatement();
                System.out.println(altSelect);
                ResultSet altRs = stmtAlt.executeQuery(altSelect);
                while (altRs.next()) {
                    Long hpoPrimId = Long.parseLong(altRs.getString("h.id"));
                    String updateHpoVw = "Update dataset2hpo set phenotype_id=" + hpoPrimId + " where phenotype_id=" + hpoId + ";";
                    Statement stmtUpdate = _conn.createStatement();
                    System.out.println(updateHpoVw);
                    stmtUpdate.executeUpdate(updateHpoVw);
                    String delete = "Delete from hpo where id=" + hpoId;
                    System.out.println(delete);
                    Statement stmtDelete = _conn.createStatement();
                    stmtDelete.executeUpdate(delete);
                }
            }

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}
