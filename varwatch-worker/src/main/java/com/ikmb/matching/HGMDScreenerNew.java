/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.ikmb.WorkerLauncher;
import com.ikmb.varwatchsql.guice.VarWatchInjector;
import com.ikmb.varwatchsql.guice.VarWatchPersist;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.entities.DatasetHGMDSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.entities.VariantEffectSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetManager;
import com.ikmb.varwatchsql.matching.MatchSQL;
import com.ikmb.varwatchsql.data.reference_db.RefDatabaseSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.data.hpo.PhenotypeSQL;
import com.ikmb.varwatchsql.data.reference_db.ReferenceDBDataManager;
import com.ikmb.varwatchsql.matching.MatchVariantDataManager;
import com.ikmb.varwatchsql.matching.MatchVariantSQL;
import com.ikmb.varwatchsql.status.variant.VariantStatusManager;
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
 * @author bfredrich
 */
public class HGMDScreenerNew implements DatabaseScreener {

    private List<MatchSQL> _matches = new ArrayList<MatchSQL>();
    private DatasetVWSQL _dataset;
    private RefDatabaseSQL _database;
    private HashMap<String, HPOPath> pathList;
    private RefDatabaseSQL vwDatabase;

    @Inject
    private MatchVariantDataManager matchDataManager;

    @Inject
    private VariantStatusManager variantStatusManager;

    @Override
    public void setVWDatabase(RefDatabaseSQL vwDatabase) {
        this.vwDatabase = vwDatabase;
    }

    @Override
    public void run() {
        if (pathList == null) {
            loadHPODistances();
        }

        for (VariantSQL variant : _dataset.getVariants()) {

            findMatches(variant);

        }
//        matchDataManager.persistMatches(getMatches());
//        variantStatusManager.addMatches(getMatches(), VariantStatusBuilder.VariantStatusMessage.MATCHED_TO_HGMD);
    }

