/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.variant_data.variant;

import com.google.inject.Inject;
import com.google.inject.Provider;
import javax.persistence.EntityManager;

/**
 *
 * @author bfredrich
 */
class VariantMetaDataDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    public void persist(VariantMetaDataSQL variantSql) {
        emProvider.get().persist(variantSql);
    }
}
