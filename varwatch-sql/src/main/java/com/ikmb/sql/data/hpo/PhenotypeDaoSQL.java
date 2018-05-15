/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.data.hpo;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.data.hpo.PhenotypeDao;
import com.ikmb.core.data.dataset.DatasetManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class PhenotypeDaoSQL implements PhenotypeDao {

    @Inject
    private Provider<EntityManager> emProvider;
    
    @Inject
    private com.ikmb.core.data.hpo.HPOTermBuilder hpoBuilder;
    
     private final org.slf4j.Logger logger = LoggerFactory.getLogger(DatasetManager.class);

    public List<HPOTerm> getHPOTermsByNames(List<String> hpoTerms, String subontology) {
        if (hpoTerms.isEmpty()) {
            return new ArrayList<>();
        }
        List<HPOTerm> hpoTermsSQL = new ArrayList<>();
        try {
            TypedQuery<HPOTerm> query = emProvider.get().createQuery("SELECT s FROM HPOTerm s WHERE s.identifier IN (:identifiers) AND s.subontology = :subontology", HPOTerm.class);
            hpoTermsSQL = query.setParameter("identifiers", hpoTerms).setParameter("subontology", subontology).getResultList();
        } catch (NoResultException nre) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, nre);
        }
        
        List<HPOTerm> hpos = new ArrayList<>();
        
        return hpos;
    }

    public HPOTerm getHPOTermByName(String hpoTerm, String subontology) {
        TypedQuery<HPOTerm> query = emProvider.get().createQuery("SELECT s FROM HPOTerm s WHERE s.identifier = :identifier AND s.subontology = :subontology", HPOTerm.class);
        try {
            HPOTerm singleResult = query.setParameter("identifier", hpoTerm).setParameter("subontology", subontology).getSingleResult();
            return singleResult;
        } catch (NoResultException nre) {
           logger.warn("Cant find an hpo term for {} and subontology {}",hpoTerm,subontology);
        }
        return null;
    }
}
