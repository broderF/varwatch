/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.variant;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.dataset.DatasetDao;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.family.GeneFamily;
import com.ikmb.core.data.gene.Gene;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import com.ikmb.core.utils.VariantHash;
import com.ikmb.core.data.hpo.HPOTermBuilder;
import com.ikmb.core.data.pathway.Pathway;
import com.ikmb.core.data.varianteffect.VariantEffect;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author broder
 */
public class VariantDataManager {

    @Inject
    private DatasetDao datasetDao;

    @Inject
    private VariantBuilder variantBuilder;

    @Inject
    private VariantDao variantDao;

    @Inject
    private VariantStatusManager variantStatusManager;

    @Inject
    private VariantHash variantHasher;

    @Inject
    private HPOTermBuilder hpoTermBuilder;

//    @Inject
//    private VariantMetaDataDao variantMetaDao;
    @Transactional
    public List<Variant> save(List<VWVariant> variants, DatasetVW dataset) {
        dataset = datasetDao.getDataset(dataset.getId());

        List<Variant> variantsSQL = new ArrayList<>();
        for (VWVariant variant : variants) {
            Variant variantSql = variantBuilder.withVWVariant(variant).buildSql();
            variantSql.setDataset(dataset);
            variantDao.persist(variantSql);

//            for (MetaData m : variant.getMetaData()) {
//                VariantMetaDataSQL varMeta = new VariantMetaDataSQL();
//                varMeta.setKey(m.getDataKey());
//                varMeta.setValue(m.getDataValue());
//                varMeta.setVariant(variantSql);
//                variantMetaDao.persist(varMeta);
//            }
            variantsSQL.add(variantSql);
//            String variantHash = variantHasher.getVariantHash(variant, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());

//            variantStatusManager.save(dataset, variant.toCompactString(), variantHash, variantSql, status);
        }

        return variantsSQL;
    }

    @Transactional
    public List<com.ikmb.core.varwatchcommons.entities.Variant> getVariantsByDataset(Long datasetId) {
        DatasetVW dataset = datasetDao.getDataset(datasetId);
        List<Variant> variantsSql = variantDao.getVariantsByDataset(dataset);
        List<com.ikmb.core.varwatchcommons.entities.Variant> variants = new ArrayList<>();
        for (Variant variantSql : variantsSql) {
            com.ikmb.core.varwatchcommons.entities.Variant variant = variantBuilder.withVariant(variantSql).build();
            variants.add(variant);
        }
        return variants;
    }

    @Transactional
    public List<Variant> getVariantsByDatasetWithMatches(Long datasetId) {
        DatasetVW dataset = datasetDao.getDataset(datasetId);
        List<Variant> variantsSql = variantDao.getVariantsByDatasetWithMatches(dataset);

        return variantsSql;
    }

    @Transactional
    public Variant get(Long variantId) {
        return variantDao.get(variantId);
    }

    @Transactional
    public void deleteVariant(Variant variant) {
        variantDao.remove(variant);
    }

    @Transactional
    public void deleteBeaconVariant(Long variantID) {
        variantDao.removeComplete(variantID);
    }

    @Transactional
    public List<Gene> getGenesFromVariant(Long variantId) {
        Variant variant = get(variantId);
        Set<Gene> genesSql = new HashSet<>();
        for (VariantEffect variantEffect : variant.getVariantEffects()) {
            Gene gene = variantEffect.getTranscript().getGene();
            genesSql.add(gene);
        }
        return new ArrayList<>(genesSql);
    }

    @Transactional
    public List<Pathway> getPathwaysFromVariant(Long variantId) {
        Variant variant = get(variantId);
        Set<Pathway> pathwaysSql = new HashSet<>();
        for (VariantEffect variantEffect : variant.getVariantEffects()) {
            Gene gene = variantEffect.getTranscript().getGene();
            Set<Pathway> currentPathways = gene.getPathways();
            pathwaysSql.addAll(currentPathways);
        }
        return new ArrayList<>(pathwaysSql);
    }

    @Transactional
    public List<GeneFamily> getFamiliesFromVariant(Long variantId) {
        Variant variant = get(variantId);
        List<GeneFamily> familiesSql = new ArrayList<>();
        for (VariantEffect variantEffect : variant.getVariantEffects()) {
            Gene gene = variantEffect.getTranscript().getGene();
            Set<GeneFamily> currentFamilies = gene.getFamilies();
            familiesSql.addAll(currentFamilies);
        }
        return familiesSql;
    }

//
//    public static void main(String[] args) {
//        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
//        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
//        VariantDataManager vdm = inj.getInstance(VariantDataManager.class);
//        List<VariantSQL> variants = vdm.getVariantsByDatasetWithMatches(105288l);
//        System.out.println("finish");
//
//    }
}
