/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.variant_data.varianteffect;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.varwatchsql.entities.VariantEffectSQL;
import javax.persistence.EntityManager;

/**
 *
 * @author broder
 */
public class VariantEffectDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    void persist(VariantEffectSQL variantEffectSQL) {
        emProvider.get().persist(variantEffectSQL);
    }

}
