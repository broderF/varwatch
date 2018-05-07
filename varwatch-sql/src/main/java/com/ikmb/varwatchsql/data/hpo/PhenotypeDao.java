/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.hpo;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetManager;
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
public class PhenotypeDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;
    
     private final org.slf4j.Logger logger = LoggerFactory.getLogger(DatasetManager.class);

    public List<HPOTermSQL> getHPOTermsByNames(List<String> hpoTerms, String subontology) {
        if (hpoTerms.isEmpty()) {
            return new ArrayList<>();
        }
        List<HPOTermSQL> hpoTermsSQL = new ArrayList<>();
        try {
            TypedQuery<HPOTermSQL> query = emProvider.get().createQuery("SELECT s FROM HPOTermSQL s WHERE s.identifier IN (:identifiers) AND s.subontology = :subontology", HPOTermSQL.class);
            hpoTermsSQL = query.setParameter("identifiers", hpoTerms).setParameter("subontology", subontology).getResultList();
        } catch (NoResultException nre) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, nre);
        }
        return hpoTermsSQL;
    }

    public HPOTermSQL getHPOTermByName(String hpoTerm, String subontology) {
        TypedQuery<HPOTermSQL> query = emProvider.get().createQuery("SELECT s FROM HPOTermSQL s WHERE s.identifier = :identifier AND s.subontology = :subontology", HPOTermSQL.class);
        try {
            return query.setParameter("identifier", hpoTerm).setParameter("subontology", subontology).getSingleResult();
        } catch (NoResultException nre) {
           logger.warn("Cant find an hpo term for {} and subontology {}",hpoTerm,subontology);
        }
        return null;
    }
}
