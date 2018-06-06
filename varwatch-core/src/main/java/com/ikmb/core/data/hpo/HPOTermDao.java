/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.hpo;

import java.util.Map;

/**
 *
 * @author bfredrich
 */
public interface HPOTermDao {

    public Map<Long, HpoPathTerm> getHpoTermsFromDb(String connectionPath);

    public HPOTerm saveOrUpdate(String primaryId);

    public void updatePhenotype(Phenotype curPheno);

}
