/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.hpo;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.config.ConfigurationManager;
import com.ikmb.core.data.config.VarWatchConfig.ConfigurationTerms;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.apache.commons.lang.StringUtils;
import org.obolibrary.oboformat.model.Frame;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatParser;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bfredrich
 */
public class HPOUpdateManager {

    @Inject
    ConfigurationManager configManager;
    @Inject
    HPOTermDao hpoDao;
    @Inject
    private Provider<EntityManager> emProvider;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HPOUpdateManager.class);

    public void update() {

    }

    public static void main(String[] args) throws MalformedURLException {
        String url = "jdbc:mysql://127.0.0.1/varwatch";
        String cleanURI = url.substring(5);

        URI uri = URI.create(cleanURI);
        System.out.println(uri);
        System.out.println(uri.getHost());
    }

    @Transactional
    public String getHpoList() {
//        String persistenceUnitName = emProvider.get().getEntityManagerFactory().getProperties().get("hibernate.ejb.persistenceUnitName").toString();
//        String uri = "jdbc:mysql://192.168.100.134/MYHPO_06_2017?user=varwatch_client&password=thEKAwahe8";
//        logger.info("get hpo list with persistance unit {}", persistenceUnitName);
//        if (persistenceUnitName.equals("varwatch_dev")) {
//            uri = "jdbc:mysql://127.0.0.1/MYHPO_06_2017?user=demo&password=omed";
//        } else if (persistenceUnitName.equals("varwatch_docker")) {
//            uri = "jdbc:mysql://hpodb/MYHPO_06_2017?user=demo&password=omed";
//        }
//        Map<Long, HpoPathTerm> hpoTerms = hpoDao.getHpoTermsFromDb(uri);
//        return getHpoListFromHpoTerms("MYHPO_06_2017", hpoTerms);
        String hpoPathString = configManager.getConfiguration(ConfigurationTerms.HPO_BASIC_FILE.getTerm());
        try {
            Path hpoPath = Paths.get(hpoPathString);
            boolean fileExist = Files.exists(hpoPath);
            if (!fileExist) {
                updateHpoBrowserFile();
            }
            return Files.readAllLines(hpoPath).stream().collect(Collectors.joining("\n"));
        } catch (IOException ex) {
            Logger.getLogger(HPOUpdateManager.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
    }

    private static String getHpoListFromHpoTerms(String version, Map<Long, HpoPathTerm> hpoTerms) {
        StringBuilder sb = new StringBuilder();
        sb.append("#version:" + version);
        sb.append("\n");
        for (HpoPathTerm currentTerm : hpoTerms.values()) {
            List<String> allhpoTerms = new ArrayList<>(Arrays.asList(currentTerm.getHpoTerm()));
            try {
                allhpoTerms.addAll(currentTerm.getAlternativeTerms());
            } catch (Exception ex) {
                System.out.println("here");
                ex.printStackTrace();
            }
            String allTerms = StringUtils.join(allhpoTerms, ",");
            String line = allTerms + "|" + currentTerm.getDescription() + "|";
            List<String> parentTerms = new ArrayList<>();
            for (Long currentId : currentTerm.getParentHPOTerms()) {
                parentTerms.add(hpoTerms.get(currentId).getHpoTerm());
            }
            line += StringUtils.join(parentTerms, ",");
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }

    @Transactional
    public void updateHpoBrowserFile() {
        String url = configManager.getConfiguration(ConfigurationTerms.HPO_OBO_SOURCE_URL.getTerm());
        OBOFormatParser parser = new OBOFormatParser();
        try {
            OBODoc parse = parser.parseURL(url);

            String version = getVersionFromObo(parse);

            List<Frame> termFrames = getAllFramesSorted(parse);

            List<String> hpoLines = parseOboHpo(termFrames);

            String hpoPath = configManager.getConfiguration(ConfigurationTerms.HPO_BASIC_FILE.getTerm());
            writeToFile(version, hpoLines, hpoPath);

        } catch (IOException ex) {
            Logger.getLogger(HPOUpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Transactional
    public void updateHpoDistanceFile() {
        String oboFilePath = configManager.getConfiguration(ConfigurationTerms.HPO_OBO_SOURCE_URL.getTerm());
        String calcFilePath = configManager.getConfiguration(ConfigurationTerms.HPO_DIST_FILE.getTerm());
        HpoDistanceFileCalculator hpoDistanceFileCalculator = new HpoDistanceFileCalculator();
        try {
            hpoDistanceFileCalculator.run(oboFilePath, calcFilePath);
        } catch (IOException ex) {
            Logger.getLogger(HPOUpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getVersionFromObo(OBODoc parse) {
        Frame headerFrame = parse.getHeaderFrame();
        String version = headerFrame.getTagValue("data-version", String.class);
        return version;
    }

    private List<Frame> getAllFramesSorted(OBODoc parse) {
        List<Frame> termFrames = new ArrayList<>(parse.getTermFrames());
        Collections.sort(termFrames, (Frame o1, Frame o2) -> {
            Integer id1 = Integer.parseInt(o1.getId().split(":")[1]);
            Integer id2 = Integer.parseInt(o2.getId().split(":")[1]);
            return id1 - id2;
        });
        return termFrames;
    }

    private List<String> parseOboHpo(List<Frame> termFrames) {
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

    private void writeToFile(String version, List<String> hpoLines, String hpoPath) {
        Path path = Paths.get(hpoPath);

        try {
            Files.write(path, version.getBytes());
            Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
            Files.write(path, hpoLines, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(HPOUpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
