/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.wipe;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.matching.MatchSQL;
import com.ikmb.varwatchsql.entities.VariantStatusSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetManager;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.matching.MatchVariantDataManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author bfredrich
 */
public class WipeDataDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    @Inject
    private DatasetManager dsDM;
    
    @Inject
    private MatchVariantDataManager matchVariantDm;

    public void wipeDataset(Long datasetID) {
        Set<Long> matchIds = new HashSet<Long>();
        DatasetVWSQL dataset = dsDM.getDatasetByID(datasetID);
        for (VariantSQL curVar : dataset.getVariants()) {
//            Set<MatchSQL> curMatches = curVar.getMatches();
            List<MatchSQL> curMatches = matchVariantDm.getMatchesByVariant(curVar);
            for (MatchSQL curMatch : curMatches) {
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
    public void wipeVariantsByDataset(DatasetVWSQL dataset) {
        Query q = emProvider.get().createQuery("Delete FROM VariantSQL u WHERE u.dataset_vw = :ds").setParameter("ds", dataset);
        q.executeUpdate();
    }
}
