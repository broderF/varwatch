/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching;

import com.google.inject.Inject;
import com.ikmb.WorkerLauncher;
import com.ikmb.core.auth.user.User;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.hpo.Phenotype;
import com.ikmb.core.data.hpo.PhenotypeDataManager;
import com.ikmb.core.data.matching.Match;
import com.ikmb.core.data.matching.MatchVariant;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.data.reference_db.RefDatabase;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantStatusManager;
import com.ikmb.core.data.varianteffect.VariantEffect;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author broder
 */
public class VarWatchScreenerNew implements DatabaseScreener {

    private List<Match> _matches = new ArrayList<Match>();
    private DatasetVW _dataset;
    private RefDatabase _database;
    private HashMap<String, HPOPath> pathList;
    private RefDatabase vwDatabase;

    @Inject
    private DatasetManager datasetManager;

    @Inject
    private PhenotypeDataManager phenotypeManager;

    @Inject
    private MatchVariantDataManager matchDataManager;

    @Inject
    private VariantStatusManager variantStatusManager;

    @Override
    public void setVWDatabase(RefDatabase vwDatabase) {
        this.vwDatabase = vwDatabase;
    }

    @Override
    public void run() {
        loadHPODistances();

        for (Variant variant : _dataset.getVariants()) {
            findMatches(variant);
        }

//        List<MatchGroupSQL> persistMatches = new ArrayList<>();
//        for (MatchGroupSQL matchgrp : getMatches()) {
//            boolean persist = matchDataManager.persistVWMatch(matchgrp);
//            if (persist) {
//                persistMatches.add(matchgrp);
//            }
//        }
//        variantStatusManager.addMatches(persistMatches, VariantStatusBuilder.VariantStatusMessage.MATCHED_TO_VARWATCH);
    }

    private void findMatches(Variant vwVariant) {
        System.out.println("query variant: "+vwVariant.toString());
        List<Variant> similarGeneVariants = matchDataManager.getSimilarVWByGene(vwVariant);
        if (similarGeneVariants.isEmpty()) {
            System.out.println("no matches found");
            return;
        }

        Set<Match> matches = createAminoAcidMatches(similarGeneVariants, vwVariant);
        if (!matches.isEmpty()) {
            _matches.addAll(matches);
            return;
        }

        boolean isLof = false;
        for (VariantEffect vareff : vwVariant.getVariantEffects()) {
            String loftee = vareff.getLoftee();
            if (loftee != null && loftee.equals("HC")) {
                isLof = true;
                break;
            }
        }

        List<Variant> lofMatches = new ArrayList<>();
        if (isLof) {
            lofMatches = getLofMatches(similarGeneVariants);
        }

        if (!lofMatches.isEmpty()) {
            matches = createMatches(lofMatches, vwVariant, VarWatchScreenerNew.MatchType.lof);
            _matches.addAll(matches);
            return;
        }

        matches = createMatches(similarGeneVariants, vwVariant, VarWatchScreenerNew.MatchType.gene);
        _matches.addAll(matches);
    }

    private Double getHPOSetDistance(Variant variant, Variant fixVariant) {
        Set<Phenotype> qhpos = variant.getDataset().getPhenotypes();
        Set<Phenotype> thpos = fixVariant.getDataset().getPhenotypes();

        Double maxSim = 0d;
        for (Phenotype qhpo : qhpos) {
            Double currentMaxSimilarity = 0d;
            for (Phenotype thpo : thpos) {
                String hpoTerm1 = qhpo.getPhenotype().getIdentifier();
                String hpoTerm2 = thpo.getPhenotype().getIdentifier();
                Double currentSimilartiy = getHPODistance(hpoTerm1, hpoTerm2);
                if (currentSimilartiy > currentMaxSimilarity) {
                    currentMaxSimilarity = currentSimilartiy;
                }
            }
            maxSim += currentMaxSimilarity;
        }

        for (Phenotype thpo : thpos) {
            Double currentMaxSimilarity = 0d;
            for (Phenotype qhpo : qhpos) {
                String hpoTerm1 = qhpo.getPhenotype().getIdentifier();
                String hpoTerm2 = thpo.getPhenotype().getIdentifier();
                Double currentSimilartiy = getHPODistance(hpoTerm2, hpoTerm1);
                if (currentSimilartiy > currentMaxSimilarity) {
                    currentMaxSimilarity = currentSimilartiy;
                }
            }
            maxSim += currentMaxSimilarity;
        }

        return maxSim / ((double) (qhpos.size() + thpos.size()));
    }

