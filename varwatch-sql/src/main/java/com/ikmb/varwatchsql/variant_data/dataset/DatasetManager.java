/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.variant_data.dataset;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.ikmb.varwatchcommons.entities.Feature;
import com.ikmb.varwatchsql.auth.client.AuthClientSQL;
import com.ikmb.varwatchsql.auth.client.ClientDao;
import com.ikmb.varwatchsql.auth.user.UserDao;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.data.hpo.HPOTermSQL;
import com.ikmb.varwatchcommons.entities.VWMatchRequest;
import com.ikmb.varwatchcommons.entities.Dataset;
import com.ikmb.varwatchsql.guice.VarWatchInjector;
import com.ikmb.varwatchsql.guice.VarWatchPersist;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.entities.DatasetHGMDSQL;
import com.ikmb.varwatchsql.matching.MatchSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.data.hpo.HPOTermBuilder;
import com.ikmb.varwatchsql.data.hpo.PhenotypeDao;
import com.ikmb.varwatchsql.data.hpo.PhenotypeSQL;
import com.ikmb.varwatchsql.matching.MatchVariantDataManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class DatasetManager {

    private final Logger logger = LoggerFactory.getLogger(DatasetManager.class);
    @Inject
    private DatasetDao datasetDao;

    @Inject
    private UserDao userDao;

    @Inject
    private PhenotypeDao hpoDao;

    @Inject
    private ClientDao clientDao;

    @Inject
    private HPOTermBuilder hpoBuilder;

    @Inject
    private DatasetBuilder datasetBuilder;

    @Inject
    private MatchVariantDataManager matchVariantDM;

    @Inject
    private EntityManager em;

    private String errorMessage;

    @Transactional
    public Long persistRawData(VWMatchRequest submitrequest, UserSQL user, AuthClientSQL client, DatasetBuilder.RawDataType rawDataType) {
        AuthClientSQL clientSql = clientDao.getClientByID(client.getId());

        UserSQL userSQL = userDao.getUserByID(user.getId());

//        List<String> hpoTerms = hpoBuilder.addFeatures(submitrequest.getPatient().getFeatures()).buildStringList();
//        Set<HPOTermSQL> hpoTermsSQL = new HashSet<HPOTermSQL>(hpoDao.getHPOTermsByNames(hpoTerms,"O"));
//        if (hpoTermsSQL.isEmpty()) {
//            errorMessage = "No valid HPO term";
//            return null;
//        }
        String assembly = submitrequest.getPatient().getAssembly();
        if (assembly == null || (!(assembly.equals("GRCh38") || assembly.equals("GRCh37")))) {
            logger.error("invalid assembly: {}", assembly);
            errorMessage = "Invalid assembly: " + assembly;
            return null;
        }

        Set<PhenotypeSQL> phenotypes = new HashSet<PhenotypeSQL>();
        Set<String> incorrectPhenotypes = new HashSet<String>();
        for (Feature vwFeature : submitrequest.getPatient().getFeatures()) {
            String ageOfOnset = vwFeature.getAgeOfOnset();
            HPOTermSQL ageOfOnsetSql = hpoDao.getHPOTermByName(ageOfOnset, "C");
            Boolean observed = false;
            if (vwFeature.getObserved() != null && vwFeature.getObserved().equalsIgnoreCase("true")) {
                observed = true;
            }
            HPOTermSQL phenotypeSql = hpoDao.getHPOTermByName(vwFeature.getId(), "O");
            logger.info("current hpo term {}", vwFeature.getId());
            if (phenotypeSql == null) {
                logger.error("invalid hpo term {}", vwFeature.getId());
                incorrectPhenotypes.add(vwFeature.getId());
                continue;
            }
            PhenotypeSQL phenotype = new PhenotypeSQL();
            phenotype.setAgeOfOnset(ageOfOnsetSql);
            phenotype.setObserved(observed);
            phenotype.setPhenotype(phenotypeSql);
            phenotypes.add(phenotype);
        }
        if (!incorrectPhenotypes.isEmpty()) {
            errorMessage = "Invalid hpo term(";
            for (String s : incorrectPhenotypes) {
                errorMessage += s;
            }
            errorMessage += ")";
            return null;
        } else if (phenotypes.isEmpty()) {
            errorMessage = "No valid HPO term";
            return null;
        }

        HPOTermSQL inheritanceMode = hpoDao.getHPOTermByName(submitrequest.getPatient().getInheritanceMode(), "I");
        HPOTermSQL ageOfOnset = hpoDao.getHPOTermByName(submitrequest.getPatient().getAgeOfOnset(), "C");

        DatasetVWSQL datasetSQL = datasetBuilder.withVWMatchRequest(submitrequest, rawDataType).withClient(clientSql).withUser(userSQL).withPhenotypes(phenotypes).withAgeOfOnset(ageOfOnset).withModeOfInheritance(inheritanceMode).buildRawVWSql();
        datasetDao.save(datasetSQL);
        return datasetSQL.getId();
    }

    @Transactional
    public DatasetVWSQL getDatasetWithVariantsByID(Long datasetID) {
        DatasetVWSQL ds = datasetDao.getDataset(datasetID);
        ds.getVariants();
        return ds;
    }

    @Transactional
    public DatasetVWSQL getDatasetByID(Long datasetID) {
        DatasetVWSQL ds = datasetDao.getDataset(datasetID);
        return ds;
    }

    @Transactional
    public void updateDataset(DatasetVWSQL dataset) {
        datasetDao.update(dataset);
    }

    @Transactional
    public void refresh(DatasetVWSQL dataset) {
        datasetDao.refresh(dataset);
    }

    @Transactional
    public List<Dataset> getSimpleDatasetByUserId(Integer userId) {
        UserSQL user = userDao.getUserByID(userId);
        List<DatasetVWSQL> datasetsSql = datasetDao.getDatasetsByUser(user);
        List<Dataset> datasets = datasetBuilder.withDatasetsSQL(datasetsSql).buildSimpleVW();
        return datasets;
    }

    public String getErrorResponse() {
        return errorMessage;
    }

    @Transactional
    public List<Long> getDatasetIDsByUser(String email) {
        UserSQL user = userDao.getUserByName(email);
        return datasetDao.getDatasetIDsByUser(user);
    }

    @Transactional
    public DatasetVWSQL getDatasetWithVariantsAndMatchesByID(Long datasetID) {
        DatasetVWSQL ds = datasetDao.getDataset(datasetID);
        Set<VariantSQL> variants = ds.getVariants();
        for (VariantSQL variant : variants) {
            List<MatchSQL> firstMatchSet = matchVariantDM.getMatchesByVariant(variant);
            logger.info("number of matches {}", firstMatchSet.size());
//            Set<MatchSQL> matches = variant.getMatches();
        }
        return ds;
    }

    public static void main(String[] args) {
        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
        DatasetManager vdm = inj.getInstance(DatasetManager.class);
        DatasetVWSQL ds = vdm.getDatasetWithVariantsAndMatchesByID(107002l);
        System.out.println("finish");
    }

    @Transactional
    public DatasetHGMDSQL getHGMDDatasetByID(long datasetID) {
        DatasetHGMDSQL ds = datasetDao.getHGMDDataset(datasetID);
        return ds;
    }

    @Transactional
    public Dataset getSimpleDatasetByID(Long datasetId) {
        DatasetVWSQL datasetsSql = datasetDao.getDataset(datasetId);
        Dataset dataset = datasetBuilder.withDatasetSQL(datasetsSql).buildSimpleVWSingle();
        return dataset;
    }

    public List<Long> getAllDatasetIds() {
        return datasetDao.getAllDatasetIds();
    }
}
