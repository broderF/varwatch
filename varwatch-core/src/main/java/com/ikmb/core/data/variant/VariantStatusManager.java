/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.variant;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.auth.user.UserDao;
import com.ikmb.core.data.dataset.DatasetDao;
import com.ikmb.core.data.dataset.DatasetHGMD;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.varwatchcommons.entities.VWStatus;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import com.ikmb.core.varwatchcommons.entities.RejectedVariant;
import com.ikmb.core.varwatchcommons.entities.Status;
import com.ikmb.core.utils.VariantHash;
import com.ikmb.core.data.hpo.HPOTermBuilder;
import com.ikmb.core.data.matching.Match;
import com.ikmb.core.data.matching.MatchVariant;
import com.ikmb.core.data.matching.MatchVariantDao;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.PersistenceException;

/**
 *
 * @author broder
 */
public class VariantStatusManager {

    @Inject
    private VariantStatusDao variantStatusDao;

    @Inject
    private UserDao userDao;

    @Inject
    private DatasetDao datasetDao;

    @Inject
    private VariantDao variantDao;

    @Inject
    private MatchVariantDao matchVariantDao;

    @Inject
    private VariantHash variantHasher;

    @Inject
    private VariantBuilder variantBuilder;

    @Inject
    private HPOTermBuilder hpoTermBuilder;

    @Inject
    private VariantStatusBuilder variantStatusBuilder;

    @Inject
    private MatchVariantDataManager matchVariantDm;

    @Transactional(rollbackOn = PersistenceException.class)
    public VariantStatus get(Long statusID) {
        VariantStatus status = variantStatusDao.getStatusByID(statusID);
        return status;
    }

    @Transactional(rollbackOn = PersistenceException.class)
    public void save(DatasetVW dataset, String rawVariant, VWStatus status) {
//        User user = userDao.getUserByID(dataset.getUser().getId());
//
//        dataset = datasetDao.getDataset(dataset.getId());
//
//        VariantStatus variantstatus = variantStatusBuilder.withStatusVW(status, rawVariant, "NA", dataset, user).buildSql();
//        variantStatusDao.save(variantstatus);
        save(dataset, rawVariant, "NA", status);
    }

    @Transactional(rollbackOn = PersistenceException.class)
    public void save(DatasetVW dataset, String rawVariant, String variantHash, VWStatus status) {
        User user = userDao.getUserByID(dataset.getUser().getId());
        VariantStatus variantstatus = variantStatusBuilder.withStatusVW(status, rawVariant, variantHash, dataset, user).buildSql();
        variantStatusDao.save(variantstatus);
    }

    public void save2(DatasetVW dataset, String rawVariant, String variantHash, VWStatus status) {
        User user = userDao.getUserByID(dataset.getUser().getId());
        VariantStatus variantstatus = variantStatusBuilder.withStatusVW(status, rawVariant, variantHash, dataset, user).buildSql();
        variantStatusDao.save(variantstatus);
    }

    public void save(DatasetVW dataset, String rawVariant, String variantHash, Variant variantSql,
            VWStatus status) {
        User user = userDao.getUserByID(dataset.getUser().getId());

        dataset = datasetDao.getDataset(dataset.getId());

        VariantStatus variantstatus = variantStatusBuilder.withStatusVW(status, rawVariant, variantHash, dataset, user).buildSql();
        variantstatus.setVariant(variantSql);
        variantStatusDao.save(variantstatus);
    }

    @Transactional
    public Status getLastStatus(Long variantId) {
        Variant variant = variantDao.get(variantId);

        List<VariantStatus> statusVariants = variantStatusDao.get(variant);
        VariantStatus lastStatus = null;
        for (VariantStatus currentStatus : statusVariants) {
            if (lastStatus == null || lastStatus.getTimestamp().isBefore(currentStatus.getTimestamp())) {
                lastStatus = currentStatus;
            }
        }
        if (lastStatus != null) {

            return variantStatusBuilder.withStatusSQL(lastStatus).build();
        } else {
            return null;
        }
    }

    @Transactional
    public List<RejectedVariant> getRejectedVariants(Long datasetId) {
        DatasetVW dataset = datasetDao.getDataset(datasetId);
        List<VariantStatus> variantStatus = variantStatusDao.getStatusFromDataset(dataset);

        List<RejectedVariant> rejectedVariants = new ArrayList<>();
        for (VariantStatus currentStatus : variantStatus) {
            if (currentStatus.getVariant() == null) {
                RejectedVariant rejVariant = new RejectedVariant();
                Status status = variantStatusBuilder.withStatusSQL(currentStatus).build();
                String identifier = null;
                if (currentStatus.getVariantHash() != null && !currentStatus.getVariantHash().equals("NA")) {
                    identifier = currentStatus.getVariantHash();
                } else if (currentStatus.getVariantRaw() != null) {
                    identifier = currentStatus.getVariantRaw();
                }
                rejVariant.setIdentifier(identifier);
                rejVariant.setStatus(status);
                rejectedVariants.add(rejVariant);
            }
        }
        return rejectedVariants;
    }

