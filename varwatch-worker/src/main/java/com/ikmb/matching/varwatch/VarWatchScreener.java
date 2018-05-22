/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching.varwatch;

import com.google.inject.Inject;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.matching.Match;
import com.ikmb.core.data.matching.MatchVariant;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.data.reference_db.RefDatabase;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.varianteffect.VariantEffect;
import com.ikmb.matching.HPODistanceCalculator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author broder
 */
public class VarWatchScreener {

    private final List<Match> _matches = new ArrayList<>();
    private DatasetVW _dataset;
    private RefDatabase _database;
    private RefDatabase vwDatabase;

    @Inject
    private MatchVariantDataManager matchDataManager;

    @Inject
    private HPODistanceCalculator hpoDistanceCalculator;

    public void setVWDatabase(RefDatabase vwDatabase) {
        this.vwDatabase = vwDatabase;
    }

    public void run() {
        for (Variant variant : _dataset.getVariants()) {
            findMatches(variant);
        }
    }

    private void findMatches(Variant vwVariant) {
        System.out.println("query variant: " + vwVariant.toString());
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
            matches = createMatches(lofMatches, vwVariant, VarWatchScreener.MatchType.lof);
            _matches.addAll(matches);
            return;
        }

        matches = createMatches(similarGeneVariants, vwVariant, VarWatchScreener.MatchType.gene);
        _matches.addAll(matches);
    }

    public List<Match> getMatches() {
        return _matches;
    }

    public void initialize(RefDatabase database, DatasetVW dataset) {
        _database = database;
        _dataset = dataset;
    }

    public DatasetVW getDataset() {
        return _dataset;
    }

    public RefDatabase getDatabase() {
        return _database;
    }

    private Set<Match> createAminoAcidMatches(List<Variant> similarVariants, Variant variant) {
        Set<Match> matches = new HashSet<>();
        System.out.println(similarVariants.size() + " variants in identical gene gound");
        for (Variant currentVar : similarVariants) {
            System.out.println("current comparison with variant " + currentVar.toString());
            Match match = new Match();
            MatchType matchingType = getTypeFromMatch(variant, currentVar);
            System.out.println(matchingType.name());
            if (matchingType.equals(MatchType.no_match)) {
                continue;
            }
            match.setMatch_type(matchingType.name());
            Double hpoDist = hpoDistanceCalculator.getHPOSetDistance(variant.getDataset().getPhenotypes(), currentVar.getDataset().getPhenotypes());
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
            Double hpoDist = hpoDistanceCalculator.getHPOSetDistance(variant.getDataset().getPhenotypes(), currentVar.getDataset().getPhenotypes());
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
