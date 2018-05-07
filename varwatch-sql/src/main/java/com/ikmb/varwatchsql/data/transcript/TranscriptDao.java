/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.transcript;

import com.google.inject.Inject;
import com.google.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class TranscriptDao {

    @Inject
//    private EntityManager em;
          private Provider<EntityManager> emProvider;

    public TranscriptSQL getByName(String transcriptName) {
        TranscriptSQL transcriptSQL = null;
        try {
            TypedQuery<TranscriptSQL> queryTrans = emProvider.get().createQuery("SELECT s FROM TranscriptSQL s WHERE s.name = :name", TranscriptSQL.class);
            transcriptSQL = queryTrans.setParameter("name", transcriptName).getSingleResult();
        } catch (NoResultException nre) {
        }
        return transcriptSQL;
    }

}
