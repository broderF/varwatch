/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.matching;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.auth.user.UserDao;
import com.ikmb.core.data.auth.user.UserManager;
import com.ikmb.core.data.dataset.DatasetDao;
import com.ikmb.core.data.dataset.DatasetHGMD;
import com.ikmb.core.varwatchcommons.entities.VWStatus;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import com.ikmb.core.varwatchcommons.entities.MatchInformation;
import com.ikmb.core.varwatchcommons.utils.VariantHash;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.gene.Gene;
import com.ikmb.core.data.variant.VariantBuilder;
import com.ikmb.core.data.variant.VariantDao;
import com.ikmb.core.data.variant.VariantDataManager;
import com.ikmb.core.data.gene.GeneDataManager;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.data.hpo.HPOTermBuilder;
import com.ikmb.core.data.hpo.PhenotypeDataManager;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantStatus;
import com.ikmb.core.data.variant.VariantStatusBuilder;
import com.ikmb.core.data.variant.VariantStatusDao;
import com.ikmb.core.data.variant.VariantStatusManager;
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
        Variant queryVariantSql = variantDao.get(variantID);
        VariantStatus statusSql = varStatusManager.get(matchStatusID);
        if (!statusSql.getStatus().equals("MATCHED")) {
            return null;
        }
        Match match = statusSql.getMatchVariant().getMatch();

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
    public MatchInformation getMatchInformation(Long matchVariantID, User user) {
        //get the variant from the user
        MatchVariant matchVariant = getVariantMatch(matchVariantID);
        Long variandId = matchVariant.getVariantId();
        Variant queryVariantSql = variantDao.get(variandId);
        VariantStatus statusSql = matchVariant.getVariantStatus();
        if (!statusSql.getStatus().equals("MATCHED")) {
            return null;
        }
        Match match = statusSql.getMatchVariant().getMatch();

        boolean internalMatch = isInternalMatch(match);
        MatchInformation matchInfo = null;
        if (internalMatch) {
            matchInfo = getInternalMatchInfo(matchVariant, queryVariantSql);
        } else {
            matchInfo = getExternalMatch(matchVariant, queryVariantSql);
        }

        return matchInfo;
    }

//    public MatchInformation getMatchInformation(Variant queryVariantSql, Match match) {
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
    public MatchInformation getMatchInformation(Variant queryVariantSql, MatchVariant match) {
        boolean internalMatch = isInternalMatch(match.getMatch());
        MatchInformation matchInfo = null;
        if (internalMatch) {
            matchInfo = getInternalMatchInfo(match, queryVariantSql);
        } else {
            matchInfo = getExternalMatch(match, queryVariantSql);
        }

        return matchInfo;
    }

    public MatchVariant getTargetVariant(Variant queryVariantSql, Set<MatchVariant> variants) {
        List<MatchVariant> variantMatch = new ArrayList<>();
        for (MatchVariant curVar : variants) {
            if (curVar.getVariantId() == null || !curVar.getVariantId().equals(queryVariantSql.getId())) {
                variantMatch.add(curVar);
            }
        }
        if (variantMatch.size() == 1) {
            return variantMatch.get(0);
        } else {
            System.out.println("more than one variantmatch with variant query id " + queryVariantSql.getId() + "!!!!");
            for (MatchVariant curVar : variantMatch) {
                System.out.println("matchid: " + curVar.getId());
            }
            return null;
        }
    }

    private boolean isInternalMatch(Match match) {
        for (MatchVariant variant : match.getVariants()) {
            if (!variant.getDatabase().getName().equals("VarWatch")) {
                return false;
            }
        }
        return true;
    }

