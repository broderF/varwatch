/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.hpo;

import java.util.List;

/**
 *
 * @author broder
 */
public interface PhenotypeDao {

    public List<HPOTerm> getHPOTermsByNames(List<String> hpoTerms, String subontology);
    
    public HPOTerm getHPOTermByName(String hpoTerm, String subontology);
}
