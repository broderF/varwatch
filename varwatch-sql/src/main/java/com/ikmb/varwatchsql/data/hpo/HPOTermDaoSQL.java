/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.hpo;

import com.ikmb.core.data.hpo.HPOTermDao;
import com.ikmb.core.data.hpo.HpoPathTerm;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bfredrich
 */
public class HPOTermDaoSQL implements HPOTermDao {
    
    public Map<Long, HpoPathTerm> getHpoTermsFromDb(String connectionPath) {
        Connection _conn = null;
        Map<Long, HpoPathTerm> hpoTerms = new TreeMap<Long, HpoPathTerm>();
        int nr = 0;
        try {
            _conn = DriverManager.getConnection(connectionPath);

            Statement stmt = _conn.createStatement();
            //am besten mit join und dann die HPOTerm Map füllen
            String select = "SELECT * FROM term t left join term2term t2t on t.id=t2t.term2_id WHERE t.is_obsolete=0";
//            String select = "SELECT term.id FROM term where subontology=\"O\"";
//            System.out.println(select);
            ResultSet rs = stmt.executeQuery(select);
//| id | name                                     | is_obsolete | is_root | subontology | comment | acc        |

            while (rs.next()) {
                Long id = Long.parseLong(rs.getString("t.id"));
                nr++;
//                ids.add(id);

                if (hpoTerms.containsKey(id)) {
                    HpoPathTerm pathTerm = hpoTerms.get(id);
                    String parent = rs.getString("t2t.term1_id");
                    if (parent != null) {
                        Long parentId = Long.parseLong(parent);
                        pathTerm.addParent(parentId);
                    }
                } else {
                    String accesionNumber = rs.getString("t.acc");
                    String name = rs.getString("t.name");
                    HpoPathTerm pathTerm = new HpoPathTerm(id, accesionNumber, name);
                    String parent = rs.getString("t2t.term1_id");
                    if (parent != null) {
                        Long parentId = Long.parseLong(parent);
                        pathTerm.addParent(parentId);
                    }
                    hpoTerms.put(id, pathTerm);
                }

                HpoPathTerm pathTerm = hpoTerms.get(id);
                Set<String> alternativeTerms = getAlternativeTerms(_conn, id);
                pathTerm.addAlternativeHpos(alternativeTerms);
            }

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        System.out.println(nr);
        return hpoTerms;
    }

    private Set<String> getAlternativeTerms(Connection conn, Long id) {
        Set<String> alternativeHpos = new HashSet<>();
        try {
            String select = "SELECT * FROM term_alternative_id WHERE term_id=" + id;

            Statement stmt = conn.createStatement();
//            System.out.println(select);
            ResultSet rs = stmt.executeQuery(select);

            while (rs.next()) {
                Long altid = Long.parseLong(rs.getString("alternative_id"));
                String formatted = String.format("%07d", altid);
                alternativeHpos.add("HP:" + formatted);
            }
        } catch (SQLException ex) {
            Logger.getLogger(HPOTermDaoSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alternativeHpos;
    }
}
