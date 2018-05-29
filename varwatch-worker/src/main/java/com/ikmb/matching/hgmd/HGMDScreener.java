/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching.hgmd;

import com.google.inject.Inject;
import com.ikmb.core.data.dataset.DatasetHGMD;
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
 * @author bfredrich
 */
public class HGMDScreener {

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

    public void findMatches(Variant vwVariant) {
        List<DatasetHGMD> similarGeneVariants = matchDataManager.getSimilarHGMDByGene(vwVariant);
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

        List<DatasetHGMD> lofMatches = new ArrayList<>();
        if (isLof) {
            lofMatches = getLofMatches(similarGeneVariants);
        }

        if (!lofMatches.isEmpty()) {
            matches = createMatches(lofMatches, vwVariant, MatchType.lof);
            _matches.addAll(matches);
            return;
        }

        matches = createMatches(similarGeneVariants, vwVariant, MatchType.gene);
        _matches.addAll(matches);
    }

    public List<Match> getMatches() {
        return _matches;
    }

    public void initialize(RefDatabase database, DatasetVW dataset
    ) {
        _database = database;
        _dataset = dataset;
    }

    public DatasetVW getDataset() {
        return _dataset;
    }

    public RefDatabase getDatabase() {
        return _database;
    }

//    public static void main(String[] args) {
//        Injector inj = Guice.createInjector(new VarWatchMainModule(), new JpaPersistModule("varwatch_dev"));
//        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
//        HGMDScreener hgmd = inj.getInstance(HGMDScreener.class);
//        DatasetManager dsManager = inj.getInstance(DatasetManager.class);
//        hgmd.loadHPODistances("/data/varwatch/VarWatchService/lib/ancestors_path_newHpo.txt");
//        RefDatabase varwatchDB = inj.getInstance(ReferenceDBDataManager.class).getVarWatchDatabase();
//        hgmd.initialize(varwatchDB, dsManager.getDatasetByID(107157l));
//        hgmd.setVWDatabase(varwatchDB);
//        hgmd.run();
//        System.out.println("finish");
//    }
    private Set<Match> createAminoAcidMatches(List<DatasetHGMD> similarVariants, Variant variant) {
        Set<Match> matches = new HashSet<>();
        for (DatasetHGMD currentVar : similarVariants) {
            Match match = new Match();
            HGMDScreener.MatchType matchingType = getTypeFromMatch(variant, currentVar);
            if (matchingType.equals(HGMDScreener.MatchType.no_match)) {
                continue;
            }
            match.setMatch_type(matchingType.name());
            Double hpoDist = hpoDistanceCalculator.getHPOSetDistance(variant.getDataset().getPhenotypes(), currentVar.getPhenotypes());
            match.setHpoDist(hpoDist);
            match.setDatabase(_database);

            MatchVariant hgmdVariant = new MatchVariant();
            hgmdVariant.setDatabase(_database);
            hgmdVariant.setMatch(match);
            hgmdVariant.setVariantId(currentVar.getId());
            hgmdVariant.setNotified(false);
            match.getVariants().add(hgmdVariant);

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

    private MatchType getTypeFromMatch(Variant queryVariant, DatasetHGMD variant) {
        if (isPerfectMatch(queryVariant, variant)) {
            return MatchType.perfect;
        } else if (isNucleotideMatch(queryVariant, variant)) {
            return MatchType.nucleotide;
        } else if (isCodonMatch(queryVariant, variant)) {
            return MatchType.codon;
        }
        return MatchType.no_match;
    }

    private boolean isPerfectMatch(Variant queryVariant, DatasetHGMD variant) {
        return queryVariant.getChromosomeName().equals(variant.getChrName()) && queryVariant.getChromosomePos().equals(variant.getChrPos()) && queryVariant.getReferenceBase().equals(variant.getRefBase()) && queryVariant.getAlternateBase().equals(variant.getAltBase());
    }

    private boolean isNucleotideMatch(Variant queryVariant, DatasetHGMD variant) {
        return queryVariant.getChromosomeName().equals(variant.getChrName()) && queryVariant.getChromosomePos().equals(variant.getChrPos());
    }

    private boolean isCodonMatch(Variant queryVariant, DatasetHGMD variant) {
        for (VariantEffect varEff1 : queryVariant.getVariantEffects()) {
            if (variant.getProtein_end() == null || variant.getProtein_start() == null || varEff1.getProtein_start() == null || varEff1.getProtein_end() == null) {
//                System.out.println("variant or matching variant effect is null for (hgmd variant: " + queryVariant.getId() + ") and effet(effect:" + varEff1.getId() + ")");
                continue;
            }
            if (varEff1.getProtein_start() <= variant.getProtein_end() && varEff1.getProtein_end() >= variant.getProtein_start()) {
                return true;
            }
        }
        return false;
    }

    private Set<Match> createMatches(List<DatasetHGMD> similarGenVariants, Variant variant, MatchType matchType) {
        Set<Match> matches = new HashSet<>();

        for (DatasetHGMD currentVar : similarGenVariants) {
            Match match = new Match();
            match.setMatch_type(matchType.name());
            Double hpoDist = hpoDistanceCalculator.getHPOSetDistance(variant.getDataset().getPhenotypes(), currentVar.getPhenotypes());
            match.setHpoDist(hpoDist);
            match.setDatabase(_database);

            MatchVariant hgmdVariant = new MatchVariant();
            hgmdVariant.setDatabase(_database);
            hgmdVariant.setMatch(match);
            hgmdVariant.setVariantId(currentVar.getId());
            hgmdVariant.setNotified(false);
            match.getVariants().add(hgmdVariant);

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

    private List<DatasetHGMD> getLofMatches(List<DatasetHGMD> similarGenVariants) {
        List<DatasetHGMD> lofMatches = new ArrayList<>();
        for (DatasetHGMD curDs : similarGenVariants) {
            String loftee = curDs.getLoftee();
            if (loftee != null && loftee.equals("HC")) {
                lofMatches.add(curDs);
            }
        }
        return lofMatches;
    }

    public enum MatchType {

        no_match, perfect, nucleotide, codon, lof, gene;
    }
}
