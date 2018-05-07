/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.update.variantdata;

import com.ikmb.update.databasehelper.PersistenceManager;
import com.ikmb.varwatchsql.entities.DatasetHGMDSQL;
import com.ikmb.varwatchsql.data.hpo.HPOTermSQL;
import com.ikmb.varwatchsql.data.transcript.TranscriptSQL;
import com.ikmb.varwatchsql.data.hpo.PhenotypeSQL;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class DatasetService {

    public void persistHgmdDataset(DatasetHGMDSQL dataset) {
//        TranscriptSQL transcript = getTranscript(dataset.getTranscript().getName());
        EntityManager _em = PersistenceManager.INSTANCE.getEntityManager();
        _em.getTransaction().begin();
        TranscriptSQL transcript = null;
        try {
            TypedQuery<TranscriptSQL> tquery = _em.createQuery("SELECT s FROM TranscriptSQL s WHERE s.name = :name", TranscriptSQL.class);
            transcript = tquery.setParameter("name", dataset.getTranscript().getName()).getSingleResult();

        } catch (NoResultException ex) {
            ex.printStackTrace();
            System.out.println("no result");
            _em.getTransaction().commit();
            _em.close();
            return;
        }

        Set<PhenotypeSQL> hpoTerms = new HashSet<>();
        for (PhenotypeSQL hpo : dataset.getPhenotypes()) {
            PhenotypeSQL phenotypeTerm = null;
            HPOTermSQL hpoTerm = null;
            try {
                TypedQuery<HPOTermSQL> query = _em.createQuery("SELECT s FROM HPOTermSQL s WHERE s.identifier = :identifier", HPOTermSQL.class);
                hpoTerm = query.setParameter("identifier", hpo.getPhenotype().getIdentifier()).getSingleResult();

            } catch (NoResultException ex) {
                ex.printStackTrace();
                System.out.println("no result");
            }
            if (hpoTerm != null) {
                phenotypeTerm = new PhenotypeSQL();
                phenotypeTerm.setPhenotype(hpoTerm);
                phenotypeTerm.setDataset(dataset);
                hpoTerms.add(phenotypeTerm);
            }
        }
        dataset.setTranscript(transcript);
        dataset.setPhenotypes(hpoTerms);
        _em.persist(dataset);
        _em.getTransaction().commit();
        _em.close();
    }

    private TranscriptSQL getTranscript(String transcriptName) {
        EntityManager _em = PersistenceManager.INSTANCE.getEntityManager();
        _em.getTransaction().begin();
        TypedQuery<TranscriptSQL> query = _em.createQuery("SELECT s FROM TranscriptSQL s WHERE s.name = :name", TranscriptSQL.class);
        TranscriptSQL transcript = query.setParameter("name", transcriptName).getSingleResult();
        _em.getTransaction().commit();
        _em.close();
        return transcript;
    }
}
