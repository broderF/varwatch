/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.data.matching;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.dataset.DatasetHGMD;
import com.ikmb.core.data.family.GeneFamily;
import com.ikmb.core.data.gene.Gene;
import com.ikmb.core.data.matching.Match;
import com.ikmb.core.data.matching.MatchVariant;
import com.ikmb.core.data.matching.MatchVariantDao;
import com.ikmb.core.data.pathway.Pathway;
import com.ikmb.core.data.transcript.Transcript;
import com.ikmb.core.data.variant.Variant;
//import static com.ikmb.varwatchsql.databasehelper.MatchingDatabaseHelper.getSetString;
import com.ikmb.core.data.variant.VariantDataManager;
import com.ikmb.core.data.varianteffect.VariantEffect;
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
public class MatchVariantDaoSQL implements MatchVariantDao {

    @Inject
    private Provider<EntityManager> emProvider;

    @Inject
    private VariantDataManager variantDm;

    public Match get(Long matchStatusID) {
        return emProvider.get().find(Match.class, matchStatusID);
    }

    public List<Variant> getSimilarVariants(Variant queryVariant, boolean geneBool, boolean pathwayBool, boolean familyBool) {
        List<Variant> simVariants = new ArrayList<>();
        //pathways
        Set<Integer> pathwayIds = new HashSet<>();
        for (VariantEffect vareff : queryVariant.getVariantEffects()) {
//            Hibernate.initialize(vareff);
            Transcript transcript = vareff.getTranscript();
            Gene gene = transcript.getGene();
            TypedQuery<Gene> qquery = emProvider.get().createQuery("SELECT g FROM Gene g  WHERE id = :id", Gene.class);
            gene = qquery.setParameter("id", gene.getId()).getSingleResult();
            Set<Pathway> pathways = gene.getPathways();
            if (pathways != null) {
                for (Pathway pathway : pathways) {
                    pathwayIds.add(pathway.getId());
                }
            }
        }

        //family
        Set<Integer> familyIds = new HashSet<>();
        for (VariantEffect vareff : queryVariant.getVariantEffects()) {
            Hibernate.initialize(vareff);
            Set<GeneFamily> families = vareff.getTranscript().getGene().getFamilies();
            if (families != null) {
                for (GeneFamily family : families) {
                    familyIds.add(family.getId());
                }
            }
        }

        //genes
        Set<Integer> geneids = new HashSet<>();
        for (VariantEffect vareff : queryVariant.getVariantEffects()) {
            Hibernate.initialize(vareff);
            Gene gene = vareff.getTranscript().getGene();
            geneids.add(gene.getId());
        }

        //query
        String geneString = getSetString(geneBool, geneids, "g.id", true);
        String pathwayString = getSetString(pathwayBool, pathwayIds, "p.id", false);
        String familyString = getSetString(familyBool, familyIds, "f.id", false);
        Integer getNrOfIns = getNrOfIns(geneString, geneBool, pathwayString, pathwayBool, familyString, familyBool);
        if (getNrOfIns > 0) {
            TypedQuery<Variant> qquery = emProvider.get().createQuery("SELECT distinct v FROM VariantEffect veff JOIN veff.variant v JOIN v.dataset_vw ds JOIN veff.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f WHERE " + geneString + pathwayString + familyString + " AND ds.completed = true AND v.id != :variantid", Variant.class);
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

    public List<Match> persistMatches(List<Match> matches) {
        for (Match match : matches) {
            match.setNotified(false);
            match.setCreationTimestamp(new DateTime());
            emProvider.get().persist(match);

            for (MatchVariant matchVariant : match.getVariants()) {
                emProvider.get().persist(matchVariant);
            }
        }
        return matches;
    }

    public List<DatasetHGMD> getSimilarHGMDVariants(Variant queryVariant, boolean geneBool, boolean pathwayBool, boolean familyBool) {
        List<DatasetHGMD> simVariants = new ArrayList<DatasetHGMD>();

        //pathways
        Set<Integer> pathwayIds = new HashSet<>();
        for (VariantEffect vareff : queryVariant.getVariantEffects()) {
            Set<Pathway> pathways = vareff.getTranscript().getGene().getPathways();
            if (pathways != null) {
                for (Pathway pathway : pathways) {
                    pathwayIds.add(pathway.getId());
                }
            }
        }

        //family
        Set<Integer> familyIds = new HashSet<>();
        for (VariantEffect vareff : queryVariant.getVariantEffects()) {
            Set<GeneFamily> families = vareff.getTranscript().getGene().getFamilies();
            if (families != null) {
                for (GeneFamily family : families) {
                    familyIds.add(family.getId());
                }
            }
        }

        //genes
        Set<Integer> geneids = new HashSet<>();
        for (VariantEffect vareff : queryVariant.getVariantEffects()) {
            Gene gene = vareff.getTranscript().getGene();
            geneids.add(gene.getId());
        }

        //query
//        TypedQuery<DatasetHGMD> qquery = _em.createQuery("SELECT distinct ds FROM DatasetHGMD d JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f JOIN WHERE g.id " + getInORNotIn(geneBool) + " (:geneids) AND p.id " + getInORNotIn(pathwayBool) + " (:pathwayids)  AND f.id " + getInORNotIn(familyBool) + " (:familyids)", DatasetHGMD.class);
//        TypedQuery<DatasetHGMD> qquery = _em.createQuery("SELECT distinct ds FROM DatasetHGMD d JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f JOIN WHERE " + getSetString(geneBool, geneids, "g.id", true) + getSetString(pathwayBool, pathwayIds, "p.id", false) + getSetString(familyBool, familyIds, "f.id", false), DatasetHGMD.class);
        //query
        String geneString = getSetString(geneBool, geneids, "g.id", true);
        String pathwayString = getSetString(pathwayBool, pathwayIds, "p.id", false);
        String familyString = getSetString(familyBool, familyIds, "f.id", false);
        Integer getNrOfIns = getNrOfIns(geneString, geneBool, pathwayString, pathwayBool, familyString, familyBool);

        Boolean pathwaySkip = pathwayBool && pathwayString.equals("");
        Boolean famSkip = familyBool && familyString.equals("");

        if (getNrOfIns > 0 && (geneBool || !(pathwaySkip || famSkip))) {
            TypedQuery<DatasetHGMD> qquery = emProvider.get().createQuery("Select distinct ds FROM DatasetHGMD ds JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f WHERE " + geneString + pathwayString + familyString, DatasetHGMD.class
            );
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
            System.out.println(qquery.unwrap(org.hibernate.Query.class
            ).getQueryString());
            simVariants = qquery.getResultList();
        }
        return simVariants;
    }

    public List<DatasetHGMD> getIdenticalHGMDVariants(Variant variant) {
        TypedQuery<DatasetHGMD> qquery = emProvider.get().createQuery("Select ds FROM DatasetHGMD ds where ds.chrName = :chr AND ds.chrPos = :pos AND ds.refBase = :ref AND ds.altBase = :alt", DatasetHGMD.class
        );
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
        System.out.println(qquery.unwrap(org.hibernate.Query.class
        ).getQueryString());
        List<DatasetHGMD> simVariants = qquery.setParameter("chr", variant.getChromosomeName()).setParameter("pos", variant.getChromosomePos()).setParameter("ref", variant.getReferenceBase()).setParameter("alt", variant.getAlternateBase()).getResultList();
        return simVariants;
    }

    public List<Variant> getIdenticalVariants(Variant variant) {
        TypedQuery<Variant> qquery = emProvider.get().createQuery("Select ds FROM Variant ds where ds.chromosomeName = :chr AND ds.chromosomePos = :pos AND ds.id != :id AND ds.referenceBase = :ref AND ds.alternateBase = :alt", Variant.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
        System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
        List<Variant> simVariants = qquery.setParameter("chr", variant.getChromosomeName()).setParameter("pos", variant.getChromosomePos()).setParameter("id", variant.getId()).setParameter("ref", variant.getReferenceBase()).setParameter("alt", variant.getAlternateBase()).getResultList();
        return simVariants;
    }

    public boolean remove(Match match) {
        emProvider.get().remove(match);
        return true;
    }

    public List<DatasetHGMD> getSimilarHGMDVariantsByGene(Variant variant) {
        emProvider.get().refresh(variant);
        //genes
        Set<Integer> geneids = new HashSet<>();
        for (VariantEffect vareff : variant.getVariantEffects()) {
            Gene gene = vareff.getTranscript().getGene();
            geneids.add(gene.getId());
        }

        //query
        String geneString = getSetString(true, geneids, "g.id", true);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "genesimString; " + geneString);

        TypedQuery<DatasetHGMD> qquery = emProvider.get().createQuery("Select distinct ds FROM DatasetHGMD ds join ds.transcript t join t.gene g WHERE " + geneString, DatasetHGMD.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
        System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
        List<DatasetHGMD> simVariants = qquery.getResultList();
        return simVariants;
    }

    public List<Variant> getSimilarVWVariantsByGene(Variant variant) {
        emProvider.get().refresh(variant);
        //genes
        Set<Integer> geneids = new HashSet<>();
        for (VariantEffect vareff : variant.getVariantEffects()) {
            Gene gene = vareff.getTranscript().getGene();
            geneids.add(gene.getId());
        }

        //query
        String geneString = getSetString(true, geneids, "g.id", true);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "genesimString; " + geneString);

        TypedQuery<Variant> qquery = emProvider.get().createQuery("Select distinct ds FROM Variant ds join ds.variantEffects v join v.transcript t join t.gene g WHERE " + geneString + " AND ds.dataset_vw != :dataset_vw", Variant.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
        System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
        List<Variant> simVariants = qquery.setParameter("dataset_vw", variant.getDataset()).getResultList();
        return simVariants;
    }

    public List<DatasetHGMD> getSimilarHGMDVariantsByFamily(Variant queryVariant) {
        emProvider.get().refresh(queryVariant);
        List<DatasetHGMD> simVariants = new ArrayList<DatasetHGMD>();

        //family
        Set<Integer> familyIds = new HashSet<>();
        for (VariantEffect vareff : queryVariant.getVariantEffects()) {
            Set<GeneFamily> families = vareff.getTranscript().getGene().getFamilies();
            if (families != null) {
                for (GeneFamily family : families) {
                    familyIds.add(family.getId());
                }
            }
        }

        //genes
        Set<Integer> geneids = new HashSet<>();
        for (VariantEffect vareff : queryVariant.getVariantEffects()) {
            Gene gene = vareff.getTranscript().getGene();
            geneids.add(gene.getId());
        }

        //query
//        TypedQuery<DatasetHGMD> qquery = _em.createQuery("SELECT distinct ds FROM DatasetHGMD d JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f JOIN WHERE g.id " + getInORNotIn(geneBool) + " (:geneids) AND p.id " + getInORNotIn(pathwayBool) + " (:pathwayids)  AND f.id " + getInORNotIn(familyBool) + " (:familyids)", DatasetHGMD.class);
//        TypedQuery<DatasetHGMD> qquery = _em.createQuery("SELECT distinct ds FROM DatasetHGMD d JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f JOIN WHERE " + getSetString(geneBool, geneids, "g.id", true) + getSetString(pathwayBool, pathwayIds, "p.id", false) + getSetString(familyBool, familyIds, "f.id", false), DatasetHGMD.class);
        //query
        String geneString = getSetString(false, geneids, "g.id", true);
        String familyString = getSetString(true, familyIds, "f.id", false);

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "famSimString; " + geneString + familyString);
        if (!familyIds.isEmpty()) {
            TypedQuery<DatasetHGMD> qquery = emProvider.get().createQuery("Select distinct ds FROM DatasetHGMD ds JOIN ds.transcript  t JOIN t.gene g JOIN g.families f WHERE " + geneString + familyString, DatasetHGMD.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
            System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
            simVariants = qquery.getResultList();
        }
        return simVariants;
    }

    public List<Variant> getSimilarVWVariantsByFamily(Variant queryVariant) {
        emProvider.get().refresh(queryVariant);
        List<Variant> simVariants = new ArrayList<Variant>();

        //family
        Set<Integer> familyIds = new HashSet<>();
        for (VariantEffect vareff : queryVariant.getVariantEffects()) {
            Set<GeneFamily> families = vareff.getTranscript().getGene().getFamilies();
            if (families != null) {
                for (GeneFamily family : families) {
                    familyIds.add(family.getId());
                }
            }
        }

        //genes
        Set<Integer> geneids = new HashSet<>();
        for (VariantEffect vareff : queryVariant.getVariantEffects()) {
            Gene gene = vareff.getTranscript().getGene();
            geneids.add(gene.getId());
        }

        //query
//        TypedQuery<DatasetHGMD> qquery = _em.createQuery("SELECT distinct ds FROM DatasetHGMD d JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f JOIN WHERE g.id " + getInORNotIn(geneBool) + " (:geneids) AND p.id " + getInORNotIn(pathwayBool) + " (:pathwayids)  AND f.id " + getInORNotIn(familyBool) + " (:familyids)", DatasetHGMD.class);
//        TypedQuery<DatasetHGMD> qquery = _em.createQuery("SELECT distinct ds FROM DatasetHGMD d JOIN ds.transcript  t JOIN t.gene g Join g.pathways p JOIN g.families f JOIN WHERE " + getSetString(geneBool, geneids, "g.id", true) + getSetString(pathwayBool, pathwayIds, "p.id", false) + getSetString(familyBool, familyIds, "f.id", false), DatasetHGMD.class);
        //query
        String geneString = getSetString(false, geneids, "g.id", true);
        String familyString = getSetString(true, familyIds, "f.id", false);

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "famSimString; " + geneString + familyString);
        if (!familyIds.isEmpty()) {
            TypedQuery<Variant> qquery = emProvider.get().createQuery("Select distinct ds FROM Variant ds JOIN ds.variantEffects veff JOIN veff.transcript t JOIN t.gene g JOIN g.families f WHERE " + geneString + familyString + " AND ds.dataset_vw != :dataset_vw", Variant.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
            System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
            simVariants = qquery.setParameter("dataset_vw", queryVariant.getDataset()).getResultList();
        }
        return simVariants;
    }

    public boolean persistVWMatch(Match match) {
        //check if there is another matchgrp with this 2 matches
        List<MatchVariant> variantMatches = new ArrayList<>(match.getVariants());
        if (variantMatches.size() != 2) {
            System.out.println("Not a variant pair");
            return false;
        }
        match.setNotified(false);
        match.setCreationTimestamp(new DateTime());
        emProvider.get().persist(match);

        for (MatchVariant matchVariant : match.getVariants()) {
            emProvider.get().persist(matchVariant);
        }
        return true;
    }

    public List<MatchVariant> getNotNotifiedMatchesByUser(User user) {
        TypedQuery<MatchVariant> qquery = emProvider.get().createQuery("SELECT m FROM MatchVariant m  WHERE m.user = :user and m.acknowledged = :notified", MatchVariant.class);
        List<MatchVariant> matches = qquery.setParameter("user", user).setParameter("notified", false).getResultList();
        for (MatchVariant curMatch : matches) {
            emProvider.get().refresh(curMatch);
        }
        return matches;
    }

    public void update(MatchVariant varMatch) {
        emProvider.get().merge(varMatch);
//        emProvider.get().refresh(varMatch);
    }

    public List<MatchVariant> getMatchesInInterval(User user, DateTime lastReport, DateTime nextCreationDate) {
        TypedQuery<MatchVariant> qquery = emProvider.get().createQuery("SELECT m FROM MatchVariant m  WHERE m.user = :user and m.timestamp > :lastReport and m.timestamp < :nextCreation", MatchVariant.class);
        List<MatchVariant> matches = qquery.setParameter("user", user).setParameter("lastReport", lastReport).setParameter("nextCreation", nextCreationDate).getResultList();
        return matches;
    }

    public MatchVariant getMatchVariant(Long curMatch) {
        return emProvider.get().find(MatchVariant.class, curMatch);
    }

    public List<Match> getVarWatchMatchesByVariantId(Long id) {
        TypedQuery<MatchVariant> qquery = emProvider.get().createQuery("SELECT m FROM MatchVariant m  WHERE m.variantId = :variantId", MatchVariant.class);
        List<MatchVariant> matchVariants = qquery.setParameter("variantId", id).getResultList();
        List<Match> matches = new ArrayList<>();
        for (MatchVariant curMatch : matchVariants) {
            emProvider.get().refresh(curMatch);
            Match match = curMatch.getMatch();
            emProvider.get().refresh(match);
            if (curMatch.getDatabase() != null && curMatch.getDatabase().getName().equals("VarWatch")) {
                matches.add(match);
            }
        }
        return matches;
    }

    public Match getMatchById(Long matchId) {
        return emProvider.get().find(Match.class, matchId);
    }

    public List<Variant> getVariantsWithSameAminoAcidExchange(Variant variant) {
        Set<VariantEffect> variantEffects = variant.getVariantEffects();
        Map<Long, Variant> similarVariants = new HashMap<>();
        for (VariantEffect curVariantEff : variantEffects) {
            if (curVariantEff == null) {
                continue;
            }
            TypedQuery<Variant> qquery = emProvider.get().createQuery("Select distinct ds FROM Variant ds JOIN ds.variantEffects veff WHERE veff.protein_start <= :proteinend and veff.protein_end >= :proteinstart and ds.dataset_vw != :dataset", Variant.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
            System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
            List<Variant> simVariants = qquery.setParameter("proteinstart", curVariantEff.getProtein_start()).setParameter("proteinend", curVariantEff.getProtein_end()).setParameter("dataset", variant.getDataset()).getResultList();
            System.out.println(simVariants.size() + " amino acid variants");
            for (Variant curMatchedVar : simVariants) {
                similarVariants.put(curMatchedVar.getId(), curMatchedVar);
            }
        }
        return new ArrayList<>(similarVariants.values());
    }

   public List<DatasetHGMD> getHgmdVariantsWithSameAminoAcidExchange(Variant variant) {
        Set<VariantEffect> variantEffects = variant.getVariantEffects();
        Map<Long, DatasetHGMD> similarVariants = new HashMap<>();
        for (VariantEffect curVariantEff : variantEffects) {
            if (curVariantEff == null) {
                continue;
            }
            TypedQuery<DatasetHGMD> qquery = emProvider.get().createQuery("Select distinct ds FROM DatasetHGMD ds WHERE ds.protein_start <= :proteinend and ds.protein_end >= :proteinstart", DatasetHGMD.class);
//        qquery.setParameter("pathwayids", pathwayIds).setParameter("familyids", familyIds).setParameter("geneids", geneids).setParameter("variantid", queryVariant.getId());
            System.out.println(qquery.unwrap(org.hibernate.Query.class).getQueryString());
            List<DatasetHGMD> simVariants = qquery.setParameter("proteinstart", curVariantEff.getProtein_start()).setParameter("proteinend", curVariantEff.getProtein_end()).getResultList();
            System.out.println(simVariants.size() + " amino acid variants");
            for (DatasetHGMD curMatchedVar : simVariants) {
                similarVariants.put(curMatchedVar.getId(), curMatchedVar);
            }
        }
        return new ArrayList<>(similarVariants.values());
    }

    public boolean isDuplicatedMatch(Match curMatch) {
        if (curMatch.getVariants() == null || curMatch.getVariants().size() != 2) {
            return false;
        }
        List<MatchVariant> matchedVariants = new ArrayList<>(curMatch.getVariants());
        MatchVariant matchVariant1 = matchedVariants.get(0);
        MatchVariant matchVariant2 = matchedVariants.get(1);
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
