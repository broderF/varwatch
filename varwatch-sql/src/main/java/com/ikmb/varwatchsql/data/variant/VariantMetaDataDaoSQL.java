/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.variant;

import com.google.inject.Inject;
import com.google.inject.Provider;
import javax.persistence.EntityManager;

/**
 *
 * @author bfredrich
 */
public class VariantMetaDataDaoSQL  {

    @Inject
    private Provider<EntityManager> emProvider;

//    public void persist(VariantMetaData variantSql) {
//        emProvider.get().persist(variantSql);
//    }
}
