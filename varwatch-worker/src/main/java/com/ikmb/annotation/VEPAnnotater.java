/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.annotation;

import com.google.inject.Inject;
import com.ikmb.EnsemblHelper;
import com.ikmb.WorkerLauncher;
import com.ikmb.core.data.varianteffect.VariantEffect;
import com.ikmb.utils.VWUtils;
import com.ikmb.core.varwatchcommons.VWConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

/**
 *
 * @author fredrich
 */
public class VEPAnnotater {

    private byte[] _vepFile;
    private List<VariantEffect> _variantEffects = new ArrayList<>();
    @Inject
    private VWUtils vwutils;
    private Map<String, List<VariantEffect>> _variantEffectMap;

    public void run(byte[] vcfFile, String ensemblName, String fileIdentifier) {
        try {
            VEPParser vepParser = new VEPParser();
            String folder = WorkerLauncher.injector.getConfiguration().getPathToTmp();
            String pathToVCF = folder + "tmp_" + fileIdentifier + "_" + new DateTime().toString("ddMMyyyyHHmmss") + ".vcf";
            String pathToVEP = folder + "tmp_" + fileIdentifier + "_" + new DateTime().toString("ddMMyyyyHHmmss") + ".vep";

            vwutils.saveFile(vcfFile, pathToVCF);

            EnsemblHelper helper = new EnsemblHelper();
//            String command = "--canonical --sift b --poly b --force_overwrite --fields Uploaded_variation,Gene,Feature,Feature_type,Consequence,SIFT,PolyPhen,IMPACT,CANONICAL";
            String command = "--cache --dir $ENSEMBLCACHE --species human --fork 2 --canonical --sift b --poly b --force_overwrite --fields Uploaded_variation,Gene,Feature,Feature_type,Consequence,SIFT,PolyPhen,IMPACT,CANONICAL --dir_plugins /data/varwatch/tools/variant_effect/vep/plugins --plugin LoF";
            InputStream annotatedFile = helper.runEnsembl(pathToVCF, pathToVEP, ensemblName, command);
            annotatedFile.mark(Integer.MAX_VALUE);
            vepParser.parse(annotatedFile);
            annotatedFile.reset();
            _variantEffects = vepParser.getVariantEffects();
            _vepFile = IOUtils.toByteArray(annotatedFile);

//           s
        } catch (IOException ex) {
            Logger.getLogger(VEPAnnotater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void runOffline(byte[] vcfFile, String ensemblName, String fileIdentifier) {
        try {
            VEPParser vepParser = new VEPParser();
            String folder = WorkerLauncher.injector.getConfiguration().getPathToTmp();
            String pathToVCF = folder + "tmp_" + fileIdentifier + "_" + new DateTime().toString("ddMMyyyyHHmmss") + ".vcf";
            String pathToVEP = folder + "tmp_" + fileIdentifier + "_" + new DateTime().toString("ddMMyyyyHHmmss") + ".vep";

            vwutils.saveFile(vcfFile, pathToVCF);

            EnsemblHelper helper = new EnsemblHelper();
//            String command = "--canonical --sift b --poly b --force_overwrite --fields Uploaded_variation,Gene,Feature,Feature_type,Consequence,SIFT,PolyPhen,IMPACT,CANONICAL";
            String command = "--offline --dir_cache $ENSEMBLCACHE --cache --assembly GRCh38 --everything --fork 2 --canonical --sift b --poly b --force_overwrite --fields Uploaded_variation,Gene,Feature,Feature_type,Consequence,SIFT,PolyPhen,IMPACT,CANONICAL,CDS_position,Protein_position,Amino_acids";
            InputStream annotatedFile = helper.runEnsembl(pathToVCF, pathToVEP, ensemblName, command);
            annotatedFile.mark(Integer.MAX_VALUE);
            vepParser.parse(annotatedFile);
            annotatedFile.reset();
            _variantEffects = vepParser.getVariantEffects();
            _vepFile = IOUtils.toByteArray(annotatedFile);

//           s
        } catch (IOException ex) {
            Logger.getLogger(VEPAnnotater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void runOfflineWithFasta(byte[] vcfFile, String ensemblName, String fileIdentifier) {
        try {
            VEPParser vepParser = new VEPParser();
            VWConfiguration config = WorkerLauncher.injector.getConfiguration();
            String folder = config.getPathToTmp();
            String pluginDir = config.getPathToPluginDir();
            String fasta = config.getPathToFastaFile();
            String pathToVCF = folder + "tmp_" + fileIdentifier + "_" + new DateTime().toString("ddMMyyyyHHmmss") + ".vcf";
            String pathToVEP = folder + "tmp_" + fileIdentifier + "_" + new DateTime().toString("ddMMyyyyHHmmss") + ".vep";

            vwutils.saveFile(vcfFile, pathToVCF);

            EnsemblHelper helper = new EnsemblHelper();
//            String command = "--canonical --sift b --poly b --force_overwrite --fields Uploaded_variation,Gene,Feature,Feature_type,Consequence,SIFT,PolyPhen,IMPACT,CANONICAL";
            String command = "--dir_cache $ENSEMBLCACHE --cache --offline --assembly GRCh38 --fork 2 --canonical --sift b --poly b --force_overwrite --fasta "+fasta+" --fields Uploaded_variation,Gene,Feature,Feature_type,Consequence,SIFT,PolyPhen,IMPACT,CANONICAL,CDS_position,Protein_position,Amino_acids,LoF --dir_plugins "+pluginDir+" --plugin LoF";
            InputStream annotatedFile = helper.runEnsembl(pathToVCF, pathToVEP, ensemblName, command);
            annotatedFile.mark(Integer.MAX_VALUE);
            vepParser.parse(annotatedFile);
            annotatedFile.reset();
            _variantEffects = vepParser.getVariantEffects();
            _variantEffectMap = vepParser.getVariantEffectMap();
            _vepFile = IOUtils.toByteArray(annotatedFile);

//           s
        } catch (IOException ex) {
            Logger.getLogger(VEPAnnotater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void runOfflineWithFasta(byte[] vcfFile, String ensemblName, String fileIdentifier, String folder) {
        try {
            VEPParser vepParser = new VEPParser();
            String pathToVCF = folder + "tmp_" + fileIdentifier + "_" + new DateTime().toString("ddMMyyyyHHmmss") + ".vcf";
            String pathToVEP = folder + "tmp_" + fileIdentifier + "_" + new DateTime().toString("ddMMyyyyHHmmss") + ".vep";

            vwutils.saveFile(vcfFile, pathToVCF);

            EnsemblHelper helper = new EnsemblHelper();
//            String command = "--canonical --sift b --poly b --force_overwrite --fields Uploaded_variation,Gene,Feature,Feature_type,Consequence,SIFT,PolyPhen,IMPACT,CANONICAL";
            String command = "--dir_cache $ENSEMBLCACHE --cache --offline --assembly GRCh38 --fork 2 --canonical --sift b --poly b --force_overwrite --fasta /home/bfredrich/Downloads/Homo_sapiens.GRCh38.dna.chromosome.1.fa  --fields Uploaded_variation,Gene,Feature,Feature_type,Consequence,SIFT,PolyPhen,IMPACT,CANONICAL,CDS_position,Protein_position,Amino_acids,LoF --dir_plugins /data/varwatch/tools/variant_effect/vep/plugins --plugin LoF";
            InputStream annotatedFile = helper.runEnsembl(pathToVCF, pathToVEP, ensemblName, command);
            annotatedFile.mark(Integer.MAX_VALUE);
            vepParser.parse(annotatedFile);
            annotatedFile.reset();
            _variantEffects = vepParser.getVariantEffects();
            _variantEffectMap = vepParser.getVariantEffectMap();
            _vepFile = IOUtils.toByteArray(annotatedFile);

//           s
        } catch (IOException ex) {
            Logger.getLogger(VEPAnnotater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<VariantEffect> getVariantEffects() {
        return _variantEffects;
    }

    public Map<String, List<VariantEffect>> getVariantEffectMap() {
        return _variantEffectMap;
    }

    public byte[] getVEPFile() {
        return _vepFile;
    }
}