//    private MatchInformation getInternalMatchInfo(Match match, Variant queryVariantSql) {
//        MatchVariant targetMatchVariant = getTargetVariant(queryVariantSql, match.getVariants());
//        Variant targetVariant = variantDM.get(targetMatchVariant.getVariantId());
//        List<Gene> genes = geneDataManager.getGenes(targetVariant.getId());
//        List<HPOTerm> phenotypes = hopTermDataManager.getPhenotypes(targetVariant.getDataset().getId());
//        User user = targetVariant.getDataset().getUser();
//        MatchInformation matchInfo = matchBuilder.withVariantSql(targetVariant).withGenes(genes).withHpoTermsSql(phenotypes).withDatasetId(queryVariantSql.getDataset().getId()).withQueryVariantId(queryVariantSql.getId()).withUserSql(user).withMatch(match).buildInternal();
//        return matchInfo;
//    }
//
//    private MatchInformation getExternalMatch(Match match, Variant queryVariantSql) {
//        MatchVariant targetMatchVariant = getTargetVariant(queryVariantSql, match.getVariants());
//        String databaseName = targetMatchVariant.getDatabase().getName();
//        String identifier = null;
//        if (databaseName.equals("HGMD")) {
//            identifier = datasetDM.getHGMDDatasetByID(targetMatchVariant.getVariantId()).getHgmdAcc();
//        }
//        MatchInformation matchInfo = matchBuilder.withDatasetId(queryVariantSql.getDataset().getId()).withQueryVariantId(queryVariantSql.getId()).withAccIdentifier(identifier).withDatabase(databaseName).withMatch(match).buildExternal();
//        return matchInfo;
//    }
    private MatchInformation getInternalMatchInfo(MatchVariant match, Variant queryVariantSql) {
        MatchVariant targetMatchVariant = getTargetVariant(queryVariantSql, match.getMatch().getVariants());
        if (targetMatchVariant == null) {
            return null;
        }
        Variant targetVariant = variantDM.get(targetMatchVariant.getVariantId());
        List<Gene> genes = geneDataManager.getGenes(targetVariant.getId());
        List<HPOTerm> phenotypes = hopTermDataManager.getPhenotypes(targetVariant.getDataset().getId());
        User user = targetVariant.getDataset().getUser();
        MatchInformation matchInfo = matchBuilder.withVariantSql(targetVariant).withGenes(genes).withHpoTermsSql(phenotypes).withDatasetId(queryVariantSql.getDataset().getId()).withQueryVariantId(queryVariantSql.getId()).withUserSql(user).withMatch(match).buildInternal();
        return matchInfo;
    }

    private MatchInformation getExternalMatch(MatchVariant match, Variant queryVariantSql) {
        MatchVariant targetMatchVariant = getTargetVariant(queryVariantSql, match.getMatch().getVariants());
        String databaseName = targetMatchVariant.getDatabase().getName();
        String identifier = null;
        if (databaseName.equals("HGMD")) {
            identifier = datasetDM.getHGMDDatasetByID(targetMatchVariant.getVariantId()).getHgmdAcc();
        }
        MatchInformation matchInfo = matchBuilder.withDatasetId(queryVariantSql.getDataset().getId()).withQueryVariantId(queryVariantSql.getId()).withAccIdentifier(identifier).withDatabase(databaseName).withMatch(match).buildExternal();
        return matchInfo;
    }

    @Transactional
    public List<Variant> getSimilarVWVariants(Variant queryVariant, boolean geneBool, boolean pathwayBool, boolean familyBool) {

        return matchVariantDao.getSimilarVariants(queryVariant, geneBool, pathwayBool, familyBool);

    }

    @Transactional
    public List<Match> persistMatches(List<Match> matches) {
        return matchVariantDao.persistMatches(matches);
    }

    @Transactional
    public boolean persistVWMatch(Match matchesgrp) {
        return matchVariantDao.persistVWMatch(matchesgrp);
    }

    @Transactional
    public void deleteBeaconMatchedVariants(DatasetVW dataset) {
        dataset = datasetDM.getDatasetWithVariantsAndMatchesByID(dataset.getId());
//delete variants with beacon match
//        Map<Variant, List<Match>> beaconMatchedVariants = new HashMap<>();
//        List<Variant> variants = variantDM.getVariantsByDatasetWithMatches(_dataset.getId());
        Map<Variant, String> variantToBeaconName = new HashMap<>();
        List<Variant> beaconMatchedVariants = new ArrayList<>();
        for (Variant variant : dataset.getVariants()) {
            logger.info("search for beacon matches with variant id {}", variant.getId());
            List<Match> matches = getMatchesByVariant(variant);
            for (Match match : matches) {
                if (match.getDatabase().getImplementation().equals("global_beacon")) {
                    logger.info("match found for variant with id {}", variant.getId());
                    String beaconName = match.getDatabase().getName();
                    variantToBeaconName.put(variant, beaconName);

                    beaconMatchedVariants.add(variant);
                }
            }
        }
        for (Variant curVar : beaconMatchedVariants) {
            //delete Variant, set Variantstatus, remove Matches
            VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.MATCHED_TO_BEACON.getMessage()).withMessageAddition(variantToBeaconName.get(curVar)).buildVWStatus();
            VWVariant buildVW = variantBuilder.withVariant(curVar).buildVW();
            String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
            User user = userDao.getUserByID(dataset.getUser().getId());
            VariantStatus variantstatus = variantStatusBuilder.withStatusVW(status, null, variantHash, dataset, user).buildSql();
            variantStatusDao.save(variantstatus);
//            List<Match> matches = beaconMatchedVariants.get(curVar);
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
    public List<DatasetHGMD> getSimilarHGMDVariants(Variant queryVariant, boolean geneBool, boolean pathwayBool, boolean familyBool) {
        return matchVariantDao.getSimilarHGMDVariants(queryVariant, geneBool, pathwayBool, familyBool);
    }

    @Transactional
    public List<DatasetHGMD> getIdenticalHGMDVariant(Variant variant) {
        return matchVariantDao.getIdenticalHGMDVariants(variant);
    }

    @Transactional
    public List<Variant> getIdenticalVariant(Variant variant) {
        List<Variant> identicalVariants = matchVariantDao.getIdenticalVariants(variant);
        logger.info("user: " + variant.getDataset().getUser().getMail());
        logger.info("datasetid: " + variant.getDataset().getId());
        return filterForNonFinishedDatasets(identicalVariants);
    }

    @Transactional
    public boolean removeMatch(Match match) {
        return matchVariantDao.remove(match);
    }

    @Transactional
    public List<Match> getMatchesByVariant(Variant variant) {
        return matchVariantDao.getVarWatchMatchesByVariantId(variant.getId());
    }

    @Transactional
    public List<DatasetHGMD> getSimilarHGMDByGene(Variant variant) {
        return matchVariantDao.getSimilarHGMDVariantsByGene(variant);
    }

    @Transactional
    public List<Variant> getSimilarVWByGene(Variant variant) {
        List<Variant> matchedVariants = matchVariantDao.getSimilarVWVariantsByGene(variant);
        return filterForNonFinishedDatasets(matchedVariants);
    }

    @Transactional
    public List<DatasetHGMD> getSimilarHGMDByFamily(Variant variant) {
        return matchVariantDao.getSimilarHGMDVariantsByFamily(variant);
    }

    @Transactional
    public List<Variant> getSimilarVWByFamily(Variant variant) {
        List<Variant> matchedVariants = matchVariantDao.getSimilarVWVariantsByFamily(variant);
        return filterForNonFinishedDatasets(matchedVariants);
//        return matchVariantDao.getSimilarVWVariantsByFamily(variant);
    }

//    public static void main(String[] args) {
//        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
//        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
//        MatchVariantDataManager vdm = inj.getInstance(MatchVariantDataManager.class);
//        Match match = vdm.get(13267l);
//        boolean bup = vdm.isDuplicatedMatch(match);
//        System.out.println(bup);
//        System.out.println("finish");
//    }

    private List<Variant> filterForNonFinishedDatasets(List<Variant> matchedVariants) {
        List<Variant> filteredVariants = new ArrayList<>();
        int notCompletedMatches = 0;
        Set<String> usersCompl = new HashSet<>();
        Set<String> usersNotCompl = new HashSet<>();
        for (Variant curVar : matchedVariants) {
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
    public List<MatchInformation> getNewMatchInformation(User user) {

//           List<VariantStatus> filteredVwVariants = filterVwMatchedVariants(vwmatchedVariantsSql, variant);
//        List<VariantStatus> filteredHGMDVariants = filterVwMatchedVariants(hgmdMatchedVariantsSql, variant);
//        List<MatchVariant> matches = matchVariantDao.getNotNotifiedMatchesByUser(user);
//        List<MatchInformation> matchInformations = new ArrayList<>();
//        for (MatchVariant match : matches) {
//            Long variantId = match.getVariantId();
//            Variant queryVariantSql = variantDao.get(variantId);
//            MatchInformation matchInfo = getMatchInformation(queryVariantSql, match.getMatch());
//            matchInformations.add(matchInfo);
//        }
//        return matchInformations;
        List<MatchVariant> matches = matchVariantDao.getNotNotifiedMatchesByUser(user);
//        Map<Long, List<MatchVariant>> variantToMatches = new HashMap<>();

        //collect all matches for a variant
//        for (MatchVariant match : matches) {
//            Long variantId = match.getVariantId();
//            if (variantToMatches.containsKey(variantId)) {
//                List<MatchVariant> curMatches = variantToMatches.get(variantId);
//                curMatches.add(match);
//                variantToMatches.put(variantId, curMatches);
//            } else {
//                List<MatchVariant> curMatches = new ArrayList<>();
//                curMatches.add(match);
//                variantToMatches.put(variantId, curMatches);
//            }
//        }
        //filter the matches for each variant
//        List<MatchVariant> filteredVariants = new ArrayList<>();
//        for (Entry<Long, List<MatchVariant>> mapEntry : variantToMatches.entrySet()) {
//            Long variantId = mapEntry.getKey();
//            List<MatchVariant> matchedVariants = mapEntry.getValue();
//            Variant variant = variantDao.get(variantId);
//
//            List<MatchVariant> curFilteredVariants = getFilteredList(matchedVariants, variant);
//            filteredVariants.addAll(curFilteredVariants);
//
//            //set notified = true to all non filteredMatches TODO maybe discard all matches in the screening worker?
//            Set<Long> filteredIds = new HashSet<>();
//            for (MatchVariant curMatch : curFilteredVariants) {
//                filteredIds.add(curMatch.getId());
//            }
//            Set<Long> nonFilteredIds = new HashSet<>();
//            for (MatchVariant curMatch : matchedVariants) {
//                if (!filteredIds.contains(curMatch.getId())) {
//                    nonFilteredIds.add(curMatch.getId());
//                }
//            }
//            for (Long curMatch : nonFilteredIds) {
//                MatchVariant curMatchSql = matchVariantDao.getMatchVariant(curMatch);
//                curMatchSql.setNotified(Boolean.TRUE);
//                matchVariantDao.update(curMatchSql);
//            }
//        }
        //convert to matchInfo
        List<MatchInformation> matchInformations = new ArrayList<>();
        for (MatchVariant match : matches) {
            Long variantId = match.getVariantId();
            Variant queryVariantSql = variantDao.get(variantId);
            MatchInformation matchInfo = getMatchInformation(queryVariantSql, match);
            if (matchInfo != null) {
                matchInformations.add(matchInfo);
            }
        }
        return matchInformations;
    }

    @Transactional
    public void setMatchAck(Long matchStatusID, User user) {
        MatchVariant varMatch = getVariantMatch(matchStatusID);
        logger.info("update match with id {}", varMatch.getId());
        varMatch.setNotified(Boolean.TRUE);
        matchVariantDao.update(varMatch);
    }

    @Transactional
    public List<MatchInformation> getMatchesByInterval(User user, DateTime lastReport, DateTime nextCreationDate) {
        List<MatchVariant> matches = matchVariantDao.getMatchesInInterval(user, lastReport, nextCreationDate);
        List<MatchInformation> matchInformations = new ArrayList<>();
        for (MatchVariant match : matches) {
            Long variantId = match.getVariantId();
            Variant queryVariantSql = variantDao.get(variantId);
            MatchInformation matchInfo = getMatchInformation(queryVariantSql, match);
            matchInformations.add(matchInfo);
        }
        return matchInformations;
    }

    private List<MatchVariant> filterMatchedVariants(List<MatchVariant> vwmatchedVariantsSql, Variant queryVariant) {
        List<MatchVariant> vwStatuses = getVWStatusEntries(vwmatchedVariantsSql);
        List<MatchVariant> hgmdStatuses = getHgmdStatusEntries(vwmatchedVariantsSql);

        List<MatchVariant> filteredStatusEntries = new ArrayList<>();
        filteredStatusEntries.addAll(getFilteredList(vwStatuses, queryVariant));
        filteredStatusEntries.addAll(getFilteredList(hgmdStatuses, queryVariant));
        return filteredStatusEntries;
    }

    private List<MatchVariant> getVWStatusEntries(List<MatchVariant> vwmatchedVariantsSql) {
        List<MatchVariant> vwVariantStatusEntries = new ArrayList<>();
        for (MatchVariant curStatus : vwmatchedVariantsSql) {
            if (curStatus.getDatabase().equals("VarWatch")) {
                vwVariantStatusEntries.add(curStatus);
            }
        }
        return vwVariantStatusEntries;
    }

    private List<MatchVariant> getHgmdStatusEntries(List<MatchVariant> vwmatchedVariantsSql) {
        List<MatchVariant> vwVariantStatusEntries = new ArrayList<>();
        for (MatchVariant curStatus : vwmatchedVariantsSql) {
            if (curStatus.getDatabase().equals("HGMD")) {
                vwVariantStatusEntries.add(curStatus);
            }
        }
        return vwVariantStatusEntries;
    }

    public List<MatchVariant> getFilteredList(List<MatchVariant> vwmatchedVariantsSql, Variant queryVariant) {
        if (vwmatchedVariantsSql.size() <= 5) {
            return vwmatchedVariantsSql;
        }

        //filter hpo
        List<MatchVariant> hpofilteredVariants = filterHpo(vwmatchedVariantsSql, 5);

        if (hpofilteredVariants.size() <= 5) {
            return hpofilteredVariants;
        }

        //filter chrom
        List<MatchVariant> chromFilteredVariants = filterChrom(queryVariant, hpofilteredVariants, 5);

        if (chromFilteredVariants.size() <= 5) {
            return chromFilteredVariants;
        }

        //filter position
        List<MatchVariant> posFilteredVariants = filterPos(queryVariant, chromFilteredVariants, 5);

        return posFilteredVariants;
    }

    private List<MatchVariant> filterHpo(List<MatchVariant> matchedVariantsSql, int i) {
        Map<Double, List<MatchVariant>> variantToHPO = new HashMap<>();
        Set<Double> hpoValues = new HashSet<>();
        for (MatchVariant curStatusl : matchedVariantsSql) {
            Double hpo = curStatusl.getMatch().getHpoDist();
            if (variantToHPO.containsKey(hpo)) {
                variantToHPO.get(hpo).add(curStatusl);
            } else {
                List<MatchVariant> variantStatusList = new ArrayList<>();
                variantStatusList.add(curStatusl);
                variantToHPO.put(hpo, variantStatusList);
            }
            hpoValues.add(hpo);
        }

        List<Double> hpoValueList = new ArrayList<>();
        hpoValueList.addAll(hpoValues);
        Collections.sort(hpoValueList, Collections.reverseOrder());
        boolean variantStatusBoxFull = false;
        List<MatchVariant> filteredVariants = new ArrayList<>();
        for (Double currentHPO : hpoValueList) {
            List<MatchVariant> currentStatusList = variantToHPO.get(currentHPO);
            for (MatchVariant curStatusl : currentStatusList) {
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

    private List<MatchVariant> filterChrom(Variant queryVariant, List<MatchVariant> matchedVariantsSql, int i) {
        List<MatchVariant> filteredVariants = new ArrayList<>();
        for (MatchVariant status : matchedVariantsSql) {
            Set<MatchVariant> matches = status.getMatch().getVariants();
            MatchVariant matchVariant = getTargetVariant(queryVariant, matches);
            if (matchVariant.getDatabase().getName().equals("HGMD")) {
                DatasetHGMD ds = datasetDao.getHGMDDataset(matchVariant.getVariantId());
                if (ds.getChrName().equals(queryVariant.getChromosomeName())) {
                    filteredVariants.add(status);
                }
            } else if (matchVariant.getDatabase().getName().equals("VarWatch")) {
                Variant var = variantDao.get(matchVariant.getVariantId());
                if (var.getChromosomeName().equals(queryVariant.getChromosomeName())) {
                    filteredVariants.add(status);
                }
            }
        }
        return filteredVariants;
    }

    private List<MatchVariant> filterPos(Variant queryVariant, List<MatchVariant> matchedVariantsSql, int i) {
        Map<Integer, List<MatchVariant>> distance2Variant = new HashMap<>();
        for (MatchVariant status : matchedVariantsSql) {
            Set<MatchVariant> matches = status.getMatch().getVariants();
            MatchVariant matchVariant = getTargetVariant(queryVariant, matches);
            Integer posisiton = null;
            if (matchVariant.getDatabase().getName().equals("HGMD")) {
                DatasetHGMD ds = datasetDao.getHGMDDataset(matchVariant.getVariantId());
                posisiton = ds.getChrPos();
            } else if (matchVariant.getDatabase().getName().equals("VarWatch")) {
                Variant var = variantDao.get(matchVariant.getVariantId());
                posisiton = var.getChromosomePos();
            }
            Integer distance = Math.abs(queryVariant.getChromosomePos() - posisiton);
            if (distance2Variant.containsKey(posisiton)) {
                distance2Variant.get(posisiton).add(status);
            } else {
                List<MatchVariant> variantStatusList = new ArrayList<>();
                variantStatusList.add(status);
                distance2Variant.put(distance, variantStatusList);
            }
        }

        List<Integer> positions = new ArrayList<>();
        positions.addAll(distance2Variant.keySet());
        Collections.sort(positions);
        boolean variantStatusBoxFull = false;
        List<MatchVariant> filteredVariants = new ArrayList<>();
        for (Integer currentPos : positions) {
            List<MatchVariant> currentStatusList = distance2Variant.get(currentPos);
            for (MatchVariant curStatusl : currentStatusList) {
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
    public MatchVariant getVariantMatch(Long dataId) {
        return matchVariantDao.getMatchVariant(dataId);
    }

    @Transactional
    public Match get(Long matchId) {
        return matchVariantDao.getMatchById(matchId);
    }

    @Transactional
    public void setMatchesAck(User user) {
        List<MatchVariant> matches = matchVariantDao.getNotNotifiedMatchesByUser(user);
        for (MatchVariant curMatch : matches) {
            logger.info("update match with id {}", curMatch.getId());
            curMatch.setNotified(Boolean.TRUE);
            matchVariantDao.update(curMatch);
        }
    }

    public List<Variant> getVariantsWithSameAminoAcidExchange(Variant variant) {
        List<Variant> identicalVariants = matchVariantDao.getVariantsWithSameAminoAcidExchange(variant);
        logger.info("user: " + variant.getDataset().getUser().getMail());
        logger.info("datasetid: " + variant.getDataset().getId());
        return filterForNonFinishedDatasets(identicalVariants);
    }

    public List<DatasetHGMD> getHgmdVariantsWithSameAminoAcidExchange(Variant variant) {
        List<DatasetHGMD> identicalVariants = matchVariantDao.getHgmdVariantsWithSameAminoAcidExchange(variant);
        return identicalVariants;
    }

    public boolean isDuplicatedMatch(Match curMatch) {
        return matchVariantDao.isDuplicatedMatch(curMatch);
    }

}
