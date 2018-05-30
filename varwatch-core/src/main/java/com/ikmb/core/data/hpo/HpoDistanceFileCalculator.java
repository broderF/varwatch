/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.hpo;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.obolibrary.oboformat.model.Frame;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatParser;

/**
 *
 * @author bfredrich
 */
public class HpoDistanceFileCalculator {

//    public static void main(String[] args) throws IOException {
//        HpoPathCalculator calculator = new HpoPathCalculator();
//        calculator.run();
//    }
    long altIds = 1;

    public void run(String oboPath, String outputPath) throws IOException {
        Map<Long, HpoPathTerm> hpoTerms = getHpoTermsFromFile(new URL(oboPath));
//        printTxt(hpoTerms);
        List<String> lines = new ArrayList<>();
        for (Map.Entry<Long, HpoPathTerm> mapEntry : hpoTerms.entrySet()) {
            lines.addAll(calculatePaths(mapEntry, hpoTerms));
        }
        Path file = Paths.get(outputPath);
        Files.write(file, lines, Charset.forName("UTF-8"));
    }

    public Map<Long, HpoPathTerm> getHpoTermsFromFile(URL oboUrl) throws IOException {
        Map<Long, HpoPathTerm> hpoTerms = new TreeMap<>();
        OBOFormatParser parser = new OBOFormatParser();
        OBODoc parse = parser.parse(oboUrl);

        List<Frame> termFrames = new ArrayList<>(parse.getTermFrames());
        Collections.sort(termFrames, (Frame o1, Frame o2) -> {
            Integer id1 = Integer.parseInt(o1.getId().split(":")[1]);
            Integer id2 = Integer.parseInt(o2.getId().split(":")[1]);
            return id1 - id2;
        });

        for (Frame curFrame : termFrames) {
            Long id = Long.parseLong(curFrame.getId().split(":")[1]);
            String description = curFrame.getTagValue("name", String.class);
            HpoPathTerm hpoPathTerm = new HpoPathTerm(id, curFrame.getId(), description);
            Collection<String> altTerms = curFrame.getTagValues("alt_id", String.class);
            Collection<String> parentTerms = curFrame.getTagValues("is_a", String.class);
            for (String s : parentTerms) {
                Long parentId = Long.parseLong(s.split(":")[1]);
                hpoPathTerm.addParent(parentId);
            }

            hpoTerms.put(id, hpoPathTerm);
        }

        return hpoTerms;
    }

//    public Map<Long, HpoPathTerm> getHpoTermsFromDb(String database, String user, String password) {
//        Connection _conn = null;
//        Map<Long, HpoPathTerm> hpoTerms = new TreeMap<Long, HpoPathTerm>();
//        int nr = 0;
//        try {
//            _conn = DriverManager.getConnection("jdbc:mysql://localhost/MYHPO_06_2017?user=root&password=yuBr3Pef");
//
//            Statement stmt = _conn.createStatement();
//            //am besten mit join und dann die HPOTerm Map füllen
//            String select = "SELECT * FROM term t left join term2term t2t on t.id=t2t.term2_id";
////            String select = "SELECT term.id FROM term where subontology=\"O\"";
//            System.out.println(select);
//            ResultSet rs = stmt.executeQuery(select);
////| id | name                                     | is_obsolete | is_root | subontology | comment | acc        |
//
//            while (rs.next()) {
//                Long id = Long.parseLong(rs.getString("t.id"));
//                nr++;
////                ids.add(id);
//
//                if (hpoTerms.containsKey(id)) {
//                    HpoPathTerm pathTerm = hpoTerms.get(id);
//                    String parent = rs.getString("t2t.term1_id");
//                    if (parent != null) {
//                        Long parentId = Long.parseLong(parent);
//                        pathTerm.addParent(parentId);
//                    }
//                } else {
//                    String accesionNumber = rs.getString("t.acc");
//                    String name = rs.getString("t.name");
//                    HpoPathTerm pathTerm = new HpoPathTerm(id, accesionNumber, name);
//                    String parent = rs.getString("t2t.term1_id");
//                    if (parent != null) {
//                        Long parentId = Long.parseLong(parent);
//                        pathTerm.addParent(parentId);
//                    }
//                    hpoTerms.put(id, pathTerm);
//                }
//
//            }
//
//        } catch (SQLException ex) {
//            // handle any errors
//            System.out.println("SQLException: " + ex.getMessage());
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());
//        } finally {
//            _conn.close();
//        }
//        System.out.println(nr);
//        return hpoTerms;
//    }

    private List<String> calculatePaths(Map.Entry<Long, HpoPathTerm> mapEntry, Map<Long, HpoPathTerm> hpoTerms) {
        List<String> lines = new ArrayList<>();
        Long currentHpo = mapEntry.getKey();
//        List<Set<Long>> hpoAncPaths = new ArrayList<>();
        List<Long> parents = mapEntry.getValue().getParentHPOTerms();
        for (Long currentParent : parents) {
            List<Set<Long>> paths = calculateAllPaths(currentParent, new HashSet<Long>(), new ArrayList(), hpoTerms);
            for (Set<Long> currentPath : paths) {
                String hpoTermsToString = mapEntry.getValue().getHpoTerm();
                for (Long currentTerm : currentPath) {
                    String currentTermString = hpoTerms.get(currentTerm).getHpoTerm();
                    hpoTermsToString = hpoTermsToString + "," + currentTermString;
                }
                lines.add(hpoTermsToString);
            }
        }
        return lines;
    }

    private List<Set<Long>> calculateAllPaths(Long currentParent, Set<Long> hpoAncPaths, List<Set<Long>> allPath, Map<Long, HpoPathTerm> hpoTerms) {
        HpoPathTerm currentTerm = hpoTerms.get(currentParent);
        Set<Long> newPath = new HashSet<>(hpoAncPaths);
        newPath.add(currentTerm.getId());
        if (currentTerm.getParentHPOTerms().isEmpty()) {
            allPath.add(newPath);
        }

        for (Long parentTerm : currentTerm.getParentHPOTerms()) {
            calculateAllPaths(parentTerm, newPath, allPath, hpoTerms);
        }
        return allPath;
    }

    private void printJson(Map<Long, HpoPathTerm> hpoTerms) throws IOException {
        JSONArray hposJson = new JSONArray();
        for (HpoPathTerm currentTerm : hpoTerms.values()) {
            JSONObject object = new JSONObject();
            try {
                object.put("acc", currentTerm.getHpoTerm());
                object.put("description", currentTerm.getDescription());
                JSONArray parentTerms = new JSONArray();
                List<String> hpoTermsString = new ArrayList<>();
                for (Long id : currentTerm.getParentHPOTerms()) {
                    hpoTermsString.add(hpoTerms.get(id).getHpoTerm());
                }
                parentTerms.put(hpoTermsString);
                object.put("parents", parentTerms);
                hposJson.put(object);
            } catch (JSONException ex) {
                Logger.getLogger(HpoDistanceFileCalculator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonParser jp = new JsonParser();
//        JsonElement je = jp.parse(hposJson.toString());
//        String prettyJsonString = gson.toJson(je);
        Path path = Paths.get("/home/bfredrich/hpo_list.json");
        Files.write(path, hposJson.toString().getBytes());

    }

    private void printTxt(Map<Long, HpoPathTerm> hpoTerms) throws IOException {
        List<String> lines = new ArrayList<>();
        for (HpoPathTerm currentTerm : hpoTerms.values()) {
            String line = currentTerm.getHpoTerm() + "|" + currentTerm.getDescription() + "|";
            List<String> parentTerms = new ArrayList<>();
            for (Long currentId : currentTerm.getParentHPOTerms()) {
                parentTerms.add(hpoTerms.get(currentId).getHpoTerm());
            }
            line += StringUtils.join(parentTerms, ",");
            lines.add(line);
        }
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonParser jp = new JsonParser();
//        JsonElement je = jp.parse(hposJson.toString());
//        String prettyJsonString = gson.toJson(je);
        Path path = Paths.get("/home/bfredrich/hpo_list_2017.txt");
        Files.write(path, lines, StandardCharsets.UTF_8);

    }
}
