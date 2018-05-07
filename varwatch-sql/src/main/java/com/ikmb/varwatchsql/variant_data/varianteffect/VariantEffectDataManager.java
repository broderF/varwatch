/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.variant_data.varianteffect;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.varwatchcommons.entities.VWStatus;
import com.ikmb.varwatchcommons.entities.VWVariant;
import com.ikmb.varwatchcommons.utils.VariantHash;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.data.transcript.TranscriptSQL;
import com.ikmb.varwatchsql.entities.VariantEffectSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetDao;
import com.ikmb.varwatchsql.variant_data.variant.VariantBuilder;
import com.ikmb.varwatchsql.variant_data.variant.VariantDao;
import com.ikmb.varwatchsql.data.hpo.HPOTermBuilder;
import com.ikmb.varwatchsql.data.transcript.TranscriptDao;
import com.ikmb.varwatchsql.status.variant.VariantStatusBuilder;
import com.ikmb.varwatchsql.status.variant.VariantStatusManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class VariantEffectDataManager {

    @Inject
    private DatasetDao datasetDao;

    @Inject
    private VariantDao variantDao;

    @Inject
    private VariantStatusManager variantStatusManager;

    @Inject
    private VariantStatusBuilder variantStatusBuilder;

    @Inject
    private HPOTermBuilder hpoTermBuilder;

    @Inject
    private VariantHash variantHasher;

    @Inject
    private VariantBuilder variantBuilder;

    @Inject
    private TranscriptDao transcriptDao;

    @Inject
    private VariantEffectDao variantEffectDao;

    private final Logger logger = LoggerFactory.getLogger(VariantEffectDataManager.class);

    @Transactional(rollbackOn = {PersistenceException.class, ConstraintViolationException.class})
    public List<VariantEffectSQL> persistSQLVariantEffects(List<VariantEffectSQL> variantEffects, DatasetVWSQL dataset) {
        List<VariantEffectSQL> variantEffectsSQL = new ArrayList<VariantEffectSQL>();
        dataset = datasetDao.getDataset(dataset.getId());

        List<VariantSQL> variants = variantDao.getVariantsByDataset(dataset);

        Map<VariantSQL, List<VariantEffectSQL>> variantMap = mapEffectsToVariant(variants, variantEffects);

        for (VariantSQL curVar : variantMap.keySet()) {
            List<VariantEffectSQL> curVariantEffects = variantMap.get(curVar);
            if (curVariantEffects.isEmpty()) {
                VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatus.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.IMPACT_TOO_LOW.getMessage()).buildVWStatus();
                VWVariant buildVW = variantBuilder.withSQLVariant(curVar).buildVW();
                String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
                variantStatusManager.save2(dataset, null, variantHash, status);
                variantDao.remove(curVar);

            } else if (!foundTranscripts(curVariantEffects)) {
                for (VariantEffectSQL variantEffect : curVariantEffects) {
                    TranscriptSQL transcriptSQL = transcriptDao.getByName(variantEffect.getTranscriptName());
                    if (transcriptSQL == null) {
                        logger.info(" no transcript found for: {}", variantEffect.getTranscriptName());
                        VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatus.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.NO_TRANSCRIPT_FOUND.getMessage()).withMessageAddition(variantEffect.getTranscriptName()).buildVWStatus();
                        VWVariant buildVW = variantBuilder.withSQLVariant(curVar).buildVW();
                        String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
                        variantStatusManager.save2(dataset, null, variantHash, status);
                        continue;
                    }
                }
                variantDao.remove(curVar);
            } else {
                for (VariantEffectSQL variantEffect : curVariantEffects) {
                    TranscriptSQL transcriptSQL = transcriptDao.getByName(variantEffect.getTranscriptName());
                    if (transcriptSQL == null) {
                        logger.info(" no transcript found for: {}", variantEffect.getTranscriptName());
                        VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatus.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.NO_TRANSCRIPT_FOUND.getMessage()).withMessageAddition(variantEffect.getTranscriptName()).buildVWStatus();
                        VWVariant buildVW = variantBuilder.withSQLVariant(curVar).buildVW();
                        String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
                        variantStatusManager.save(dataset, null, variantHash, curVar, status);
                        continue;
                    }

                    VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatus.STORED).withMessage(VariantStatusBuilder.VariantStatusMessage.STORED_IN_VARWATCH.getMessage()).buildVWStatus();
                    VWVariant buildVW = variantBuilder.withSQLVariant(curVar).buildVW();
                    String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
                    variantStatusManager.save(dataset, null, variantHash, curVar, status);

                    variantEffect.setVariant(curVar);
                    variantEffect.setTranscript(transcriptSQL);
                    variantEffectDao.persist(variantEffect);
                    variantEffectsSQL.add(variantEffect);
                }
            }
        }
        return variantEffectsSQL;
    }

    private Map<VariantSQL, List<VariantEffectSQL>> mapEffectsToVariant(List<VariantSQL> variants, List<VariantEffectSQL> variantEffects) {
        Map<String, VariantSQL> variantMap = new HashMap<>();
        Map<VariantSQL, List<VariantEffectSQL>> resultVariantMap = new HashMap<>();
        //init map with variants
        for (VariantSQL curVar : variants) {
            resultVariantMap.put(curVar, new ArrayList<VariantEffectSQL>());
            variantMap.put(curVar.getUploadedVariant(), curVar);
        }

        for (VariantEffectSQL variantEffect : variantEffects) {
            if (variantMap.get(variantEffect.getUploaded_variation()) == null) {
                System.out.println("here");
            } else {
                VariantSQL curVar = variantMap.get(variantEffect.getUploaded_variation());
                resultVariantMap.get(curVar).add(variantEffect);
            }
        }
        return resultVariantMap;
    }

    private boolean foundTranscripts(List<VariantEffectSQL> curVariantEffects) {
        for (VariantEffectSQL variantEffect : curVariantEffects) {
            TranscriptSQL transcriptSQL = transcriptDao.getByName(variantEffect.getTranscriptName());
            if (transcriptSQL != null) {
                return true;
            }
        }
        return false;
    }

}
