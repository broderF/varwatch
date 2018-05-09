/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.hpo;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bfredrich
 */
public class HPOUpdateManager {

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

    public String getHpoList() {
        String persistenceUnitName = emProvider.get().getEntityManagerFactory().getProperties().get("hibernate.ejb.persistenceUnitName").toString();
        String uri = "jdbc:mysql://192.168.100.134/MYHPO_06_2017?user=varwatch_client&password=thEKAwahe8";
        logger.info("get hpo list with persistance unit {}",persistenceUnitName);
        if (persistenceUnitName.equals("varwatch_dev")) {
            uri = "jdbc:mysql://127.0.0.1/MYHPO_06_2017?user=demo&password=omed";
        } else if (persistenceUnitName.equals("varwatch_docker")){
            uri = "jdbc:mysql://hpodb/MYHPO_06_2017?user=demo&password=omed";
        }
        Map<Long, HpoPathTerm> hpoTerms = hpoDao.getHpoTermsFromDb(uri);
        return getHpoListFromHpoTerms("MYHPO_06_2017", hpoTerms);
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

}
