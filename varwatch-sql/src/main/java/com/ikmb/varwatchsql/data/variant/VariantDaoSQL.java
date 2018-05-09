/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.variant;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.matching.Match;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantDao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class VariantDaoSQL implements VariantDao {

    private final Logger logger = LoggerFactory.getLogger(VariantDaoSQL.class);

    @Inject
    private Provider<EntityManager> emProvider;

    @Inject
    private MatchVariantDataManager matchVariantDm;

    public void persist(Variant variantSql) {
        emProvider.get().persist(variantSql);
    }

    public void refresh(Variant variantSql) {
        emProvider.get().refresh(variantSql);
    }

    public void remove(Variant variant) {
        emProvider.get().refresh(variant);
        emProvider.get().remove(variant);
    }

    public void removeComplete(Long varId) {
        Variant variant = emProvider.get().find(Variant.class, varId);
        List<Match> matches = matchVariantDm.getMatchesByVariant(variant);
//        Set<MatchSQL> matches = variant.getMatches();

        //remove matches
        for (Match match : matches) {
            Query q = emProvider.get().createQuery("Delete FROM MatchSQL u WHERE u.id = :id").setParameter("id", match.getId());
            q.executeUpdate();
        }
        //remove var
        Query q = emProvider.get().createQuery("Delete FROM Variant u WHERE u.id = :id").setParameter("id", varId);
        q.executeUpdate();
    }

    public void removeVariantWithMatches(Variant variantSql) {
        logger.info("delete variant with id {}", variantSql.getId());
        Query qv = emProvider.get().createQuery("Delete FROM Variant u WHERE u.id = :id").setParameter("id", variantSql.getId());
        qv.executeUpdate();

        List<Match> curMatches = matchVariantDm.getMatchesByVariant(variantSql);
//        Set<MatchSQL> curMatches = variantSql.getMatches();

        for (Match match : curMatches) {
            logger.info("delete match with id {}", match.getId());
            Query q = emProvider.get().createQuery("Delete FROM MatchSQL u WHERE u.id = :id").setParameter("id", match.getId());
            q.executeUpdate();
        }
    }

    public List<Variant> getVariantsByDataset(DatasetVW dataset) {
        TypedQuery<Variant> query = emProvider.get().createQuery("SELECT s FROM Variant s WHERE s.dataset_vw = :dataset", Variant.class
        );
        List<Variant> variants = query.setParameter("dataset", dataset).getResultList();
        return new ArrayList<Variant>(variants);
    }

    public List<Variant> getVariantsByDatasetWithMatches(DatasetVW dataset) {
        TypedQuery<Variant> query = emProvider.get().createQuery("SELECT s FROM Variant s WHERE s.dataset_vw = :dataset", Variant.class
        );
        List<Variant> variants = query.setParameter("dataset", dataset).getResultList();
        for (Variant variant : variants) {
            emProvider.get().refresh(variant);
//            variant.getMatches();
            List<Match> curMatches = matchVariantDm.getMatchesByVariant(variant);
        }
        return new ArrayList<Variant>(variants);
    }

    public Variant get(Long variantId) {
        Variant variant = emProvider.get().find(Variant.class, variantId);
        if (variant == null) {
            logger.error("variant is null with id: " + variantId);
        }
        emProvider.get().refresh(variant);
        return variant;
    }

//    public Variant getVariantWithMatches(Long id) {
//        Variant variant = emProvider.get().find(Variant.class, id);
////        emProvider.get().merge(variant);
//        emProvider.get().refresh(variant);
//         List<MatchSQL> curMatches = matchVariantDm.getMatchesByVariant(variant);
////        variant.getMatches();
//        return variant;
//    }
}
