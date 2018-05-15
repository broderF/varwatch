/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.wipe;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.matching.Match;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.wipe.WipeDataDao;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author bfredrich
 */
public class WipeDataDaoSQL implements WipeDataDao{

    @Inject
    private Provider<EntityManager> emProvider;

    @Inject
    private DatasetManager dsDM;

    @Inject
    private MatchVariantDataManager matchVariantDm;

    public void wipeDataset(Long datasetID) {
        Set<Long> matchIds = new HashSet<Long>();
        DatasetVW dataset = dsDM.getDatasetByID(datasetID);
        for (Variant curVar : dataset.getVariants()) {
//            Set<MatchSQL> curMatches = curVar.getMatches();
            List<Match> curMatches = matchVariantDm.getMatchesByVariant(curVar);
            for (Match curMatch : curMatches) {
                matchIds.add(curMatch.getId());
            }
        }
//            TypedQuery<DatasetSQL> dsquery = _em.createQuery("SELECT u FROM DatasetSQL u WHERE u.id = :id", DatasetSQL.class);
//            DatasetSQL ds = dsquery.setParameter("id", curDS.getId()).getSingleResult();
        Query q = emProvider.get().createQuery("Delete FROM DatasetSQL u WHERE u.id = :id").setParameter("id", dataset.getId());
        q.executeUpdate();

        Query jobq = emProvider.get().createNativeQuery("Delete FROM analysis_job WHERE dataset_id = :id").setParameter("id", dataset.getId());
        jobq.executeUpdate();

        for (Long matchId : matchIds) {
            Query qM = emProvider.get().createQuery("Delete FROM MatchSQL u WHERE u.id = :id").setParameter("id", matchId);
            qM.executeUpdate();
        }
    }

//    public void wipeDataByUser(UserSQL user) {
//
//        Set<Long> matchGroupIDS = new HashSet<Long>();
//        Set<DatasetVWSQL> datasets = user.getDatasets();
//        for (DatasetVWSQL curDS : datasets) {
//            for (VariantSQL curVar : curDS.getVariants()) {
//                Set<MatchSQL> curMatches = curVar.getMatches();
//                for (MatchSQL curMatch : curMatches) {
//                    matchGroupIDS.add(curMatch.getMatchGroup().getId());
//                }
//            }
////            TypedQuery<DatasetSQL> dsquery = _em.createQuery("SELECT u FROM DatasetSQL u WHERE u.id = :id", DatasetSQL.class);
////            DatasetSQL ds = dsquery.setParameter("id", curDS.getId()).getSingleResult();
//            Query q = emProvider.get().createQuery("Delete FROM DatasetSQL u WHERE u.id = :id").setParameter("id", curDS.getId());
//            q.executeUpdate();
//
//            Query jobq = emProvider.get().createNativeQuery("Delete FROM analysis_job WHERE dataset_id = :id").setParameter("id", curDS.getId());
//            jobq.executeUpdate();
//
//        }
//        for (Long matchGrpId : matchGroupIDS) {
//            TypedQuery<MatchGroupSQL> grpquery = emProvider.get().createQuery("SELECT u FROM MatchGroupSQL u WHERE u.id = :id", MatchGroupSQL.class
//            );
//            MatchGroupSQL grp = grpquery.setParameter("id", matchGrpId).getSingleResult();
//            boolean delete = true;
//            for (MatchSQL match
//                    : grp.getMatches()) {
//                Integer matchSize = match.getExternalVariants().size() + match.getVariants().size();
//                if (matchSize < 2) {
//                    emProvider.get().remove(match);
//                } else {
//                    delete = false;
//                }
//            }
//            if (delete) {
//                Query q = emProvider.get().createQuery("Delete FROM MatchGroupSQL u WHERE u.id = :id").setParameter("id", matchGrpId);
//                q.executeUpdate();
//            }
//        }
//    }
    public void wipeVariantsByDataset(DatasetVW dataset) {
        Query q = emProvider.get().createQuery("Delete FROM VariantSQL u WHERE u.dataset_vw = :ds").setParameter("ds", dataset);
        q.executeUpdate();
    }
}
