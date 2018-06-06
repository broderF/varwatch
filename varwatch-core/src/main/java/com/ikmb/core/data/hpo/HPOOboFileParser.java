/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.hpo;

import com.google.inject.Inject;
import com.ikmb.core.data.config.ConfigurationManager;
import com.ikmb.core.data.config.VarWatchConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.obolibrary.oboformat.model.Frame;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatParser;

/**
 *
 * @author broder
 */
public class HPOOboFileParser {

    private ArrayList<Frame> frames;
    @Inject
    private ConfigurationManager configManager;

    public String getVersionFromObo(OBODoc parse) {
        Frame headerFrame = parse.getHeaderFrame();
        String version = headerFrame.getTagValue("data-version", String.class);
        return version;
    }

    public List<Frame> getAllFramesSorted(OBODoc parse) {
        List<Frame> termFrames = new ArrayList<>(parse.getTermFrames());
        Collections.sort(termFrames, (Frame o1, Frame o2) -> {
            Integer id1 = Integer.parseInt(o1.getId().split(":")[1]);
            Integer id2 = Integer.parseInt(o2.getId().split(":")[1]);
            return id1 - id2;
        });
        return termFrames;
    }

    public List<String> parseOboHpo(List<Frame> termFrames) {
        //id,alternativeid | description | parents
        List<String> hpoLines = new ArrayList<>();
        for (Frame curFrame : termFrames) {
            String id = curFrame.getId();
            String name = curFrame.getTagValue("name", String.class);
            Collection<String> altTerms = curFrame.getTagValues("alt_id", String.class);
            Collection<String> parentTerms = curFrame.getTagValues("is_a", String.class);
            StringJoiner idsJoiner = new StringJoiner(",");
            idsJoiner.add(id);
            for (String curAlt : altTerms) {
                idsJoiner.add(curAlt);
            }
            String ids = idsJoiner.toString();

            idsJoiner = new StringJoiner(",");
            for (String curParent : parentTerms) {
                idsJoiner.add(curParent);
            }
            String parents = idsJoiner.toString();
            hpoLines.add(ids + "|" + name + "|" + parents);
        }
        return hpoLines;
    }

    public Map<String, Frame> getIdToFrameMapping() {
        //id,alternativeid | description | parents
        Map<String, Frame> idToFrame = new HashMap<>();
        for (Frame curFrame : frames) {
            String id = curFrame.getId();
            idToFrame.put(id, curFrame);
        }
        return idToFrame;
    }

    public void writeToFile(String version, List<String> hpoLines, String hpoPath) {
        Path path = Paths.get(hpoPath);

        try {
            Files.write(path, version.getBytes());
            Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
            Files.write(path, hpoLines, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(HPOUpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Map<String, String> getAltIdToIdMapping() {
        Map<String, String> altIdToId = new HashMap<>();
        for (Frame curFrame : frames) {
            String id = curFrame.getId();
//            String name = curFrame.getTagValue("name", String.class);
            Collection<String> altTerms = curFrame.getTagValues("alt_id", String.class);
            for (String curAltTerm : altTerms) {
                if (altIdToId.containsKey(curAltTerm)) {
                    System.out.println("multiple mappings for alt id :" + curAltTerm);
                }
                altIdToId.put(curAltTerm, id);
            }
        }
        return altIdToId;
    }

    public void parseHpoFromConfig() {
        String url = configManager.getConfiguration(VarWatchConfig.ConfigurationTerms.HPO_OBO_SOURCE_URL.getTerm());
        OBOFormatParser parser = new OBOFormatParser();

        try {
            OBODoc parse = parser.parseURL(url);
            frames = new ArrayList<>(parse.getTermFrames());

        } catch (IOException ex) {
            Logger.getLogger(HPOUpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Set<String> gerPrimaryIds() {
        //id,alternativeid | description | parents
        Set<String> ids = new HashSet<>();
        for (Frame curFrame : frames) {
            String id = curFrame.getId();
            ids.add(id);
        }
        return ids;
    }
}
