/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.data.transcript;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.data.transcript.Transcript;
import com.ikmb.core.data.transcript.TranscriptDao;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class TranscriptDaoSQL implements TranscriptDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    public Transcript getByName(String transcriptName) {
        Transcript transcriptSQL = null;
        try {
            TypedQuery<Transcript> queryTrans = emProvider.get().createQuery("SELECT s FROM Transcript s WHERE s.name = :name", Transcript.class);
            transcriptSQL = queryTrans.setParameter("name", transcriptName).getSingleResult();
        } catch (NoResultException nre) {
        }
        return transcriptSQL;
    }

}
