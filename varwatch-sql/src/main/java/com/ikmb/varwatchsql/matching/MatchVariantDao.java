/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.matching;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.varwatchsql.auth.user.UserSQL;
//import static com.ikmb.varwatchsql.databasehelper.MatchingDatabaseHelper.getSetString;
import com.ikmb.varwatchsql.entities.DatasetHGMDSQL;
import com.ikmb.varwatchsql.entities.FamilySQL;
import com.ikmb.varwatchsql.data.gene.GeneSQL;
import com.ikmb.varwatchsql.entities.PathwaySQL;
import com.ikmb.varwatchsql.data.transcript.TranscriptSQL;
import com.ikmb.varwatchsql.entities.VariantEffectSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantDataManager;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;

/**
 *
 * @author broder
 */
public class MatchVariantDao {

    @Inject
    private Provider<EntityManager> emProvider;

    @Inject
    private VariantDataManager variantDm;

    public MatchSQL get(Long matchStatusID) {
        return emProvider.get().find(MatchSQL.class, matchStatusID);
    }

    public List<VariantSQL> getSimilarVariants(VariantSQL queryVariant, boolean geneBool, boolean pathwayBool, boolean familyBool) {
        List<VariantSQL> simVariants = new ArrayList<VariantSQL>();
        //pathways
        Set<Integer> pathwayIds = new HashSet<>();
        for (VariantEffectSQL vareff : queryVariant.getVariantEffects()) {
//            Hibernate.initialize(vareff);
            TranscriptSQL transcript = vareff.getTranscript();
            GeneSQL gene = transcript.getGene();
            TypedQuery<GeneSQL> qquery = emProvider.get().createQuery("SELECT g FROM GeneSQL g  WHERE id = :id", GeneSQL.class);
            gene = qquery.setParameter("id", gene.getId()).getSingleResult();
            Set<PathwaySQL> pathways = gene.getPathways();
            if (pathways != null) {
                for (PathwaySQL pathway : pathways) {
                    pathwayIds.add(pathway.getId());
                }
            }
        }

        //family
        Set<Integer> familyIds = new HashSet<>();
        for (VariantEffectSQL vareff : queryVariant.getVariantEffects()) {
            Hibernate.initialize(vareff);
            Set<FamilySQL> families = vareff.getTranscript().getGene().getFamilies();
            if (families != null) {
                for (FamilySQL family : families) {
                    familyIds.add(family.getId());
                }
            }
        }

        //genes
        Set<Integer> geneids = new HashSet<>();
        for (VariantEffectSQL vareff : queryVariant.getVariantEffects()) {
            Hibernate.initialize(vareff);
            GeneSQL gene = vareff.getTranscript().getGene();
            geneids.add(gene.getId());
        }

        //query
        String geneString = getSetString(geneBool, geneids, "g.id", true);
        String pathwayString = getSetString(pathwayBool, pathwayIds, "p.id", false);
        String familyString = getSetString(familyBool, familyIds, "f.id", false);
        Integer getNrOfIns = getNrOfIns(geneString, geneBool, pathwayString, pathwayBool, familyString, familyBool);
        if (getNrOfIns > 0) {
            TypedQuery<VariantSQL> qquery = emProvider.get().createQuery("SELECT distinct v FROM VariantEffectSQL veff JOIN veff.variant v JOIN v.dataset_vw ds JOIN veff.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f WHERE " + geneString + pathwayString + familyString + " AND ds.completed = true AND v.id != :variantid", VariantSQL.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
            qquery.setParameter("variantid", queryVariant.getId());
            System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
            simVariants = qquery.getResultList();
        }
        return simVariants;
    }

    private static Integer getNrOfIns(String geneString, boolean geneBool, String pathwayString, boolean pathwayBool, String familyString, boolean familyBool) {
        int nr = 0;
        if (geneBool && !geneString.equals("")) {
            nr++;
        }
        if (pathwayBool && !pathwayString.equals("")) {
            nr++;
        }
        if (familyBool && !familyString.equals("")) {
            nr++;
        }
        return nr;
    }

    public List<MatchSQL> persistMatches(List<MatchSQL> matches) {
        for (MatchSQL match : matches) {
            match.setNotified(false);
            match.setCreationTimestamp(new DateTime());
            emProvider.get().persist(match);

            for (MatchVariantSQL matchVariant : match.getVariants()) {
                emProvider.get().persist(matchVariant);
            }
        }
        return matches;
    }

    public List<DatasetHGMDSQL> getSimilarHGMDVariants(VariantSQL queryVariant, boolean geneBool, boolean pathwayBool, boolean familyBool) {
        List<DatasetHGMDSQL> simVariants = new ArrayList<DatasetHGMDSQL>();

        //pathways
        Set<Integer> pathwayIds = new HashSet<>();
        for (VariantEffectSQL vareff : queryVariant.getVariantEffects()) {
            Set<PathwaySQL> pathways = vareff.getTranscript().getGene().getPathways();
            if (pathways != null) {
                for (PathwaySQL pathway : pathways) {
                    pathwayIds.add(pathway.getId());
                }
            }
        }

        //family
        Set<Integer> familyIds = new HashSet<>();
        for (VariantEffectSQL vareff : queryVariant.getVariantEffects()) {
            Set<FamilySQL> families = vareff.getTranscript().getGene().getFamilies();
            if (families != null) {
                for (FamilySQL family : families) {
                    familyIds.add(family.getId());
                }
            }
        }

        //genes
        Set<Integer> geneids = new HashSet<>();
        for (VariantEffectSQL vareff : queryVariant.getVariantEffects()) {
            GeneSQL gene = vareff.getTranscript().getGene();
            geneids.add(gene.getId());
        }

        //query
//        TypedQuery<DatasetHGMDSQL> qquery = _em.createQuery("SELECT distinct ds FROM DatasetHGMDSQL d JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f JOIN WHERE g.id " + getInORNotIn(geneBool) + " (:geneids) AND p.id " + getInORNotIn(pathwayBool) + " (:pathwayids)  AND f.id " + getInORNotIn(familyBool) + " (:familyids)", DatasetHGMDSQL.class);
//        TypedQuery<DatasetHGMDSQL> qquery = _em.createQuery("SELECT distinct ds FROM DatasetHGMDSQL d JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f JOIN WHERE " + getSetString(geneBool, geneids, "g.id", true) + getSetString(pathwayBool, pathwayIds, "p.id", false) + getSetString(familyBool, familyIds, "f.id", false), DatasetHGMDSQL.class);
        //query
        String geneString = getSetString(geneBool, geneids, "g.id", true);
        String pathwayString = getSetString(pathwayBool, pathwayIds, "p.id", false);
        String familyString = getSetString(familyBool, familyIds, "f.id", false);
        Integer getNrOfIns = getNrOfIns(geneString, geneBool, pathwayString, pathwayBool, familyString, familyBool);

        Boolean pathwaySkip = pathwayBool && pathwayString.equals("");
        Boolean famSkip = familyBool && familyString.equals("");

        if (getNrOfIns > 0 && (geneBool || !(pathwaySkip || famSkip))) {
            TypedQuery<DatasetHGMDSQL> qquery = emProvider.get().createQuery("Select distinct ds FROM DatasetHGMDSQL ds JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f WHERE " + geneString + pathwayString + familyString, DatasetHGMDSQL.class
            );
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
            System.out.println(qquery.unwrap(org.hibernate.Query.class
            ).getQueryString());
            simVariants = qquery.getResultList();
        }
        return simVariants;
    }

    public List<DatasetHGMDSQL> getIdenticalHGMDVariants(VariantSQL variant) {
        TypedQuery<DatasetHGMDSQL> qquery = emProvider.get().createQuery("Select ds FROM DatasetHGMDSQL ds where ds.chrName = :chr AND ds.chrPos = :pos AND ds.refBase = :ref AND ds.altBase = :alt", DatasetHGMDSQL.class
        );
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
        System.out.println(qquery.unwrap(org.hibernate.Query.class
        ).getQueryString());
        List<DatasetHGMDSQL> simVariants = qquery.setParameter("chr", variant.getChromosomeName()).setParameter("pos", variant.getChromosomePos()).setParameter("ref", variant.getReferenceBase()).setParameter("alt", variant.getAlternateBase()).getResultList();
        return simVariants;
    }

    public List<VariantSQL> getIdenticalVariants(VariantSQL variant) {
        TypedQuery<VariantSQL> qquery = emProvider.get().createQuery("Select ds FROM VariantSQL ds where ds.chromosomeName = :chr AND ds.chromosomePos = :pos AND ds.id != :id AND ds.referenceBase = :ref AND ds.alternateBase = :alt", VariantSQL.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
        System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
        List<VariantSQL> simVariants = qquery.setParameter("chr", variant.getChromosomeName()).setParameter("pos", variant.getChromosomePos()).setParameter("id", variant.getId()).setParameter("ref", variant.getReferenceBase()).setParameter("alt", variant.getAlternateBase()).getResultList();
        return simVariants;
    }

    public boolean remove(MatchSQL match) {
        emProvider.get().remove(match);
        return true;
    }

    public List<DatasetHGMDSQL> getSimilarHGMDVariantsByGene(VariantSQL variant) {
        emProvider.get().refresh(variant);
        //genes
        Set<Integer> geneids = new HashSet<>();
        for (VariantEffectSQL vareff : variant.getVariantEffects()) {
            GeneSQL gene = vareff.getTranscript().getGene();
            geneids.add(gene.getId());
        }

        //query
        String geneString = getSetString(true, geneids, "g.id", true);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "genesimString; " + geneString);

        TypedQuery<DatasetHGMDSQL> qquery = emProvider.get().createQuery("Select distinct ds FROM DatasetHGMDSQL ds join ds.transcript t join t.gene g WHERE " + geneString, DatasetHGMDSQL.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
        System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
        List<DatasetHGMDSQL> simVariants = qquery.getResultList();
        return simVariants;
    }

    public List<VariantSQL> getSimilarVWVariantsByGene(VariantSQL variant) {
        emProvider.get().refresh(variant);
        //genes
        Set<Integer> geneids = new HashSet<>();
        for (VariantEffectSQL vareff : variant.getVariantEffects()) {
            GeneSQL gene = vareff.getTranscript().getGene();
            geneids.add(gene.getId());
        }

        //query
        String geneString = getSetString(true, geneids, "g.id", true);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "genesimString; " + geneString);

        TypedQuery<VariantSQL> qquery = emProvider.get().createQuery("Select distinct ds FROM VariantSQL ds join ds.variantEffects v join v.transcript t join t.gene g WHERE " + geneString + " AND ds.dataset_vw != :dataset_vw", VariantSQL.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
        System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
        List<VariantSQL> simVariants = qquery.setParameter("dataset_vw", variant.getDataset()).getResultList();
        return simVariants;
    }

    public List<DatasetHGMDSQL> getSimilarHGMDVariantsByFamily(VariantSQL queryVariant) {
        emProvider.get().refresh(queryVariant);
        List<DatasetHGMDSQL> simVariants = new ArrayList<DatasetHGMDSQL>();

        //family
        Set<Integer> familyIds = new HashSet<>();
        for (VariantEffectSQL vareff : queryVariant.getVariantEffects()) {
            Set<FamilySQL> families = vareff.getTranscript().getGene().getFamilies();
            if (families != null) {
                for (FamilySQL family : families) {
                    familyIds.add(family.getId());
                }
            }
        }

        //genes
        Set<Integer> geneids = new HashSet<>();
        for (VariantEffectSQL vareff : queryVariant.getVariantEffects()) {
            GeneSQL gene = vareff.getTranscript().getGene();
            geneids.add(gene.getId());
        }

        //query
//        TypedQuery<DatasetHGMDSQL> qquery = _em.createQuery("SELECT distinct ds FROM DatasetHGMDSQL d JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f JOIN WHERE g.id " + getInORNotIn(geneBool) + " (:geneids) AND p.id " + getInORNotIn(pathwayBool) + " (:pathwayids)  AND f.id " + getInORNotIn(familyBool) + " (:familyids)", DatasetHGMDSQL.class);
//        TypedQuery<DatasetHGMDSQL> qquery = _em.createQuery("SELECT distinct ds FROM DatasetHGMDSQL d JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f JOIN WHERE " + getSetString(geneBool, geneids, "g.id", true) + getSetString(pathwayBool, pathwayIds, "p.id", false) + getSetString(familyBool, familyIds, "f.id", false), DatasetHGMDSQL.class);
        //query
        String geneString = getSetString(false, geneids, "g.id", true);
        String familyString = getSetString(true, familyIds, "f.id", false);

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "famSimString; " + geneString + familyString);
        if (!familyIds.isEmpty()) {
            TypedQuery<DatasetHGMDSQL> qquery = emProvider.get().createQuery("Select distinct ds FROM DatasetHGMDSQL ds JOIN ds.transcript  t JOIN t.gene g JOIN g.families f WHERE " + geneString + familyString, DatasetHGMDSQL.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
            System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
            simVariants = qquery.getResultList();
        }
        return simVariants;
    }

    public List<VariantSQL> getSimilarVWVariantsByFamily(VariantSQL queryVariant) {
        emProvider.get().refresh(queryVariant);
        List<VariantSQL> simVariants = new ArrayList<VariantSQL>();

        //family
        Set<Integer> familyIds = new HashSet<>();
        for (VariantEffectSQL vareff : queryVariant.getVariantEffects()) {
            Set<FamilySQL> families = vareff.getTranscript().getGene().getFamilies();
            if (families != null) {
                for (FamilySQL family : families) {
                    familyIds.add(family.getId());
                }
            }
        }

        //genes
        Set<Integer> geneids = new HashSet<>();
        for (VariantEffectSQL vareff : queryVariant.getVariantEffects()) {
            GeneSQL gene = vareff.getTranscript().getGene();
            geneids.add(gene.getId());
        }

        //query
//        TypedQuery<DatasetHGMDSQL> qquery = _em.createQuery("SELECT distinct ds FROM DatasetHGMDSQL d JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f JOIN WHERE g.id " + getInORNotIn(geneBool) + " (:geneids) AND p.id " + getInORNotIn(pathwayBool) + " (:pathwayids)  AND f.id " + getInORNotIn(familyBool) + " (:familyids)", DatasetHGMDSQL.class);
//        TypedQuery<DatasetHGMDSQL> qquery = _em.createQuery("SELECT distinct ds FROM DatasetHGMDSQL d JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f JOIN WHERE " + getSetString(geneBool, geneids, "g.id", true) + getSetString(pathwayBool, pathwayIds, "p.id", false) + getSetString(familyBool, familyIds, "f.id", false), DatasetHGMDSQL.class);
        //query
        String geneString = getSetString(false, geneids, "g.id", true);
        String familyString = getSetString(true, familyIds, "f.id", false);

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "famSimString; " + geneString + familyString);
        if (!familyIds.isEmpty()) {
            TypedQuery<VariantSQL> qquery = emProvider.get().createQuery("Select distinct ds FROM VariantSQL ds JOIN ds.variantEffects veff JOIN veff.transcript t JOIN t.gene g JOIN g.families f WHERE " + geneString + familyString + " AND ds.dataset_vw != :dataset_vw", VariantSQL.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
            System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
            simVariants = qquery.setParameter("dataset_vw", queryVariant.getDataset()).getResultList();
        }
        return simVariants;
    }

    public boolean persistVWMatch(MatchSQL match) {
        //check if there is another matchgrp with this 2 matches
        List<MatchVariantSQL> variantMatches = new ArrayList<>(match.getVariants());
        if (variantMatches.size() != 2) {
            System.out.println("Not a variant pair");
            return false;
        }
        match.setNotified(false);
        match.setCreationTimestamp(new DateTime());
        emProvider.get().persist(match);

        for (MatchVariantSQL matchVariant : match.getVariants()) {
            emProvider.get().persist(matchVariant);
        }
        return true;
    }

    public List<MatchVariantSQL> getNotNotifiedMatchesByUser(UserSQL user) {
        TypedQuery<MatchVariantSQL> qquery = emProvider.get().createQuery("SELECT m FROM MatchVariantSQL m  WHERE m.user = :user and m.acknowledged = :notified", MatchVariantSQL.class);
        List<MatchVariantSQL> matches = qquery.setParameter("user", user).setParameter("notified", false).getResultList();
        for (MatchVariantSQL curMatch : matches) {
            emProvider.get().refresh(curMatch);
        }
        return matches;
    }

    public void update(MatchVariantSQL varMatch) {
        emProvider.get().merge(varMatch);
//        emProvider.get().refresh(varMatch);
    }

    List<MatchVariantSQL> getMatchesInInterval(UserSQL user, DateTime lastReport, DateTime nextCreationDate) {
        TypedQuery<MatchVariantSQL> qquery = emProvider.get().createQuery("SELECT m FROM MatchVariantSQL m  WHERE m.user = :user and m.timestamp > :lastReport and m.timestamp < :nextCreation", MatchVariantSQL.class);
        List<MatchVariantSQL> matches = qquery.setParameter("user", user).setParameter("lastReport", lastReport).setParameter("nextCreation", nextCreationDate).getResultList();
        return matches;
    }

    MatchVariantSQL getMatchVariant(Long curMatch) {
        return emProvider.get().find(MatchVariantSQL.class, curMatch);
    }

    public List<MatchSQL> getVarWatchMatchesByVariantId(Long id) {
        TypedQuery<MatchVariantSQL> qquery = emProvider.get().createQuery("SELECT m FROM MatchVariantSQL m  WHERE m.variantId = :variantId", MatchVariantSQL.class);
        List<MatchVariantSQL> matchVariants = qquery.setParameter("variantId", id).getResultList();
        List<MatchSQL> matches = new ArrayList<>();
        for (MatchVariantSQL curMatch : matchVariants) {
            emProvider.get().refresh(curMatch);
            MatchSQL match = curMatch.getMatch();
            emProvider.get().refresh(match);
            if (curMatch.getDatabase() != null && curMatch.getDatabase().getName().equals("VarWatch")) {
                matches.add(match);
            }
        }
        return matches;
    }

    public MatchSQL getMatchById(Long matchId) {
        return emProvider.get().find(MatchSQL.class, matchId);
    }

    public List<VariantSQL> getVariantsWithSameAminoAcidExchange(VariantSQL variant) {
        Set<VariantEffectSQL> variantEffects = variant.getVariantEffects();
        Map<Long, VariantSQL> similarVariants = new HashMap<>();
        for (VariantEffectSQL curVariantEff : variantEffects) {
            if (curVariantEff == null) {
                continue;
            }
            TypedQuery<VariantSQL> qquery = emProvider.get().createQuery("Select distinct ds FROM VariantSQL ds JOIN ds.variantEffects veff WHERE veff.protein_start <= :proteinend and veff.protein_end >= :proteinstart and ds.dataset_vw != :dataset", VariantSQL.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
            System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
            List<VariantSQL> simVariants = qquery.setParameter("proteinstart", curVariantEff.getProtein_start()).setParameter("proteinend", curVariantEff.getProtein_end()).setParameter("dataset", variant.getDataset()).getResultList();
            System.out.println(simVariants.size() + " amino acid variants");
            for (VariantSQL curMatchedVar : simVariants) {
                similarVariants.put(curMatchedVar.getId(), curMatchedVar);
            }
        }
        return new ArrayList<>(similarVariants.values());
    }

    List<DatasetHGMDSQL> getHgmdVariantsWithSameAminoAcidExchange(VariantSQL variant) {
        Set<VariantEffectSQL> variantEffects = variant.getVariantEffects();
        Map<Long, DatasetHGMDSQL> similarVariants = new HashMap<>();
        for (VariantEffectSQL curVariantEff : variantEffects) {
            if (curVariantEff == null) {
                continue;
            }
            TypedQuery<DatasetHGMDSQL> qquery = emProvider.get().createQuery("Select distinct ds FROM DatasetHGMDSQL ds WHERE ds.protein_start <= :proteinend and ds.protein_end >= :proteinstart", DatasetHGMDSQL.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
            System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
            List<DatasetHGMDSQL> simVariants = qquery.setParameter("proteinstart", curVariantEff.getProtein_start()).setParameter("proteinend", curVariantEff.getProtein_end()).getResultList();
            System.out.println(simVariants.size() + " amino acid variants");
            for (DatasetHGMDSQL curMatchedVar : simVariants) {
                similarVariants.put(curMatchedVar.getId(), curMatchedVar);
            }
        }
        return new ArrayList<>(similarVariants.values());
    }

    public boolean isDuplicatedMatch(MatchSQL curMatch) {
        if (curMatch.getVariants() == null || curMatch.getVariants().size() != 2) {
            return false;
        }
        List<MatchVariantSQL> matchedVariants = new ArrayList<>(curMatch.getVariants());
        MatchVariantSQL matchVariant1 = matchedVariants.get(0);
        MatchVariantSQL matchVariant2 = matchedVariants.get(1);
        Query q = emProvider.get().createNativeQuery("Select m2v1.id as m2v1id,m2v2.id as m2v2id from match2variant m2v1 left join match2variant m2v2 on m2v1.match_id=m2v2.match_id "
                + "where m2v1.database_id= :db1 and m2v2.database_id= :db2 "
                + "and (m2v1.variant_id=:variantId1 or (ISNULL(m2v1.variant_id) and ISNULL(:variantId1))) "
                + "and (m2v2.variant_id=:variantId2 or (ISNULL(m2v2.variant_id) and ISNULL(:variantId2)))");
        q.setParameter("variantId1", matchVariant1.getVariantId()).setParameter("variantId2", matchVariant2.getVariantId());
        q.setParameter("db1", matchVariant1.getDatabase().getId()).setParameter("db2", matchVariant2.getDatabase().getId());
        System.out.println(q.unwrap(org.hibernate.Query.class).getQueryString());
        List<Object[]> matches = q.getResultList();
        System.out.println(matches.size());
        return !matches.isEmpty();
    }

    public static String getSetString(boolean bool, Set<Integer> ids, String inIdent, boolean first) {
        String returnString = "";
        if (!ids.isEmpty()) {
            if (!first) {
                returnString += " AND ";
            }
            returnString += inIdent + " " + getInORNotIn(bool) + " (";
            Iterator<Integer> iter = ids.iterator();
            if (iter.hasNext()) {
                Integer id = iter.next();
                returnString += id;
                while (iter.hasNext()) {
                    id = iter.next();
                    returnString += "," + id;
                }
            }
            returnString += ")";
        }
        return returnString;
    }

    private static String getInORNotIn(boolean bool) {
        if (bool) {
            return "IN";
        } else {
            return "NOT IN";
        }
    }
}
