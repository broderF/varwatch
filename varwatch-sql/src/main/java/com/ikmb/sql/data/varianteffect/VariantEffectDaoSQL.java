/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.data.varianteffect;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.data.varianteffect.VariantEffect;
import com.ikmb.core.data.varianteffect.VariantEffectDao;
import javax.persistence.EntityManager;

/**
 *
 * @author broder
 */
public class VariantEffectDaoSQL implements VariantEffectDao{

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    public void persist(VariantEffect variantEffectSQL) {
        emProvider.get().persist(variantEffectSQL);
    }

}