    @Override
    public List<Match> getMatches() {
        return _matches;
    }

    @Override
    public void initialize(RefDatabase database, DatasetVW dataset) {
        _database = database;
        _dataset = dataset;
    }

    @Override
    public DatasetVW getDataset() {
        return _dataset;
    }

    @Override
    public RefDatabase getDatabase() {
        return _database;
    }

    private Set<Match> createAminoAcidMatches(List<Variant> similarVariants, Variant variant) {
        Set<Match> matches = new HashSet<>();
        System.out.println(similarVariants.size() +" variants in identical gene gound");
        for (Variant currentVar : similarVariants) {
            System.out.println("current comparison with variant "+currentVar.toString());
            Match match = new Match();
            MatchType matchingType = getTypeFromMatch(variant, currentVar);
            System.out.println(matchingType.name());
            if (matchingType.equals(MatchType.no_match)) {
                continue;
            }
            match.setMatch_type(matchingType.name());
            Double hpoDist = getHPOSetDistance(variant, currentVar);
            match.setHpoDist(hpoDist);
            match.setDatabase(_database);
            if (currentVar.getChromosomeName().equals(variant.getChromosomeName()) && currentVar.getChromosomePos().equals(variant.getChromosomePos())) {
                match.setIdentical(true);
            } else {
                match.setIdentical(false);
            }

            MatchVariant vwVariant = new MatchVariant();
            User matchedUser = currentVar.getDataset().getUser();
            vwVariant.setDatabase(_database);
            vwVariant.setUser(matchedUser);
            vwVariant.setMatch(match);
            vwVariant.setVariantId(currentVar.getId());
            vwVariant.setNotified(false);
            match.getVariants().add(vwVariant);

            MatchVariant vwRefVariant = new MatchVariant();
            vwRefVariant.setDatabase(vwDatabase);
            vwRefVariant.setUser(_dataset.getUser());
            vwRefVariant.setMatch(match);
            vwRefVariant.setNotified(false);
            vwRefVariant.setVariantId(variant.getId());
            match.getVariants().add(vwRefVariant);

            matches.add(match);
        }
        return matches;
    }

    private Set<Match> createMatches(List<Variant> similarVariants, Variant variant, MatchType matchType) {
        Set<Match> matches = new HashSet<>();
        for (Variant currentVar : similarVariants) {
            Match match = new Match();
            match.setMatch_type(matchType.name());
            Double hpoDist = getHPOSetDistance(variant, currentVar);
            match.setHpoDist(hpoDist);
            match.setDatabase(_database);

            MatchVariant vwVariant = new MatchVariant();
            User matchedUser = currentVar.getDataset().getUser();
            vwVariant.setDatabase(_database);
            vwVariant.setUser(matchedUser);
            vwVariant.setMatch(match);
            vwVariant.setVariantId(currentVar.getId());
            vwVariant.setNotified(false);
            match.getVariants().add(vwVariant);

            MatchVariant vwRefVariant = new MatchVariant();
            vwRefVariant.setDatabase(vwDatabase);
            vwRefVariant.setUser(_dataset.getUser());
            vwRefVariant.setMatch(match);
            vwRefVariant.setNotified(false);
            vwRefVariant.setVariantId(variant.getId());
            match.getVariants().add(vwRefVariant);

            matches.add(match);
        }
        return matches;
    }