    @Transactional
    public List<Status> getAllStatus(Long id) {
        List<VariantStatus> matchedVariantsSql = variantStatusDao.getMatchedStatus(id);
        //are there matches? --> yes then sort the matches, not then dont
//        boolean matches = false;
//        for (VariantStatus curStatusl : statusVariantsSql) {
//            String status = curStatusl.getStatus();
//            if (status.equals("MATCHED")) {
//                matches = true;
//                break;
//            }
//        }

        List<Status> statusVariantsVarWatch = new ArrayList<>();
        List<Status> statusVariantsHGMD = new ArrayList<>();
        if (!matchedVariantsSql.isEmpty()) {
            Map<Double, List<VariantStatus>> variantToHPO = new HashMap<>();
            Set<Double> hpoValues = new HashSet<>();
            for (VariantStatus curStatusl : matchedVariantsSql) {
                Double hpo = curStatusl.getMatchVariant().getMatch().getHpoDist();
                if (variantToHPO.containsKey(hpo)) {
                    variantToHPO.get(hpo).add(curStatusl);
                } else {
                    List<VariantStatus> variantStatusList = new ArrayList<>();
                    variantStatusList.add(curStatusl);
                    variantToHPO.put(hpo, variantStatusList);
                }
                hpoValues.add(hpo);
            }

            List<Double> hpoValueList = new ArrayList<>();
            hpoValueList.addAll(hpoValues);
            Collections.sort(hpoValueList, Collections.reverseOrder());
            boolean vwStatusBoxFull = false;
            boolean hgmdStatusBoxFull = false;
            for (Double currentHPO : hpoValueList) {
                List<VariantStatus> currentStatusList = variantToHPO.get(currentHPO);
                for (VariantStatus curStatusl : currentStatusList) {
                    Status build = variantStatusBuilder.withStatusSQL(curStatusl).build();
                    if (build.getDescription().equals("Variant matched to VarWatch")) {
                        if (!vwStatusBoxFull) {
                            statusVariantsVarWatch.add(build);
                        }
                    } else if (build.getDescription().equals("Variant matched to HGMD")) {
                        if (!hgmdStatusBoxFull) {
                            statusVariantsHGMD.add(build);
                        }
                    }
                }
                if (statusVariantsVarWatch.size() > 5 && statusVariantsHGMD.size() > 5) {
                    break;
                } else if (statusVariantsVarWatch.size() > 5) {
                    vwStatusBoxFull = true;
                } else if (statusVariantsHGMD.size() > 5) {
                    hgmdStatusBoxFull = true;
                }
            }
            statusVariantsVarWatch.addAll(statusVariantsHGMD);
        } else {
            List<VariantStatus> statusVariantsSql = variantStatusDao.getNonMatchedStatus(id);
            for (VariantStatus curStatusl : statusVariantsSql) {
                Status build = variantStatusBuilder.withStatusSQL(curStatusl).build();
                statusVariantsVarWatch.add(build);
            }
        }

        return statusVariantsVarWatch;
    }

    @Transactional
    public List<Status> getTmpAllStatus(Long id) {
        Variant variant = variantDao.get(id);
        List<VariantStatus> vwmatchedVariantsSql = variantStatusDao.getVwMatchedStatus(id);
        List<VariantStatus> hgmdMatchedVariantsSql = variantStatusDao.getHgmdMatchedStatus(id);
        List<VariantStatus> beaconMatchedVariantsSql = variantStatusDao.getBeaconMatchedStatus(id);

        if (vwmatchedVariantsSql.isEmpty() && hgmdMatchedVariantsSql.isEmpty() && beaconMatchedVariantsSql.isEmpty()) {
            return getAllNonMatchingStatus(id);
        }

        //here are matches in vw or hgmd
//        List<VariantStatus> filteredVwVariants = filterVwMatchedVariants(vwmatchedVariantsSql, variant);
//        List<VariantStatus> filteredHGMDVariants = filterVwMatchedVariants(hgmdMatchedVariantsSql, variant);
        List<Status> statusVariantsSql = new ArrayList<>();
        for (VariantStatus curStatusl : vwmatchedVariantsSql) {
            Status build = variantStatusBuilder.withStatusSQL(curStatusl).build();
            statusVariantsSql.add(build);
        }
        for (VariantStatus curStatusl : hgmdMatchedVariantsSql) {
            Status build = variantStatusBuilder.withStatusSQL(curStatusl).build();
            statusVariantsSql.add(build);
        }
        for (VariantStatus curStatusl : beaconMatchedVariantsSql) {
            Status build = variantStatusBuilder.withStatusSQL(curStatusl).build();
            statusVariantsSql.add(build);
        }
        return statusVariantsSql;
    }

