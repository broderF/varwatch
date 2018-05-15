/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.varianteffect;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.dataset.DatasetDao;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.varwatchcommons.entities.VWStatus;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import com.ikmb.core.varwatchcommons.utils.VariantHash;
import com.ikmb.core.data.variant.VariantBuilder;
import com.ikmb.core.data.variant.VariantDao;
import com.ikmb.core.data.hpo.HPOTermBuilder;
import com.ikmb.core.data.transcript.Transcript;
import com.ikmb.core.data.transcript.TranscriptDao;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantStatusBuilder;
import com.ikmb.core.data.variant.VariantStatusManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.PersistenceException;
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

    @Transactional(rollbackOn = {PersistenceException.class})
    public List<VariantEffect> persistSQLVariantEffects(List<VariantEffect> variantEffects, DatasetVW dataset) {
        List<VariantEffect> variantEffectsSQL = new ArrayList<>();
        dataset = datasetDao.getDataset(dataset.getId());

        List<Variant> variants = variantDao.getVariantsByDataset(dataset);

        Map<Variant, List<VariantEffect>> variantMap = mapEffectsToVariant(variants, variantEffects);

        for (Variant curVar : variantMap.keySet()) {
            List<VariantEffect> curVariantEffects = variantMap.get(curVar);
            if (curVariantEffects.isEmpty()) {
                VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.IMPACT_TOO_LOW.getMessage()).buildVWStatus();
                VWVariant buildVW = variantBuilder.withVariant(curVar).buildVW();
                String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
                variantStatusManager.save2(dataset, null, variantHash, status);
                variantDao.remove(curVar);

            } else if (!foundTranscripts(curVariantEffects)) {
                for (VariantEffect variantEffect : curVariantEffects) {
                    Transcript transcriptSQL = transcriptDao.getByName(variantEffect.getTranscriptName());
                    if (transcriptSQL == null) {
                        logger.info(" no transcript found for: {}", variantEffect.getTranscriptName());
                        VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.NO_TRANSCRIPT_FOUND.getMessage()).withMessageAddition(variantEffect.getTranscriptName()).buildVWStatus();
                        VWVariant buildVW = variantBuilder.withVariant(curVar).buildVW();
                        String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
                        variantStatusManager.save2(dataset, null, variantHash, status);
                        continue;
                    }
                }
                variantDao.remove(curVar);
            } else {
                for (VariantEffect variantEffect : curVariantEffects) {
                    Transcript transcriptSQL = transcriptDao.getByName(variantEffect.getTranscriptName());
                    if (transcriptSQL == null) {
                        logger.info(" no transcript found for: {}", variantEffect.getTranscriptName());
                        VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.NO_TRANSCRIPT_FOUND.getMessage()).withMessageAddition(variantEffect.getTranscriptName()).buildVWStatus();
                        VWVariant buildVW = variantBuilder.withVariant(curVar).buildVW();
                        String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
                        variantStatusManager.save(dataset, null, variantHash, curVar, status);
                        continue;
                    }

                    VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.STORED).withMessage(VariantStatusBuilder.VariantStatusMessage.STORED_IN_VARWATCH.getMessage()).buildVWStatus();
                    VWVariant buildVW = variantBuilder.withVariant(curVar).buildVW();
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

    private Map<Variant, List<VariantEffect>> mapEffectsToVariant(List<Variant> variants, List<VariantEffect> variantEffects) {
        Map<String, Variant> variantMap = new HashMap<>();
        Map<Variant, List<VariantEffect>> resultVariantMap = new HashMap<>();
        //init map with variants
        for (Variant curVar : variants) {
            resultVariantMap.put(curVar, new ArrayList<VariantEffect>());
            variantMap.put(curVar.getUploadedVariant(), curVar);
        }

        for (VariantEffect variantEffect : variantEffects) {
            if (variantMap.get(variantEffect.getUploaded_variation()) == null) {
                System.out.println("here");
            } else {
                Variant curVar = variantMap.get(variantEffect.getUploaded_variation());
                resultVariantMap.get(curVar).add(variantEffect);
            }
        }
        return resultVariantMap;
    }

    private boolean foundTranscripts(List<VariantEffect> curVariantEffects) {
        for (VariantEffect variantEffect : curVariantEffects) {
            Transcript transcriptSQL = transcriptDao.getByName(variantEffect.getTranscriptName());
            if (transcriptSQL != null) {
                return true;
            }
        }
        return false;
    }

    public void persistSQLVariantEffect(Variant curVar, List<VariantEffect> curVariantEffects) {

        DatasetVW dataset = curVar.getDataset();
        if (curVariantEffects.isEmpty()) {
            VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.IMPACT_TOO_LOW.getMessage()).buildVWStatus();
            VWVariant buildVW = variantBuilder.withVariant(curVar).buildVW();
            String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
            variantStatusManager.save2(dataset, null, variantHash, status);
            variantDao.remove(curVar);

        } else if (!foundTranscripts(curVariantEffects)) {
            for (VariantEffect variantEffect : curVariantEffects) {
                Transcript transcriptSQL = transcriptDao.getByName(variantEffect.getTranscriptName());
                if (transcriptSQL == null) {
                    logger.info(" no transcript found for: {}", variantEffect.getTranscriptName());
                    VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.NO_TRANSCRIPT_FOUND.getMessage()).withMessageAddition(variantEffect.getTranscriptName()).buildVWStatus();
                    VWVariant buildVW = variantBuilder.withVariant(curVar).buildVW();
                    String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
                    variantStatusManager.save2(dataset, null, variantHash, status);
                    continue;
                }
            }
            variantDao.remove(curVar);
        } else {
            for (VariantEffect variantEffect : curVariantEffects) {
                Transcript transcriptSQL = transcriptDao.getByName(variantEffect.getTranscriptName());
                if (transcriptSQL == null) {
                    logger.info(" no transcript found for: {}", variantEffect.getTranscriptName());
                    VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.NO_TRANSCRIPT_FOUND.getMessage()).withMessageAddition(variantEffect.getTranscriptName()).buildVWStatus();
                    VWVariant buildVW = variantBuilder.withVariant(curVar).buildVW();
                    String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
                    variantStatusManager.save(dataset, null, variantHash, curVar, status);
                    continue;
                }

                VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.STORED).withMessage(VariantStatusBuilder.VariantStatusMessage.STORED_IN_VARWATCH.getMessage()).buildVWStatus();
                VWVariant buildVW = variantBuilder.withVariant(curVar).buildVW();
                String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
                variantStatusManager.save(dataset, null, variantHash, curVar, status);

                variantEffect.setVariant(curVar);
                variantEffect.setTranscript(transcriptSQL);
                variantEffectDao.persist(variantEffect);
            }
        }
    }
}