    private void loadHPODistances() {
        pathList = new HashMap<>();
        String hpoFile = WorkerLauncher.injector.getConfiguration().getPathToHPO();
        try (BufferedReader br = new BufferedReader(new FileReader(hpoFile))) {
            String line = br.readLine();

            while (line != null) {
                String[] hpoTerms = line.split(",");
                String firstHPO = hpoTerms[0];
                Set<String> currentPath = new HashSet<>();
                for (int i = 0; i < hpoTerms.length; i++) {
                    currentPath.add(hpoTerms[i]);
                }
                HPOPath hpoPath = null;
                if (pathList.containsKey(firstHPO)) {
                    hpoPath = pathList.get(firstHPO);
                } else {
                    hpoPath = new HPOPath(firstHPO);
                }
                hpoPath.addPath(currentPath);
                pathList.put(firstHPO, hpoPath);
                line = br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(VarWatchScreenerNew.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadHPODistances(String hpoFile) {
        pathList = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(hpoFile))) {
            String line = br.readLine();

            while (line != null) {
                String[] hpoTerms = line.split(",");
                String firstHPO = hpoTerms[0];
                Set<String> currentPath = new HashSet<>();
                for (int i = 0; i < hpoTerms.length; i++) {
                    currentPath.add(hpoTerms[i]);
                }
                HPOPath hpoPath = null;
                if (pathList.containsKey(firstHPO)) {
                    hpoPath = pathList.get(firstHPO);
                } else {
                    hpoPath = new HPOPath(firstHPO);
                }
                hpoPath.addPath(currentPath);
                pathList.put(firstHPO, hpoPath);
                line = br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(VarWatchScreenerNew.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Double getHPODistance(String hpoTerm1, String hpoTerm2) {
        HPOPath firstHPO = pathList.get(hpoTerm1);
        HPOPath secondHPO = pathList.get(hpoTerm2);
        if (firstHPO == null || secondHPO == null) {
            return 0.5;
        }
        return firstHPO.getSimilarity(secondHPO);
    }

    public static void main(String[] args) {
        VarWatchScreenerNew vwScreenerNew = new VarWatchScreenerNew();
        vwScreenerNew.loadHPODistances("/data/varwatch/VarWatchService/lib/ancestors_path_newHpo.txt");
        System.out.println(vwScreenerNew.getHPODistance("HP:0000118", "HP:0000002"));
        System.out.println(vwScreenerNew.getHPODistance("HP:0000002", "HP:0000002"));
        System.out.println(vwScreenerNew.getHPODistance("HP:0000118", "HP:0000118"));
    }

    private MatchType getTypeFromMatch(Variant queryVariant, Variant variant) {
        if (isPerfectMatch(queryVariant, variant)) {
            return MatchType.perfect;
        } else if (isNucleotideMatch(queryVariant, variant)) {
            return MatchType.nucleotide;
        } else if (isCodonMatch(queryVariant, variant)) {
            return MatchType.codon;
        }
        return MatchType.no_match;
    }

    private boolean isPerfectMatch(Variant queryVariant, Variant variant) {
        return queryVariant.getChromosomeName().equals(variant.getChromosomeName()) && queryVariant.getChromosomePos().equals(variant.getChromosomePos()) && queryVariant.getReferenceBase().equals(variant.getReferenceBase()) && queryVariant.getAlternateBase().equals(variant.getAlternateBase());
    }

    private boolean isNucleotideMatch(Variant queryVariant, Variant variant) {
        return queryVariant.getChromosomeName().equals(variant.getChromosomeName()) && queryVariant.getChromosomePos().equals(variant.getChromosomePos());
    }

    private boolean isCodonMatch(Variant queryVariant, Variant variant) {
        for (VariantEffect varEff1 : queryVariant.getVariantEffects()) {
            for (VariantEffect varEff2 : variant.getVariantEffects()) {
                if (varEff2.getProtein_end() != null && varEff2.getProtein_start() != null && varEff1.getProtein_end() != null && varEff1.getProtein_start() != null && (varEff1.getProtein_start() <= varEff2.getProtein_end() && varEff1.getProtein_end() >= varEff2.getProtein_start())) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Variant> getLofMatches(List<Variant> similarGenVariants) {
        List<Variant> lofMatches = new ArrayList<>();
        for (Variant curDs : similarGenVariants) {
            for (VariantEffect varEff : curDs.getVariantEffects()) {
                String loftee = varEff.getLoftee();
                if (loftee != null && loftee.equals("HC")) {
                    lofMatches.add(curDs);
                    break;
                }
            }
        }
        return lofMatches;
    }

    public enum MatchType {

        no_match, perfect, nucleotide, codon, lof, gene;
    }
}
