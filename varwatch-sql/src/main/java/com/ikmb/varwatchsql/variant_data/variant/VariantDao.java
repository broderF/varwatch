/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.variant_data.variant;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.matching.MatchSQL;
import com.ikmb.varwatchsql.matching.MatchVariantDataManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class VariantDao {

    private final Logger logger = LoggerFactory.getLogger(VariantDao.class);

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    @Inject
    private MatchVariantDataManager matchVariantDm;

    public void persist(VariantSQL variantSql) {
        emProvider.get().persist(variantSql);
    }

    public void refresh(VariantSQL variantSql) {
        emProvider.get().refresh(variantSql);
    }

    public void remove(VariantSQL variant) {
        emProvider.get().refresh(variant);
        emProvider.get().remove(variant);
    }

    public void removeComplete(Long varId) {
        VariantSQL variant = emProvider.get().find(VariantSQL.class, varId);
        List<MatchSQL> matches = matchVariantDm.getMatchesByVariant(variant);
//        Set<MatchSQL> matches = variant.getMatches();

        //remove matches
        for (MatchSQL match : matches) {
            Query q = emProvider.get().createQuery("Delete FROM MatchSQL u WHERE u.id = :id").setParameter("id", match.getId());
            q.executeUpdate();
        }
        //remove var
        Query q = emProvider.get().createQuery("Delete FROM VariantSQL u WHERE u.id = :id").setParameter("id", varId);
        q.executeUpdate();
    }

    public void removeVariantWithMatches(VariantSQL variantSql) {
        logger.info("delete variant with id {}", variantSql.getId());
        Query qv = emProvider.get().createQuery("Delete FROM VariantSQL u WHERE u.id = :id").setParameter("id", variantSql.getId());
        qv.executeUpdate();
        
        List<MatchSQL> curMatches = matchVariantDm.getMatchesByVariant(variantSql);
//        Set<MatchSQL> curMatches = variantSql.getMatches();

        for (MatchSQL match : curMatches) {
            logger.info("delete match with id {}", match.getId());
            Query q = emProvider.get().createQuery("Delete FROM MatchSQL u WHERE u.id = :id").setParameter("id", match.getId());
            q.executeUpdate();
        }
    }

    public List<VariantSQL> getVariantsByDataset(DatasetVWSQL dataset) {
        TypedQuery<VariantSQL> query = emProvider.get().createQuery("SELECT s FROM VariantSQL s WHERE s.dataset_vw = :dataset", VariantSQL.class
        );
        List<VariantSQL> variants = query.setParameter("dataset", dataset).getResultList();
        return variants;
    }

    public List<VariantSQL> getVariantsByDatasetWithMatches(DatasetVWSQL dataset) {
        TypedQuery<VariantSQL> query = emProvider.get().createQuery("SELECT s FROM VariantSQL s WHERE s.dataset_vw = :dataset", VariantSQL.class
        );
        List<VariantSQL> variants = query.setParameter("dataset", dataset).getResultList();
        for (VariantSQL variant : variants) {
            emProvider.get().refresh(variant);
//            variant.getMatches();
            List<MatchSQL> curMatches = matchVariantDm.getMatchesByVariant(variant);
        }
        return variants;
    }

    public VariantSQL get(Long variantId) {
        VariantSQL variant = emProvider.get().find(VariantSQL.class, variantId);
        if (variant == null) {
            logger.error("variant is null with id: " + variantId);
        }
        emProvider.get().refresh(variant);
        return variant;
    }

//    public VariantSQL getVariantWithMatches(Long id) {
//        VariantSQL variant = emProvider.get().find(VariantSQL.class, id);
////        emProvider.get().merge(variant);
//        emProvider.get().refresh(variant);
//         List<MatchSQL> curMatches = matchVariantDm.getMatchesByVariant(variant);
////        variant.getMatches();
//        return variant;
//    }

}