    @Transactional
    public void persistMatchStatus(List<Match> matches, String database) {
        for (Match currentMatch : matches) {
            for (MatchVariant curVariant : currentMatch.getVariants()) {
                if (!curVariant.getDatabase().getName().equals("VarWatch")) {
                    continue;
                }
                Variant variant = variantDao.get(curVariant.getVariantId());
                DatasetVW dssql = datasetDao.getDataset(variant.getDataset().getId());
                Match matchsql = matchVariantDao.get(currentMatch.getId());

                VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.MATCHED).withVariantMatch(curVariant).withMessage("Variant matched to " + database).buildVWStatus();
                VWVariant buildVW = variantBuilder.withVariant(variant).buildVW();
                String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dssql.getPhenotypes()).buildStringSet());

                save(dssql, null, variantHash, variant, status);
            }
        }
    }

    private void persistExternal(Match currentMatch) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void persistInternal(Match currentMatch) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    public static void main(String[] args) {
//        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
//        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
//        VariantStatusManager vdm = inj.getInstance(VariantStatusManager.class);
//        List<Status> variants = vdm.getTmpAllStatus(1136l);
//        System.out.println("finish");
//    }
    private List<Status> getAllNonMatchingStatus(Long id) {
        List<Status> allVariantStatus = new ArrayList<>();
        List<VariantStatus> statusVariantsSql = variantStatusDao.getNonMatchedStatus(id);
        for (VariantStatus curStatusl : statusVariantsSql) {
            Status build = variantStatusBuilder.withStatusSQL(curStatusl).build();
            allVariantStatus.add(build);
        }
        return allVariantStatus;
    }

    public List<VariantStatus> filterVwMatchedVariants(List<VariantStatus> vwmatchedVariantsSql, Variant queryVariant) {
        if (vwmatchedVariantsSql.size() <= 5) {
            return vwmatchedVariantsSql;
        }

        //filter hpo
        List<VariantStatus> hpofilteredVariants = filterHpo(vwmatchedVariantsSql, 5);

        if (hpofilteredVariants.size() <= 5) {
            return hpofilteredVariants;
        }

        //filter chrom
        List<VariantStatus> chromFilteredVariants = filterChrom(queryVariant, hpofilteredVariants, 5);

        if (chromFilteredVariants.size() <= 5) {
            return chromFilteredVariants;
        }

        //filter position
        List<VariantStatus> posFilteredVariants = filterPos(queryVariant, chromFilteredVariants, 5);

        return posFilteredVariants;
    }

    private List<VariantStatus> filterHpo(List<VariantStatus> matchedVariantsSql, int i) {
        Map<Double, List<VariantStatus>> variantToHPO = new HashMap<>();
        Set<Double> hpoValues = new HashSet<>();
        for (VariantStatus curStatusl : matchedVariantsSql) {
            Double hpo = curStatusl.getMatchVariant().getMatch().getHpoDist();
            if (variantToHPO.containsKey(hpo)) {
                variantToHPO.get(hpo).add(curStatusl);
            } else {
                List<VariantStatus> variantStatusList = new ArrayList<>();
                variantStatusList.add(curStatusl);
                variantToHPO.put(hpo, variantStatusList);
            }
            hpoValues.add(hpo);
        }

        List<Double> hpoValueList = new ArrayList<>();
        hpoValueList.addAll(hpoValues);
        Collections.sort(hpoValueList, Collections.reverseOrder());
        boolean variantStatusBoxFull = false;
        List<VariantStatus> filteredVariants = new ArrayList<>();
        for (Double currentHPO : hpoValueList) {
            List<VariantStatus> currentStatusList = variantToHPO.get(currentHPO);
            for (VariantStatus curStatusl : currentStatusList) {
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

    private List<VariantStatus> filterChrom(Variant queryVariant, List<VariantStatus> matchedVariantsSql, int i) {
        List<VariantStatus> filteredVariants = new ArrayList<>();
        for (VariantStatus status : matchedVariantsSql) {
            Set<MatchVariant> matches = status.getMatchVariant().getMatch().getVariants();
            MatchVariant matchVariant = matchVariantDm.getTargetVariant(queryVariant, matches);
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

    private List<VariantStatus> filterPos(Variant queryVariant, List<VariantStatus> matchedVariantsSql, int i) {
        Map<Integer, List<VariantStatus>> distance2Variant = new HashMap<>();
        for (VariantStatus status : matchedVariantsSql) {
            Set<MatchVariant> matches = status.getMatchVariant().getMatch().getVariants();
            MatchVariant matchVariant = matchVariantDm.getTargetVariant(queryVariant, matches);
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
                List<VariantStatus> variantStatusList = new ArrayList<>();
                variantStatusList.add(status);
                distance2Variant.put(distance, variantStatusList);
            }
        }

        List<Integer> positions = new ArrayList<>();
        positions.addAll(distance2Variant.keySet());
        Collections.sort(positions);
        boolean variantStatusBoxFull = false;
        List<VariantStatus> filteredVariants = new ArrayList<>();
        for (Integer currentPos : positions) {
            List<VariantStatus> currentStatusList = distance2Variant.get(currentPos);
            for (VariantStatus curStatusl : currentStatusList) {
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

}