    public void findMatches(VariantSQL vwVariant) {
        List<DatasetHGMDSQL> similarGeneVariants = matchDataManager.getSimilarHGMDByGene(vwVariant);
        if (similarGeneVariants.isEmpty()) {
            System.out.println("no matches found");
            return;
        }

        Set<MatchSQL> matches = createAminoAcidMatches(similarGeneVariants, vwVariant);
        if (!matches.isEmpty()) {
            _matches.addAll(matches);
            return;
        }

        boolean isLof = false;
        for (VariantEffectSQL vareff : vwVariant.getVariantEffects()) {
            String loftee = vareff.getLoftee();
            if (loftee != null && loftee.equals("HC")) {
                isLof = true;
                break;
            }
        }

        List<DatasetHGMDSQL> lofMatches = new ArrayList<>();
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

    @Override
    public List<MatchSQL> getMatches() {
        return _matches;
    }

    @Override
    public void initialize(RefDatabaseSQL database, DatasetVWSQL dataset
    ) {
        _database = database;
        _dataset = dataset;
    }

    @Override
    public DatasetVWSQL getDataset() {
        return _dataset;
    }

    @Override
    public RefDatabaseSQL getDatabase() {
        return _database;
    }

    private Set<MatchSQL> createMatches(List<DatasetHGMDSQL> identicalVariants, VariantSQL variant, boolean equalGene, boolean equalPath, boolean equalFam) {
        Set<MatchSQL> matches = new HashSet<>();
        for (DatasetHGMDSQL currentVar : identicalVariants) {
            MatchSQL match = new MatchSQL();
            match.setEqualGene(equalGene);
            match.setEqualPath(equalPath);
            match.setEqualFam(equalFam);
            match.setDatabase(_database);
            Double hpoDist = getHPOSetDistance(variant, currentVar);
            match.setHpoDist(hpoDist);
            if (currentVar.getChrName().equals(variant.getChromosomeName()) && currentVar.getChrPos().equals(variant.getChromosomePos())) {
                match.setIdentical(true);
            } else {
                match.setIdentical(false);
            }

            MatchVariantSQL hgmdVariant = new MatchVariantSQL();
            hgmdVariant.setDatabase(_database);
            hgmdVariant.setMatch(match);
            hgmdVariant.setVariantId(currentVar.getId());
            hgmdVariant.setNotified(false);
            match.getVariants().add(hgmdVariant);

            MatchVariantSQL vwVariant = new MatchVariantSQL();
            vwVariant.setDatabase(vwDatabase);
            vwVariant.setUser(_dataset.getUser());
            vwVariant.setMatch(match);
            vwVariant.setNotified(false);
            vwVariant.setVariantId(variant.getId());
            match.getVariants().add(vwVariant);

            matches.add(match);
        }
        return matches;
    }

    private Double getHPOSetDistance(VariantSQL variant, DatasetHGMDSQL fixVariant) {
        Set<PhenotypeSQL> qhpos = variant.getDataset().getPhenotypes();

        Set<PhenotypeSQL> thpos = fixVariant.getPhenotypes();

        Double maxSim = 0d;
        for (PhenotypeSQL qhpo : qhpos) {
            Double currentMaxSimilarity = 0.5d;
            for (PhenotypeSQL thpo : thpos) {
                String hpoTerm1 = qhpo.getPhenotype().getIdentifier();
                String hpoTerm2 = thpo.getPhenotype().getIdentifier();
                Double currentSimilartiy = getHPODistance(hpoTerm1, hpoTerm2);
                if (currentSimilartiy > currentMaxSimilarity) {
                    currentMaxSimilarity = currentSimilartiy;
                }
            }
            maxSim += currentMaxSimilarity;
        }

        for (PhenotypeSQL thpo : thpos) {
            Double currentMaxSimilarity = 0.5d;
            for (PhenotypeSQL qhpo : qhpos) {
                String hpoTerm1 = qhpo.getPhenotype().getIdentifier();
                String hpoTerm2 = thpo.getPhenotype().getIdentifier();
                Double currentSimilartiy = getHPODistance(hpoTerm2, hpoTerm1);
                if (currentSimilartiy > currentMaxSimilarity) {
                    currentMaxSimilarity = currentSimilartiy;
                }
            }
            maxSim += currentMaxSimilarity;
        }

        if ((qhpos.size() + thpos.size()) != 0) {
            double result = maxSim / ((double) (qhpos.size() + thpos.size()));
//            if (result == 0) {
//                System.out.println("stop");
//            }
            return result;
        } else {
            return 0.5;
        }
    }

    private void loadHPODistances() {
        pathList = new HashMap<>();
        String hpoFile = WorkerLauncher.injector.getConfiguration().getPathToHPO();
//        String hpoFile = "/data/varwatch/VarWatchService/lib/ancestors_path_newHpo.txt";
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
        if (firstHPO != null && secondHPO != null) {
            return firstHPO.getSimilarity(secondHPO);
        } else {
            return 0.5;
        }
    }

    public static void main(String[] args) {
        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
        HGMDScreenerNew hgmd = inj.getInstance(HGMDScreenerNew.class);
        DatasetManager dsManager = inj.getInstance(DatasetManager.class);
        hgmd.loadHPODistances("/data/varwatch/VarWatchService/lib/ancestors_path_newHpo.txt");
        RefDatabaseSQL varwatchDB = inj.getInstance(ReferenceDBDataManager.class).getVarWatchDatabase();
        hgmd.initialize(varwatchDB, dsManager.getDatasetByID(107157l));
        hgmd.setVWDatabase(varwatchDB);
        hgmd.run();
        System.out.println("finish");
    }

    private Set<MatchSQL> createAminoAcidMatches(List<DatasetHGMDSQL> similarVariants, VariantSQL variant) {
        Set<MatchSQL> matches = new HashSet<>();
        for (DatasetHGMDSQL currentVar : similarVariants) {
            MatchSQL match = new MatchSQL();
            HGMDScreenerNew.MatchType matchingType = getTypeFromMatch(variant, currentVar);
            if (matchingType.equals(HGMDScreenerNew.MatchType.no_match)) {
                continue;
            }
            match.setMatch_type(matchingType.name());
            Double hpoDist = getHPOSetDistance(variant, currentVar);
            match.setHpoDist(hpoDist);
            match.setDatabase(_database);

            MatchVariantSQL hgmdVariant = new MatchVariantSQL();
            hgmdVariant.setDatabase(_database);
            hgmdVariant.setMatch(match);
            hgmdVariant.setVariantId(currentVar.getId());
            hgmdVariant.setNotified(false);
            match.getVariants().add(hgmdVariant);

            MatchVariantSQL vwRefVariant = new MatchVariantSQL();
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

    private MatchType getTypeFromMatch(VariantSQL queryVariant, DatasetHGMDSQL variant) {
        if (isPerfectMatch(queryVariant, variant)) {
            return MatchType.perfect;
        } else if (isNucleotideMatch(queryVariant, variant)) {
            return MatchType.nucleotide;
        } else if (isCodonMatch(queryVariant, variant)) {
            return MatchType.codon;
        }
        return MatchType.no_match;
    }

    private boolean isPerfectMatch(VariantSQL queryVariant, DatasetHGMDSQL variant) {
        return queryVariant.getChromosomeName().equals(variant.getChrName()) && queryVariant.getChromosomePos().equals(variant.getChrPos()) && queryVariant.getReferenceBase().equals(variant.getRefBase()) && queryVariant.getAlternateBase().equals(variant.getAltBase());
    }

    private boolean isNucleotideMatch(VariantSQL queryVariant, DatasetHGMDSQL variant) {
        return queryVariant.getChromosomeName().equals(variant.getChrName()) && queryVariant.getChromosomePos().equals(variant.getChrPos());
    }

    private boolean isCodonMatch(VariantSQL queryVariant, DatasetHGMDSQL variant) {
        for (VariantEffectSQL varEff1 : queryVariant.getVariantEffects()) {
            if (variant.getProtein_end() == null || variant.getProtein_start() == null || varEff1.getProtein_start() == null || varEff1.getProtein_end() == null) {
                System.out.println("variant or matching variant effect is null for (hgmd variant: " + queryVariant.getId() + ") and effet(effect:" + varEff1.getId() + ")");
                continue;
            }
            if (varEff1.getProtein_start() <= variant.getProtein_end() && varEff1.getProtein_end() >= variant.getProtein_start()) {
                return true;
            }
        }
        return false;
    }

    private Set<MatchSQL> createMatches(List<DatasetHGMDSQL> similarGenVariants, VariantSQL variant, MatchType matchType) {
        Set<MatchSQL> matches = new HashSet<>();

        for (DatasetHGMDSQL currentVar : similarGenVariants) {
            MatchSQL match = new MatchSQL();
            match.setMatch_type(matchType.name());
            Double hpoDist = getHPOSetDistance(variant, currentVar);
            match.setHpoDist(hpoDist);
            match.setDatabase(_database);

            MatchVariantSQL hgmdVariant = new MatchVariantSQL();
            hgmdVariant.setDatabase(_database);
            hgmdVariant.setMatch(match);
            hgmdVariant.setVariantId(currentVar.getId());
            hgmdVariant.setNotified(false);
            match.getVariants().add(hgmdVariant);

            MatchVariantSQL vwRefVariant = new MatchVariantSQL();
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

    private List<DatasetHGMDSQL> getLofMatches(List<DatasetHGMDSQL> similarGenVariants) {
        List<DatasetHGMDSQL> lofMatches = new ArrayList<>();
        for (DatasetHGMDSQL curDs : similarGenVariants) {
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
