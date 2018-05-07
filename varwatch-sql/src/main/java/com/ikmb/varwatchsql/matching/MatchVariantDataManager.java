/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.matching;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.ikmb.varwatchcommons.entities.VWStatus;
import com.ikmb.varwatchcommons.entities.VWVariant;
import com.ikmb.varwatchcommons.entities.Gene;
import com.ikmb.varwatchcommons.entities.HPOTerm;
import com.ikmb.varwatchcommons.entities.MatchInformation;
import com.ikmb.varwatchcommons.utils.VariantHash;
import com.ikmb.varwatchsql.guice.VarWatchInjector;
import com.ikmb.varwatchsql.guice.VarWatchPersist;
import com.ikmb.varwatchsql.auth.user.UserDao;
import com.ikmb.varwatchsql.auth.user.UserManager;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.entities.DatasetHGMDSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
//import com.ikmb.varwatchsql.entities.ExternalVariantSQL;
import com.ikmb.varwatchsql.entities.VariantStatusSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetDao;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetManager;
import com.ikmb.varwatchsql.variant_data.variant.VariantBuilder;
import com.ikmb.varwatchsql.variant_data.variant.VariantDao;
import com.ikmb.varwatchsql.variant_data.variant.VariantDataManager;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.data.gene.GeneDataManager;
import com.ikmb.varwatchsql.data.hpo.HPOTermBuilder;
import com.ikmb.varwatchsql.data.hpo.PhenotypeDataManager;
import com.ikmb.varwatchsql.status.variant.VariantStatusBuilder;
import com.ikmb.varwatchsql.status.variant.VariantStatusDao;
import com.ikmb.varwatchsql.status.variant.VariantStatusManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class MatchVariantDataManager {

    private final Logger logger = LoggerFactory.getLogger(MatchVariantDataManager.class);

    @Inject
    private MatchVariantDao matchVariantDao;

    @Inject
    private VariantDao variantDao;

    @Inject
    private GeneDataManager geneDataManager;

    @Inject
    private PhenotypeDataManager hopTermDataManager;

    @Inject
    private UserManager userDataManger;

    @Inject
    private MatchBuilder matchBuilder;

    @Inject
    private VariantStatusManager varStatusManager;

    @Inject
    private DatasetManager datasetDM;

    @Inject
    private VariantStatusBuilder variantStatusBuilder;

    @Inject
    private VariantBuilder variantBuilder;

    @Inject
    private VariantStatusManager variantStatusManager;

    @Inject
    private HPOTermBuilder hpoTermBuilder;

    @Inject
    private VariantHash variantHasher;

    @Inject
    private VariantDataManager variantDM;

    @Inject
    private VariantStatusDao variantStatusDao;

    @Inject
    private UserDao userDao;

    @Inject
    private DatasetDao datasetDao;

    @Transactional
    public MatchInformation getMatchInformation(Long variantID, Long matchStatusID) {
        VariantSQL queryVariantSql = variantDao.get(variantID);
        VariantStatusSQL statusSql = varStatusManager.get(matchStatusID);
        if (!statusSql.getStatus().equals("MATCHED")) {
            return null;
        }
        MatchSQL match = statusSql.getMatchVariant().getMatch();

        boolean internalMatch = isInternalMatch(match);
        MatchInformation matchInfo = null;
        if (internalMatch) {
            matchInfo = getInternalMatchInfo(statusSql.getMatchVariant(), queryVariantSql);
        } else {
            matchInfo = getExternalMatch(statusSql.getMatchVariant(), queryVariantSql);
        }

        return matchInfo;
    }

    @Transactional
    public MatchInformation getMatchInformation(Long matchVariantID, UserSQL user) {
        //get the variant from the user
        MatchVariantSQL matchVariant = getVariantMatch(matchVariantID);
        Long variandId = matchVariant.getVariantId();
        VariantSQL queryVariantSql = variantDao.get(variandId);
        VariantStatusSQL statusSql = matchVariant.getVariantStatus();
        if (!statusSql.getStatus().equals("MATCHED")) {
            return null;
        }
        MatchSQL match = statusSql.getMatchVariant().getMatch();

        boolean internalMatch = isInternalMatch(match);
        MatchInformation matchInfo = null;
        if (internalMatch) {
            matchInfo = getInternalMatchInfo(matchVariant, queryVariantSql);
        } else {
            matchInfo = getExternalMatch(matchVariant, queryVariantSql);
        }

        return matchInfo;
    }

//    public MatchInformation getMatchInformation(VariantSQL queryVariantSql, MatchSQL match) {
//        boolean internalMatch = isInternalMatch(match);
//        MatchInformation matchInfo = null;
//        if (internalMatch) {
//            matchInfo = getInternalMatchInfo(match, queryVariantSql);
//        } else {
//            matchInfo = getExternalMatch(match, queryVariantSql);
//        }
//
//        return matchInfo;
//    }
    public MatchInformation getMatchInformation(VariantSQL queryVariantSql, MatchVariantSQL match) {
        boolean internalMatch = isInternalMatch(match.getMatch());
        MatchInformation matchInfo = null;
        if (internalMatch) {
            matchInfo = getInternalMatchInfo(match, queryVariantSql);
        } else {
            matchInfo = getExternalMatch(match, queryVariantSql);
        }

        return matchInfo;
    }

    public MatchVariantSQL getTargetVariant(VariantSQL queryVariantSql, Set<MatchVariantSQL> variants) {
        List<MatchVariantSQL> variantMatch = new ArrayList<>();
        for (MatchVariantSQL curVar : variants) {
            if (curVar.getVariantId() == null || !curVar.getVariantId().equals(queryVariantSql.getId())) {
                variantMatch.add(curVar);
            }
        }
        if (variantMatch.size() == 1) {
            return variantMatch.get(0);
        } else {
            System.out.println("more than one variantmatch with variant query id " + queryVariantSql.getId() + "!!!!");
            for (MatchVariantSQL curVar : variantMatch) {
                System.out.println("matchid: " + curVar.getId());
            }
            return null;
        }
    }

    private boolean isInternalMatch(MatchSQL match) {
        for (MatchVariantSQL variant : match.getVariants()) {
            if (!variant.getDatabase().getName().equals("VarWatch")) {
                return false;
            }
        }
        return true;
    }

//    private MatchInformation getInternalMatchInfo(MatchSQL match, VariantSQL queryVariantSql) {
//        MatchVariantSQL targetMatchVariant = getTargetVariant(queryVariantSql, match.getVariants());
//        VariantSQL targetVariant = variantDM.get(targetMatchVariant.getVariantId());
//        List<Gene> genes = geneDataManager.getGenes(targetVariant.getId());
//        List<HPOTerm> phenotypes = hopTermDataManager.getPhenotypes(targetVariant.getDataset().getId());
//        UserSQL user = targetVariant.getDataset().getUser();
//        MatchInformation matchInfo = matchBuilder.withVariantSql(targetVariant).withGenes(genes).withHpoTermsSql(phenotypes).withDatasetId(queryVariantSql.getDataset().getId()).withQueryVariantId(queryVariantSql.getId()).withUserSql(user).withMatch(match).buildInternal();
//        return matchInfo;
//    }
//
//    private MatchInformation getExternalMatch(MatchSQL match, VariantSQL queryVariantSql) {
//        MatchVariantSQL targetMatchVariant = getTargetVariant(queryVariantSql, match.getVariants());
//        String databaseName = targetMatchVariant.getDatabase().getName();
//        String identifier = null;
//        if (databaseName.equals("HGMD")) {
//            identifier = datasetDM.getHGMDDatasetByID(targetMatchVariant.getVariantId()).getHgmdAcc();
//        }
//        MatchInformation matchInfo = matchBuilder.withDatasetId(queryVariantSql.getDataset().getId()).withQueryVariantId(queryVariantSql.getId()).withAccIdentifier(identifier).withDatabase(databaseName).withMatch(match).buildExternal();
//        return matchInfo;
//    }
    private MatchInformation getInternalMatchInfo(MatchVariantSQL match, VariantSQL queryVariantSql) {
        MatchVariantSQL targetMatchVariant = getTargetVariant(queryVariantSql, match.getMatch().getVariants());
        if (targetMatchVariant == null) {
            return null;
        }
        VariantSQL targetVariant = variantDM.get(targetMatchVariant.getVariantId());
        List<Gene> genes = geneDataManager.getGenes(targetVariant.getId());
        List<HPOTerm> phenotypes = hopTermDataManager.getPhenotypes(targetVariant.getDataset().getId());
        UserSQL user = targetVariant.getDataset().getUser();
        MatchInformation matchInfo = matchBuilder.withVariantSql(targetVariant).withGenes(genes).withHpoTermsSql(phenotypes).withDatasetId(queryVariantSql.getDataset().getId()).withQueryVariantId(queryVariantSql.getId()).withUserSql(user).withMatch(match).buildInternal();
        return matchInfo;
    }

    private MatchInformation getExternalMatch(MatchVariantSQL match, VariantSQL queryVariantSql) {
        MatchVariantSQL targetMatchVariant = getTargetVariant(queryVariantSql, match.getMatch().getVariants());
        String databaseName = targetMatchVariant.getDatabase().getName();
        String identifier = null;
        if (databaseName.equals("HGMD")) {
            identifier = datasetDM.getHGMDDatasetByID(targetMatchVariant.getVariantId()).getHgmdAcc();
        }
        MatchInformation matchInfo = matchBuilder.withDatasetId(queryVariantSql.getDataset().getId()).withQueryVariantId(queryVariantSql.getId()).withAccIdentifier(identifier).withDatabase(databaseName).withMatch(match).buildExternal();
        return matchInfo;
    }

    @Transactional
    public List<VariantSQL> getSimilarVWVariants(VariantSQL queryVariant, boolean geneBool, boolean pathwayBool, boolean familyBool) {

        return matchVariantDao.getSimilarVariants(queryVariant, geneBool, pathwayBool, familyBool);

    }

    @Transactional
    public List<MatchSQL> persistMatches(List<MatchSQL> matches) {
        return matchVariantDao.persistMatches(matches);
    }

    @Transactional
    public boolean persistVWMatch(MatchSQL matchesgrp) {
        return matchVariantDao.persistVWMatch(matchesgrp);
    }

    @Transactional
    public void deleteBeaconMatchedVariants(DatasetVWSQL dataset) {
        dataset = datasetDM.getDatasetWithVariantsAndMatchesByID(dataset.getId());
//delete variants with beacon match
//        Map<VariantSQL, List<MatchSQL>> beaconMatchedVariants = new HashMap<>();
//        List<VariantSQL> variants = variantDM.getVariantsByDatasetWithMatches(_dataset.getId());
        Map<VariantSQL, String> variantToBeaconName = new HashMap<>();
        List<VariantSQL> beaconMatchedVariants = new ArrayList<>();
        for (VariantSQL variant : dataset.getVariants()) {
            logger.info("search for beacon matches with variant id {}", variant.getId());
            List<MatchSQL> matches = getMatchesByVariant(variant);
            for (MatchSQL match : matches) {
                if (match.getDatabase().getImplementation().equals("global_beacon")) {
                    logger.info("match found for variant with id {}", variant.getId());
                    String beaconName = match.getDatabase().getName();
                    variantToBeaconName.put(variant, beaconName);

                    beaconMatchedVariants.add(variant);
                }
            }
        }
        for (VariantSQL curVar : beaconMatchedVariants) {
            //delete Variant, set Variantstatus, remove Matches
            VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatus.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.MATCHED_TO_BEACON.getMessage()).withMessageAddition(variantToBeaconName.get(curVar)).buildVWStatus();
            VWVariant buildVW = variantBuilder.withSQLVariant(curVar).buildVW();
            String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
            UserSQL user = userDao.getUserByID(dataset.getUser().getId());
            VariantStatusSQL variantstatus = variantStatusBuilder.withStatusVW(status, null, variantHash, dataset, user).buildSql();
            variantStatusDao.save(variantstatus);
//            List<MatchSQL> matches = beaconMatchedVariants.get(curVar);
//            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Nr of Matches:{0}", matches.size());
            logger.info("remove variant with id {} " + curVar.getId());
            variantDao.removeVariantWithMatches(curVar);
        }
    }

