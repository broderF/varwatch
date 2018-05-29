/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching;

import com.google.inject.Inject;
import com.ikmb.core.data.config.ConfigurationManager;
import com.ikmb.core.data.config.VarWatchConfig.ConfigurationTerms;
import com.ikmb.core.data.hpo.HPOUpdateManager;
import com.ikmb.core.data.hpo.HpoDistanceFileCalculator;
import com.ikmb.matching.varwatch.VarWatchScreener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author broder
 */
public class HPODistanceLoader {

    @Inject
    private ConfigurationManager configManager;
    private Map<String, HPOPath> pathList;

    public Map<String, HPOPath> loadHPODistances() {
        String hpoFile = configManager.getConfiguration(ConfigurationTerms.HPO_DIST_FILE.getTerm());
        pathList = new HashMap<>();

        Path hpoFilePath = Paths.get(hpoFile);
        boolean fileExist = Files.exists(hpoFilePath);
        if (!fileExist) {
            String oboUrlPath = configManager.getConfiguration(ConfigurationTerms.HPO_OBO_SOURCE_URL.getTerm());
            String calcFilePath = configManager.getConfiguration(ConfigurationTerms.HPO_DIST_FILE.getTerm());
            HpoDistanceFileCalculator hpoDistanceFileCalculator = new HpoDistanceFileCalculator();
            try {
                hpoDistanceFileCalculator.run(oboUrlPath, calcFilePath);
            } catch (IOException ex) {
                Logger.getLogger(HPOUpdateManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(hpoFile))) {
            String line = br.readLine();

            while (line != null) {
                String[] hpoTerms = line.split(",");
                String firstHPO = hpoTerms[0];
                Set<String> currentPath = new HashSet<>();
                for (int i = 0; i < hpoTerms.length; i++) {
                    currentPath.add(hpoTerms[i]);
                }
                HPOPath hpoPath = null;
                if (pathList.containsKey(firstHPO)) {
                    hpoPath = pathList.get(firstHPO);
                } else {
                    hpoPath = new HPOPath(firstHPO);
                }
                hpoPath.addPath(currentPath);
                pathList.put(firstHPO, hpoPath);
                line = br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(VarWatchScreener.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pathList;
    }
}
