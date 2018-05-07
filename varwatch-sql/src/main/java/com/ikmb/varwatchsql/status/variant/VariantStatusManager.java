/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.status.variant;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.ikmb.varwatchcommons.entities.VWStatus;
import com.ikmb.varwatchcommons.entities.VWVariant;
import com.ikmb.varwatchcommons.entities.RejectedVariant;
import com.ikmb.varwatchcommons.entities.Status;
import com.ikmb.varwatchcommons.utils.VariantHash;
import com.ikmb.varwatchsql.guice.VarWatchInjector;
import com.ikmb.varwatchsql.guice.VarWatchPersist;
import com.ikmb.varwatchsql.auth.user.UserDao;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.entities.DatasetHGMDSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.matching.MatchSQL;
import com.ikmb.varwatchsql.entities.VariantStatusSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetDao;
import com.ikmb.varwatchsql.variant_data.variant.VariantBuilder;
import com.ikmb.varwatchsql.variant_data.variant.VariantDao;
import com.ikmb.varwatchsql.data.hpo.HPOTermBuilder;
import com.ikmb.varwatchsql.matching.MatchVariantDao;
import com.ikmb.varwatchsql.matching.MatchVariantDataManager;
import com.ikmb.varwatchsql.matching.MatchVariantSQL;
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
    public VariantStatusSQL get(Long statusID) {
        VariantStatusSQL status = variantStatusDao.getStatusByID(statusID);
        return status;
    }

    @Transactional(rollbackOn = PersistenceException.class)
    public void save(DatasetVWSQL dataset, String rawVariant, VWStatus status) {
//        UserSQL user = userDao.getUserByID(dataset.getUser().getId());
//
//        dataset = datasetDao.getDataset(dataset.getId());
//
//        VariantStatusSQL variantstatus = variantStatusBuilder.withStatusVW(status, rawVariant, "NA", dataset, user).buildSql();
//        variantStatusDao.save(variantstatus);
        save(dataset, rawVariant, "NA", status);
    }

    @Transactional(rollbackOn = PersistenceException.class)
    public void save(DatasetVWSQL dataset, String rawVariant, String variantHash, VWStatus status) {
        UserSQL user = userDao.getUserByID(dataset.getUser().getId());
        VariantStatusSQL variantstatus = variantStatusBuilder.withStatusVW(status, rawVariant, variantHash, dataset, user).buildSql();
        variantStatusDao.save(variantstatus);
    }

    public void save2(DatasetVWSQL dataset, String rawVariant, String variantHash, VWStatus status) {
        UserSQL user = userDao.getUserByID(dataset.getUser().getId());
        VariantStatusSQL variantstatus = variantStatusBuilder.withStatusVW(status, rawVariant, variantHash, dataset, user).buildSql();
        variantStatusDao.save(variantstatus);
    }

    public void save(DatasetVWSQL dataset, String rawVariant, String variantHash, VariantSQL variantSql,
            VWStatus status) {
        UserSQL user = userDao.getUserByID(dataset.getUser().getId());

        dataset = datasetDao.getDataset(dataset.getId());

        VariantStatusSQL variantstatus = variantStatusBuilder.withStatusVW(status, rawVariant, variantHash, dataset, user).buildSql();
        variantstatus.setVariant(variantSql);
        variantStatusDao.save(variantstatus);
    }

    @Transactional
    public Status getLastStatus(Long variantId) {
        VariantSQL variant = variantDao.get(variantId);

        List<VariantStatusSQL> statusVariants = variantStatusDao.get(variant);
        VariantStatusSQL lastStatus = null;
        for (VariantStatusSQL currentStatus : statusVariants) {
            if (lastStatus == null || lastStatus.getTimestamp().isBefore(currentStatus.getTimestamp())) {
                lastStatus = currentStatus;
            }
        }
        return variantStatusBuilder.withStatusSQL(lastStatus).build();
    }

    @Transactional
    public List<RejectedVariant> getRejectedVariants(Long datasetId) {
        DatasetVWSQL dataset = datasetDao.getDataset(datasetId);
        List<VariantStatusSQL> variantStatus = variantStatusDao.getStatusFromDataset(dataset);

        List<RejectedVariant> rejectedVariants = new ArrayList<>();
        for (VariantStatusSQL currentStatus : variantStatus) {
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
        List<VariantStatusSQL> matchedVariantsSql = variantStatusDao.getMatchedStatus(id);
        //are there matches? --> yes then sort the matches, not then dont
//        boolean matches = false;
//        for (VariantStatusSQL curStatusl : statusVariantsSql) {
//            String status = curStatusl.getStatus();
//            if (status.equals("MATCHED")) {
//                matches = true;
//                break;
//            }
//        }

        List<Status> statusVariantsVarWatch = new ArrayList<>();
        List<Status> statusVariantsHGMD = new ArrayList<>();
        if (!matchedVariantsSql.isEmpty()) {
            Map<Double, List<VariantStatusSQL>> variantToHPO = new HashMap<>();
            Set<Double> hpoValues = new HashSet<>();
            for (VariantStatusSQL curStatusl : matchedVariantsSql) {
                Double hpo = curStatusl.getMatchVariant().getMatch().getHpoDist();
                if (variantToHPO.containsKey(hpo)) {
                    variantToHPO.get(hpo).add(curStatusl);
                } else {
                    List<VariantStatusSQL> variantStatusList = new ArrayList<>();
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
                List<VariantStatusSQL> currentStatusList = variantToHPO.get(currentHPO);
                for (VariantStatusSQL curStatusl : currentStatusList) {
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
            List<VariantStatusSQL> statusVariantsSql = variantStatusDao.getNonMatchedStatus(id);
            for (VariantStatusSQL curStatusl : statusVariantsSql) {
                Status build = variantStatusBuilder.withStatusSQL(curStatusl).build();
                statusVariantsVarWatch.add(build);
            }
        }

        return statusVariantsVarWatch;
    }

    @Transactional
    public List<Status> getTmpAllStatus(Long id) {
        VariantSQL variant = variantDao.get(id);
        List<VariantStatusSQL> vwmatchedVariantsSql = variantStatusDao.getVwMatchedStatus(id);
        List<VariantStatusSQL> hgmdMatchedVariantsSql = variantStatusDao.getHgmdMatchedStatus(id);
        List<VariantStatusSQL> beaconMatchedVariantsSql = variantStatusDao.getBeaconMatchedStatus(id);

        if (vwmatchedVariantsSql.isEmpty() && hgmdMatchedVariantsSql.isEmpty()) {
            return getAllNonMatchingStatus(id);
        }

        //here are matches in vw or hgmd
//        List<VariantStatusSQL> filteredVwVariants = filterVwMatchedVariants(vwmatchedVariantsSql, variant);
//        List<VariantStatusSQL> filteredHGMDVariants = filterVwMatchedVariants(hgmdMatchedVariantsSql, variant);
        List<Status> statusVariantsSql = new ArrayList<>();
        for (VariantStatusSQL curStatusl : vwmatchedVariantsSql) {
            Status build = variantStatusBuilder.withStatusSQL(curStatusl).build();
            statusVariantsSql.add(build);
        }
        for (VariantStatusSQL curStatusl : hgmdMatchedVariantsSql) {
            Status build = variantStatusBuilder.withStatusSQL(curStatusl).build();
            statusVariantsSql.add(build);
        }
        for (VariantStatusSQL curStatusl : beaconMatchedVariantsSql) {
            Status build = variantStatusBuilder.withStatusSQL(curStatusl).build();
            statusVariantsSql.add(build);
        }
        return statusVariantsSql;
    }

    @Transactional
    public void persistMatchStatus(List<MatchSQL> matches, String database) {
        for (MatchSQL currentMatch : matches) {
            for (MatchVariantSQL curVariant : currentMatch.getVariants()) {
                if (!curVariant.getDatabase().getName().equals("VarWatch")) {
                    continue;
                }
                VariantSQL variant = variantDao.get(curVariant.getVariantId());
                DatasetVWSQL dssql = datasetDao.getDataset(variant.getDataset().getId());
                MatchSQL matchsql = matchVariantDao.get(currentMatch.getId());

                VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatus.MATCHED).withVariantMatch(curVariant).withMessage("Variant matched to " + database).buildVWStatus();
                VWVariant buildVW = variantBuilder.withSQLVariant(variant).buildVW();
                String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dssql.getPhenotypes()).buildStringSet());

                save(dssql, null, variantHash, variant, status);
            }
        }
    }

    private void persistExternal(MatchSQL currentMatch) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void persistInternal(MatchSQL currentMatch) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void main(String[] args) {
        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
        VariantStatusManager vdm = inj.getInstance(VariantStatusManager.class);
        List<Status> variants = vdm.getTmpAllStatus(1136l);
        System.out.println("finish");
    }

    private List<Status> getAllNonMatchingStatus(Long id) {
        List<Status> allVariantStatus = new ArrayList<>();
        List<VariantStatusSQL> statusVariantsSql = variantStatusDao.getNonMatchedStatus(id);
        for (VariantStatusSQL curStatusl : statusVariantsSql) {
            Status build = variantStatusBuilder.withStatusSQL(curStatusl).build();
            allVariantStatus.add(build);
        }
        return allVariantStatus;
    }

    public List<VariantStatusSQL> filterVwMatchedVariants(List<VariantStatusSQL> vwmatchedVariantsSql, VariantSQL queryVariant) {
        if (vwmatchedVariantsSql.size() <= 5) {
            return vwmatchedVariantsSql;
        }

        //filter hpo
        List<VariantStatusSQL> hpofilteredVariants = filterHpo(vwmatchedVariantsSql, 5);

        if (hpofilteredVariants.size() <= 5) {
            return hpofilteredVariants;
        }

        //filter chrom
        List<VariantStatusSQL> chromFilteredVariants = filterChrom(queryVariant, hpofilteredVariants, 5);

        if (chromFilteredVariants.size() <= 5) {
            return chromFilteredVariants;
        }

        //filter position
        List<VariantStatusSQL> posFilteredVariants = filterPos(queryVariant, chromFilteredVariants, 5);

        return posFilteredVariants;
    }

    private List<VariantStatusSQL> filterHpo(List<VariantStatusSQL> matchedVariantsSql, int i) {
        Map<Double, List<VariantStatusSQL>> variantToHPO = new HashMap<>();
        Set<Double> hpoValues = new HashSet<>();
        for (VariantStatusSQL curStatusl : matchedVariantsSql) {
            Double hpo = curStatusl.getMatchVariant().getMatch().getHpoDist();
            if (variantToHPO.containsKey(hpo)) {
                variantToHPO.get(hpo).add(curStatusl);
            } else {
                List<VariantStatusSQL> variantStatusList = new ArrayList<>();
                variantStatusList.add(curStatusl);
                variantToHPO.put(hpo, variantStatusList);
            }
            hpoValues.add(hpo);
        }

        List<Double> hpoValueList = new ArrayList<>();
        hpoValueList.addAll(hpoValues);
        Collections.sort(hpoValueList, Collections.reverseOrder());
        boolean variantStatusBoxFull = false;
        List<VariantStatusSQL> filteredVariants = new ArrayList<>();
        for (Double currentHPO : hpoValueList) {
            List<VariantStatusSQL> currentStatusList = variantToHPO.get(currentHPO);
            for (VariantStatusSQL curStatusl : currentStatusList) {
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

    private List<VariantStatusSQL> filterChrom(VariantSQL queryVariant, List<VariantStatusSQL> matchedVariantsSql, int i) {
        List<VariantStatusSQL> filteredVariants = new ArrayList<>();
        for (VariantStatusSQL status : matchedVariantsSql) {
            Set<MatchVariantSQL> matches = status.getMatchVariant().getMatch().getVariants();
            MatchVariantSQL matchVariant = matchVariantDm.getTargetVariant(queryVariant, matches);
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

    private List<VariantStatusSQL> filterPos(VariantSQL queryVariant, List<VariantStatusSQL> matchedVariantsSql, int i) {
        Map<Integer, List<VariantStatusSQL>> distance2Variant = new HashMap<>();
        for (VariantStatusSQL status : matchedVariantsSql) {
            Set<MatchVariantSQL> matches = status.getMatchVariant().getMatch().getVariants();
            MatchVariantSQL matchVariant = matchVariantDm.getTargetVariant(queryVariant, matches);
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
                List<VariantStatusSQL> variantStatusList = new ArrayList<>();
                variantStatusList.add(status);
                distance2Variant.put(distance, variantStatusList);
            }
        }

        List<Integer> positions = new ArrayList<>();
        positions.addAll(distance2Variant.keySet());
        Collections.sort(positions);
        boolean variantStatusBoxFull = false;
        List<VariantStatusSQL> filteredVariants = new ArrayList<>();
        for (Integer currentPos : positions) {
            List<VariantStatusSQL> currentStatusList = distance2Variant.get(currentPos);
            for (VariantStatusSQL curStatusl : currentStatusList) {
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
