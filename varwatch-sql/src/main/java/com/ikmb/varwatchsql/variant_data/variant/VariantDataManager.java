/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.variant_data.variant;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.ikmb.varwatchcommons.entities.MetaData;
import com.ikmb.varwatchcommons.entities.VWVariant;
import com.ikmb.varwatchcommons.entities.Variant;
import com.ikmb.varwatchcommons.utils.VariantHash;
import com.ikmb.varwatchsql.guice.VarWatchInjector;
import com.ikmb.varwatchsql.guice.VarWatchPersist;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetDao;
import com.ikmb.varwatchsql.data.hpo.HPOTermBuilder;
import com.ikmb.varwatchsql.status.variant.VariantStatusManager;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceException;

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

    @Inject
    private VariantMetaDataDao variantMetaDao;

    @Transactional
    public List<VariantSQL> save(List<VWVariant> variants, DatasetVWSQL dataset) {
        dataset = datasetDao.getDataset(dataset.getId());

        List<VariantSQL> variantsSQL = new ArrayList<VariantSQL>();
        for (VWVariant variant : variants) {
            VariantSQL variantSql = variantBuilder.withVWVariant(variant).buildSql();
            variantSql.setDataset(dataset);
            variantDao.persist(variantSql);

            for (MetaData m : variant.getMetaData()) {
                VariantMetaDataSQL varMeta = new VariantMetaDataSQL();
                varMeta.setKey(m.getDataKey());
                varMeta.setValue(m.getDataValue());
                varMeta.setVariant(variantSql);
                variantMetaDao.persist(varMeta);
            }

            variantsSQL.add(variantSql);
//            String variantHash = variantHasher.getVariantHash(variant, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());

//            variantStatusManager.save(dataset, variant.toCompactString(), variantHash, variantSql, status);
        }

        return variantsSQL;
    }

    @Transactional
    public List<Variant> getVariantsByDataset(Long datasetId) {
        DatasetVWSQL dataset = datasetDao.getDataset(datasetId);
        List<VariantSQL> variantsSql = variantDao.getVariantsByDataset(dataset);
        List<Variant> variants = new ArrayList<>();
        for (VariantSQL variantSql : variantsSql) {
            Variant variant = variantBuilder.withSQLVariant(variantSql).build();
            variants.add(variant);
        }
        return variants;
    }

    @Transactional
    public List<VariantSQL> getVariantsByDatasetWithMatches(Long datasetId) {
        DatasetVWSQL dataset = datasetDao.getDataset(datasetId);
        List<VariantSQL> variantsSql = variantDao.getVariantsByDatasetWithMatches(dataset);

        return variantsSql;
    }

    @Transactional
    public VariantSQL get(Long variantId) {
        return variantDao.get(variantId);
    }

    @Transactional
    public void deleteVariant(VariantSQL variant) {
        variantDao.remove(variant);
    }

    @Transactional
    public void deleteBeaconVariant(Long variantID) {
        variantDao.removeComplete(variantID);
    }

    public static void main(String[] args) {
        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
        VariantDataManager vdm = inj.getInstance(VariantDataManager.class);
        List<VariantSQL> variants = vdm.getVariantsByDatasetWithMatches(105288l);
        System.out.println("finish");

    }
}