//    @Transactional
//    public List<MatchGroupSQL> getDatasetWithMatches(List<MatchGroupSQL> matchesgrps) {
//        matchVariantDao.persistMatches(matchesgrps);
//        return matchesgrps;
//    }
    @Transactional
    public List<DatasetHGMDSQL> getSimilarHGMDVariants(VariantSQL queryVariant, boolean geneBool, boolean pathwayBool, boolean familyBool) {
        return matchVariantDao.getSimilarHGMDVariants(queryVariant, geneBool, pathwayBool, familyBool);
    }

    @Transactional
    public List<DatasetHGMDSQL> getIdenticalHGMDVariant(VariantSQL variant) {
        return matchVariantDao.getIdenticalHGMDVariants(variant);
    }

    @Transactional
    public List<VariantSQL> getIdenticalVariant(VariantSQL variant) {
        List<VariantSQL> identicalVariants = matchVariantDao.getIdenticalVariants(variant);
        logger.info("user: " + variant.getDataset().getUser().getMail());
        logger.info("datasetid: " + variant.getDataset().getId());
        return filterForNonFinishedDatasets(identicalVariants);
    }

    @Transactional
    public boolean removeMatch(MatchSQL match) {
        return matchVariantDao.remove(match);
    }

    @Transactional
    public List<MatchSQL> getMatchesByVariant(VariantSQL variant) {
        return matchVariantDao.getVarWatchMatchesByVariantId(variant.getId());
    }

    @Transactional
    public List<DatasetHGMDSQL> getSimilarHGMDByGene(VariantSQL variant) {
        return matchVariantDao.getSimilarHGMDVariantsByGene(variant);
    }

    @Transactional
    public List<VariantSQL> getSimilarVWByGene(VariantSQL variant) {
//        return matchVariantDao.getSimilarVWVariantsByGene(variant);
        List<VariantSQL> matchedVariants = matchVariantDao.getSimilarVWVariantsByGene(variant);
        return filterForNonFinishedDatasets(matchedVariants);
    }

    @Transactional
    public List<DatasetHGMDSQL> getSimilarHGMDByFamily(VariantSQL variant) {
        return matchVariantDao.getSimilarHGMDVariantsByFamily(variant);
    }

    @Transactional
    public List<VariantSQL> getSimilarVWByFamily(VariantSQL variant) {
        List<VariantSQL> matchedVariants = matchVariantDao.getSimilarVWVariantsByFamily(variant);
        return filterForNonFinishedDatasets(matchedVariants);
//        return matchVariantDao.getSimilarVWVariantsByFamily(variant);
    }

    public static void main(String[] args) {
        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
        MatchVariantDataManager vdm = inj.getInstance(MatchVariantDataManager.class);
        MatchSQL match = vdm.get(13267l);
        boolean bup = vdm.isDuplicatedMatch(match);
        System.out.println(bup);
        System.out.println("finish");
    }

    private List<VariantSQL> filterForNonFinishedDatasets(List<VariantSQL> matchedVariants) {
        List<VariantSQL> filteredVariants = new ArrayList<>();
        int notCompletedMatches = 0;
        Set<String> usersCompl = new HashSet<>();
        Set<String> usersNotCompl = new HashSet<>();
        for (VariantSQL curVar : matchedVariants) {
            variantDao.refresh(curVar);
            datasetDao.refresh(curVar.getDataset());
            if (curVar.getDataset().getCompleted()) {
                filteredVariants.add(curVar);
                usersCompl.add(curVar.getDataset().getUser().getMail());
            } else {
                usersNotCompl.add(curVar.getDataset().getUser().getMail());
                notCompletedMatches++;
            }
        }
        logger.info("Not completed matches found: " + notCompletedMatches);
        logger.info(Arrays.toString(usersCompl.toArray()));
        logger.info(Arrays.toString(usersNotCompl.toArray()));
        logger.info("------------");
        return filteredVariants;
    }

    @Transactional
    public List<MatchInformation> getNewMatchInformation(UserSQL user) {

//           List<VariantStatusSQL> filteredVwVariants = filterVwMatchedVariants(vwmatchedVariantsSql, variant);
//        List<VariantStatusSQL> filteredHGMDVariants = filterVwMatchedVariants(hgmdMatchedVariantsSql, variant);
//        List<MatchVariantSQL> matches = matchVariantDao.getNotNotifiedMatchesByUser(user);
//        List<MatchInformation> matchInformations = new ArrayList<>();
//        for (MatchVariantSQL match : matches) {
//            Long variantId = match.getVariantId();
//            VariantSQL queryVariantSql = variantDao.get(variantId);
//            MatchInformation matchInfo = getMatchInformation(queryVariantSql, match.getMatch());
//            matchInformations.add(matchInfo);
//        }
//        return matchInformations;
        List<MatchVariantSQL> matches = matchVariantDao.getNotNotifiedMatchesByUser(user);
//        Map<Long, List<MatchVariantSQL>> variantToMatches = new HashMap<>();

        //collect all matches for a variant
//        for (MatchVariantSQL match : matches) {
//            Long variantId = match.getVariantId();
//            if (variantToMatches.containsKey(variantId)) {
//                List<MatchVariantSQL> curMatches = variantToMatches.get(variantId);
//                curMatches.add(match);
//                variantToMatches.put(variantId, curMatches);
//            } else {
//                List<MatchVariantSQL> curMatches = new ArrayList<>();
//                curMatches.add(match);
//                variantToMatches.put(variantId, curMatches);
//            }
//        }
        //filter the matches for each variant
//        List<MatchVariantSQL> filteredVariants = new ArrayList<>();
//        for (Entry<Long, List<MatchVariantSQL>> mapEntry : variantToMatches.entrySet()) {
//            Long variantId = mapEntry.getKey();
//            List<MatchVariantSQL> matchedVariants = mapEntry.getValue();
//            VariantSQL variant = variantDao.get(variantId);
//
//            List<MatchVariantSQL> curFilteredVariants = getFilteredList(matchedVariants, variant);
//            filteredVariants.addAll(curFilteredVariants);
//
//            //set notified = true to all non filteredMatches TODO maybe discard all matches in the screening worker?
//            Set<Long> filteredIds = new HashSet<>();
//            for (MatchVariantSQL curMatch : curFilteredVariants) {
//                filteredIds.add(curMatch.getId());
//            }
//            Set<Long> nonFilteredIds = new HashSet<>();
//            for (MatchVariantSQL curMatch : matchedVariants) {
//                if (!filteredIds.contains(curMatch.getId())) {
//                    nonFilteredIds.add(curMatch.getId());
//                }
//            }
//            for (Long curMatch : nonFilteredIds) {
//                MatchVariantSQL curMatchSql = matchVariantDao.getMatchVariant(curMatch);
//                curMatchSql.setNotified(Boolean.TRUE);
//                matchVariantDao.update(curMatchSql);
//            }
//        }
        //convert to matchInfo
        List<MatchInformation> matchInformations = new ArrayList<>();
        for (MatchVariantSQL match : matches) {
            Long variantId = match.getVariantId();
            VariantSQL queryVariantSql = variantDao.get(variantId);
            MatchInformation matchInfo = getMatchInformation(queryVariantSql, match);
            if (matchInfo != null) {
                matchInformations.add(matchInfo);
            }
        }
        return matchInformations;
    }

    @Transactional
    public void setMatchAck(Long matchStatusID, UserSQL user) {
        MatchVariantSQL varMatch = getVariantMatch(matchStatusID);
        logger.info("update match with id {}", varMatch.getId());
        varMatch.setNotified(Boolean.TRUE);
        matchVariantDao.update(varMatch);
    }

    @Transactional
    public List<MatchInformation> getMatchesByInterval(UserSQL user, DateTime lastReport, DateTime nextCreationDate) {
        List<MatchVariantSQL> matches = matchVariantDao.getMatchesInInterval(user, lastReport, nextCreationDate);
        List<MatchInformation> matchInformations = new ArrayList<>();
        for (MatchVariantSQL match : matches) {
            Long variantId = match.getVariantId();
            VariantSQL queryVariantSql = variantDao.get(variantId);
            MatchInformation matchInfo = getMatchInformation(queryVariantSql, match);
            matchInformations.add(matchInfo);
        }
        return matchInformations;
    }

    private List<MatchVariantSQL> filterMatchedVariants(List<MatchVariantSQL> vwmatchedVariantsSql, VariantSQL queryVariant) {
        List<MatchVariantSQL> vwStatuses = getVWStatusEntries(vwmatchedVariantsSql);
        List<MatchVariantSQL> hgmdStatuses = getHgmdStatusEntries(vwmatchedVariantsSql);

        List<MatchVariantSQL> filteredStatusEntries = new ArrayList<>();
        filteredStatusEntries.addAll(getFilteredList(vwStatuses, queryVariant));
        filteredStatusEntries.addAll(getFilteredList(hgmdStatuses, queryVariant));
        return filteredStatusEntries;
    }

    private List<MatchVariantSQL> getVWStatusEntries(List<MatchVariantSQL> vwmatchedVariantsSql) {
        List<MatchVariantSQL> vwVariantStatusEntries = new ArrayList<>();
        for (MatchVariantSQL curStatus : vwmatchedVariantsSql) {
            if (curStatus.getDatabase().equals("VarWatch")) {
                vwVariantStatusEntries.add(curStatus);
            }
        }
        return vwVariantStatusEntries;
    }

    private List<MatchVariantSQL> getHgmdStatusEntries(List<MatchVariantSQL> vwmatchedVariantsSql) {
        List<MatchVariantSQL> vwVariantStatusEntries = new ArrayList<>();
        for (MatchVariantSQL curStatus : vwmatchedVariantsSql) {
            if (curStatus.getDatabase().equals("HGMD")) {
                vwVariantStatusEntries.add(curStatus);
            }
        }
        return vwVariantStatusEntries;
    }

    public List<MatchVariantSQL> getFilteredList(List<MatchVariantSQL> vwmatchedVariantsSql, VariantSQL queryVariant) {
        if (vwmatchedVariantsSql.size() <= 5) {
            return vwmatchedVariantsSql;
        }

        //filter hpo
        List<MatchVariantSQL> hpofilteredVariants = filterHpo(vwmatchedVariantsSql, 5);

        if (hpofilteredVariants.size() <= 5) {
            return hpofilteredVariants;
        }

        //filter chrom
        List<MatchVariantSQL> chromFilteredVariants = filterChrom(queryVariant, hpofilteredVariants, 5);

        if (chromFilteredVariants.size() <= 5) {
            return chromFilteredVariants;
        }

        //filter position
        List<MatchVariantSQL> posFilteredVariants = filterPos(queryVariant, chromFilteredVariants, 5);

        return posFilteredVariants;
    }

    private List<MatchVariantSQL> filterHpo(List<MatchVariantSQL> matchedVariantsSql, int i) {
        Map<Double, List<MatchVariantSQL>> variantToHPO = new HashMap<>();
        Set<Double> hpoValues = new HashSet<>();
        for (MatchVariantSQL curStatusl : matchedVariantsSql) {
            Double hpo = curStatusl.getMatch().getHpoDist();
            if (variantToHPO.containsKey(hpo)) {
                variantToHPO.get(hpo).add(curStatusl);
            } else {
                List<MatchVariantSQL> variantStatusList = new ArrayList<>();
                variantStatusList.add(curStatusl);
                variantToHPO.put(hpo, variantStatusList);
            }
            hpoValues.add(hpo);
        }

        List<Double> hpoValueList = new ArrayList<>();
        hpoValueList.addAll(hpoValues);
        Collections.sort(hpoValueList, Collections.reverseOrder());
        boolean variantStatusBoxFull = false;
        List<MatchVariantSQL> filteredVariants = new ArrayList<>();
        for (Double currentHPO : hpoValueList) {
            List<MatchVariantSQL> currentStatusList = variantToHPO.get(currentHPO);
            for (MatchVariantSQL curStatusl : currentStatusList) {
                if (!variantStatusBoxFull) {
                    filteredVariants.add(curStatusl);
                }
            }
            if (filteredVariants.size() >= i) {
                break;
            }
        }
        return filteredVariants;
    }

    private List<MatchVariantSQL> filterChrom(VariantSQL queryVariant, List<MatchVariantSQL> matchedVariantsSql, int i) {
        List<MatchVariantSQL> filteredVariants = new ArrayList<>();
        for (MatchVariantSQL status : matchedVariantsSql) {
            Set<MatchVariantSQL> matches = status.getMatch().getVariants();
            MatchVariantSQL matchVariant = getTargetVariant(queryVariant, matches);
            if (matchVariant.getDatabase().getName().equals("HGMD")) {
                DatasetHGMDSQL ds = datasetDao.getHGMDDataset(matchVariant.getVariantId());
                if (ds.getChrName().equals(queryVariant.getChromosomeName())) {
                    filteredVariants.add(status);
                }
            } else if (matchVariant.getDatabase().getName().equals("VarWatch")) {
                VariantSQL var = variantDao.get(matchVariant.getVariantId());
                if (var.getChromosomeName().equals(queryVariant.getChromosomeName())) {
                    filteredVariants.add(status);
                }
            }
        }
        return filteredVariants;
    }

    private List<MatchVariantSQL> filterPos(VariantSQL queryVariant, List<MatchVariantSQL> matchedVariantsSql, int i) {
        Map<Integer, List<MatchVariantSQL>> distance2Variant = new HashMap<>();
        for (MatchVariantSQL status : matchedVariantsSql) {
            Set<MatchVariantSQL> matches = status.getMatch().getVariants();
            MatchVariantSQL matchVariant = getTargetVariant(queryVariant, matches);
            Integer posisiton = null;
            if (matchVariant.getDatabase().getName().equals("HGMD")) {
                DatasetHGMDSQL ds = datasetDao.getHGMDDataset(matchVariant.getVariantId());
                posisiton = ds.getChrPos();
            } else if (matchVariant.getDatabase().getName().equals("VarWatch")) {
                VariantSQL var = variantDao.get(matchVariant.getVariantId());
                posisiton = var.getChromosomePos();
            }
            Integer distance = Math.abs(queryVariant.getChromosomePos() - posisiton);
            if (distance2Variant.containsKey(posisiton)) {
                distance2Variant.get(posisiton).add(status);
            } else {
                List<MatchVariantSQL> variantStatusList = new ArrayList<>();
                variantStatusList.add(status);
                distance2Variant.put(distance, variantStatusList);
            }
        }

        List<Integer> positions = new ArrayList<>();
        positions.addAll(distance2Variant.keySet());
        Collections.sort(positions);
        boolean variantStatusBoxFull = false;
        List<MatchVariantSQL> filteredVariants = new ArrayList<>();
        for (Integer currentPos : positions) {
            List<MatchVariantSQL> currentStatusList = distance2Variant.get(currentPos);
            for (MatchVariantSQL curStatusl : currentStatusList) {
                if (!variantStatusBoxFull) {
                    filteredVariants.add(curStatusl);
                }
            }
            if (filteredVariants.size() >= i) {
                break;
            }
        }
        return filteredVariants;
    }

    @Transactional
    public MatchVariantSQL getVariantMatch(Long dataId) {
        return matchVariantDao.getMatchVariant(dataId);
    }

    @Transactional
    public MatchSQL get(Long matchId) {
        return matchVariantDao.getMatchById(matchId);
    }

    @Transactional
    public void setMatchesAck(UserSQL user) {
        List<MatchVariantSQL> matches = matchVariantDao.getNotNotifiedMatchesByUser(user);
        for (MatchVariantSQL curMatch : matches) {
            logger.info("update match with id {}", curMatch.getId());
            curMatch.setNotified(Boolean.TRUE);
            matchVariantDao.update(curMatch);
        }
    }

    public List<VariantSQL> getVariantsWithSameAminoAcidExchange(VariantSQL variant) {
        List<VariantSQL> identicalVariants = matchVariantDao.getVariantsWithSameAminoAcidExchange(variant);
        logger.info("user: " + variant.getDataset().getUser().getMail());
        logger.info("datasetid: " + variant.getDataset().getId());
        return filterForNonFinishedDatasets(identicalVariants);
    }

    public List<DatasetHGMDSQL> getHgmdVariantsWithSameAminoAcidExchange(VariantSQL variant) {
        List<DatasetHGMDSQL> identicalVariants = matchVariantDao.getHgmdVariantsWithSameAminoAcidExchange(variant);
        return identicalVariants;
    }

    public boolean isDuplicatedMatch(MatchSQL curMatch) {
        return matchVariantDao.isDuplicatedMatch(curMatch);
    }

}
