/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.dataset;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.auth.client.AuthClient;
import com.ikmb.core.data.auth.client.ClientDao;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.auth.user.UserDao;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.varwatchcommons.entities.Feature;
import com.ikmb.core.varwatchcommons.entities.VWMatchRequest;
import com.ikmb.core.varwatchcommons.entities.Dataset;
import com.ikmb.core.data.hpo.HPOTermBuilder;
import com.ikmb.core.data.hpo.Phenotype;
import com.ikmb.core.data.hpo.PhenotypeDao;
import com.ikmb.core.data.matching.Match;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.varwatchcommons.utils.VarWatchException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private DatasetBuilder datasetBuilder;

    @Inject
    private MatchVariantDataManager matchVariantDM;

    private String errorMessage;

    @Transactional
    public Long persistRawData(VWMatchRequest submitrequest, User user, AuthClient client, DatasetBuilder.RawDataType rawDataType) throws VarWatchException {
        AuthClient clientSql = clientDao.getClientByID(client.getId());
        User userSQL = userDao.getUserByID(user.getId());

        validateAssembly(submitrequest.getPatient().getAssembly());

        validateHpoTerms(submitrequest.getPatient().getFeatures());

        Set<Phenotype> phenotypes = getPhenotypesFromFeatures(submitrequest.getPatient().getFeatures());
        HPOTerm inheritanceMode = hpoDao.getHPOTermByName(submitrequest.getPatient().getInheritanceMode(), "I");
        HPOTerm ageOfOnset = hpoDao.getHPOTermByName(submitrequest.getPatient().getAgeOfOnset(), "C");

        DatasetVW datasetSQL = datasetBuilder.withVWMatchRequest(submitrequest, rawDataType).withClient(clientSql).withUser(userSQL).withPhenotypes(phenotypes).withAgeOfOnset(ageOfOnset).withModeOfInheritance(inheritanceMode).buildRawVWSql();
        persistDataset(datasetSQL);
        return datasetSQL.getId();
    }

    private void validateAssembly(String assembly) throws VarWatchException {
        if (assembly == null || (!(assembly.equals("GRCh38") || assembly.equals("GRCh37")))) {
            logger.error("invalid assembly: {}", assembly);
            throw new VarWatchException("Invalid assembly: " + assembly);
        }
    }

    private void validateHpoTerms(List<Feature> features) throws VarWatchException {
        for (Feature vwFeature : features) {
            HPOTerm phenotypeSql = hpoDao.getHPOTermByName(vwFeature.getId(), "O");
            logger.info("current hpo term {}", vwFeature.getId());
            if (phenotypeSql == null) {
                logger.error("invalid hpo term {}", vwFeature.getId());
                throw new VarWatchException("invalid hpo term " + vwFeature.getId());
            }
        }
    }

    @Transactional
    public DatasetVW getDatasetWithVariantsByID(Long datasetID) {
        DatasetVW ds = datasetDao.getDataset(datasetID);
        ds.getVariants();
        return ds;
    }

    @Transactional
    public DatasetVW getDatasetByID(Long datasetID) {
        DatasetVW ds = datasetDao.getDataset(datasetID);
        return ds;
    }

    @Transactional
    public void updateDataset(DatasetVW dataset) {
        datasetDao.update(dataset);
    }

    @Transactional
    public void refresh(DatasetVW dataset) {
        datasetDao.refresh(dataset);
    }

    @Transactional
    public List<Dataset> getSimpleDatasetByUserId(Integer userId) {
        User user = userDao.getUserByID(userId);
        List<DatasetVW> datasetsSql = datasetDao.getDatasetsByUser(user);
        logger.info("nr of sql datasets {}", datasetsSql.size());
        List<Dataset> datasets = datasetBuilder.withDatasetsSQL(datasetsSql).buildSimpleVW();
        return datasets;
    }

    public String getErrorResponse() {
        return errorMessage;
    }

    @Transactional
    public List<Long> getDatasetIDsByUser(String email) {
        User user = userDao.getUserByName(email);
        return datasetDao.getDatasetIDsByUser(user);
    }

    @Transactional
    public DatasetVW getDatasetWithVariantsAndMatchesByID(Long datasetID) {
        DatasetVW ds = datasetDao.getDataset(datasetID);
        Set<Variant> variants = ds.getVariants();
        for (Variant variant : variants) {
            List<Match> firstMatchSet = matchVariantDM.getMatchesByVariant(variant);
            logger.info("number of matches {}", firstMatchSet.size());
//            Set<MatchSQL> matches = variant.getMatches();
        }
        return ds;
    }

//    public static void main(String[] args) {
//        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
//        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
//        DatasetManager vdm = inj.getInstance(DatasetManager.class);
//        DatasetVWSQL ds = vdm.getDatasetWithVariantsAndMatchesByID(107002l);
//        System.out.println("finish");
//    }
    @Transactional
    public DatasetHGMD getHGMDDatasetByID(long datasetID) {
        DatasetHGMD ds = datasetDao.getHGMDDataset(datasetID);
        return ds;
    }

    @Transactional
    public Dataset getSimpleDatasetByID(Long datasetId) {
        DatasetVW datasetsSql = datasetDao.getDataset(datasetId);
        Dataset dataset = datasetBuilder.withDatasetSQL(datasetsSql).buildSimpleVWSingle();
        return dataset;
    }

    public List<Long> getAllDatasetIds() {
        return datasetDao.getAllDatasetIds();
    }

    @Transactional
    public List<HPOTerm> getPhenotypes(Long datasetId) {
        DatasetVW dataset = datasetDao.getDataset(datasetId);
        Set<Phenotype> hpoTermsSql = dataset.getPhenotypes();
        List<HPOTerm> hpoTerms = new HPOTermBuilder().addFeatures(hpoTermsSql).buildList();
        return hpoTerms;
    }

    public void setDatasetDao(DatasetDao datasetDao) {
        this.datasetDao = datasetDao;
    }

    private Set<Phenotype> getPhenotypesFromFeatures(List<Feature> features) {
        Set<Phenotype> phenotypes = new HashSet<>();
        for (Feature vwFeature : features) {
            String ageOfOnset = vwFeature.getAgeOfOnset();
            HPOTerm ageOfOnsetSql = hpoDao.getHPOTermByName(ageOfOnset, "C");
            Boolean observed = false;
            if (vwFeature.getObserved() != null && vwFeature.getObserved().equalsIgnoreCase("true")) {
                observed = true;
            }
            HPOTerm phenotypeSql = hpoDao.getHPOTermByName(vwFeature.getId(), "O");
            logger.info("current hpo term {}", vwFeature.getId());
            if (phenotypeSql == null) {
                continue;
            }
            Phenotype phenotype = new Phenotype();
            phenotype.setAgeOfOnset(ageOfOnsetSql);
            phenotype.setObserved(observed);
            phenotype.setPhenotype(phenotypeSql);
            phenotypes.add(phenotype);
        }
        return phenotypes;
    }

    @Transactional
    public void persistDataset(DatasetVW datasetSQL) {
        datasetDao.save(datasetSQL);
    }

}
