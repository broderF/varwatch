///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.importer;
//
//import com.ikmb.importer.hgmd.HGMDData;
//import com.ikmb.annotation.VEPAnnotater;
//import com.ikmb.varwatchsql.variant_data.dataset.DatasetHGMDSQL;
//import com.ikmb.varwatchsql.data.hpo.HPOTermSQL;
//import com.ikmb.core.data.transcript.TranscriptSQL;
//import com.ikmb.core.varwatchcommons.entities.VWVariant;
//import com.ikmb.varwatchsql.variant_data.varianteffect.VariantEffectSQL;
//import com.ikmb.varwatchsql.data.hpo.PhenotypeSQL;
//import com.ikmb.update.variantdata.DatasetService;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.codehaus.jettison.json.JSONArray;
//import org.codehaus.jettison.json.JSONException;
//import org.codehaus.jettison.json.JSONObject;
//
///**
// *
// * @author broder
// */
//public class ImportDataHandler {
//
//    public void persist(DatasetHGMDSQL dataset) {
//        DatasetService dsService = new DatasetService();
//        dsService.persistHgmdDataset(dataset);
//    }
//
//    public List<VariantEffectSQL> annotate(byte[] vcfFile) {
//        VEPAnnotater vepAnnotater = new VEPAnnotater();
//        vepAnnotater.run(vcfFile,"EnsEMBL/83", "hgmd_file");
//        return vepAnnotater.getVariantEffects();
//    }
//
//    public List<HGMDData> readHgmdFile(File hmdgData) {
//
//        String jsonFileString = null;
//        try (BufferedReader br = new BufferedReader(new FileReader(hmdgData))) {
//            StringBuilder sb = new StringBuilder();
//            String line = br.readLine();
//
//            while (line != null) {
//                sb.append(line);
//                sb.append(System.lineSeparator());
//                line = br.readLine();
//            }
//            jsonFileString = sb.toString();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ImportDataHandler.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ImportDataHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        List<HGMDData> hgmdDatas = new ArrayList<HGMDData>();
//
//        try {
//            JSONArray jsonArray = new JSONArray(jsonFileString);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject hgmdObject = jsonArray.getJSONObject(i);
//                String hgmdAcc = hgmdObject.getString("hgmd_acc");
//                String ref = hgmdObject.getString("ref");
//                String alt = hgmdObject.getString("alt");
//                String chromosome = hgmdObject.getString("chromosome");
//                Integer pos = hgmdObject.getInt("pos");
//
//                JSONArray hposJson = hgmdObject.getJSONArray("hpo");
//                List<String> hpos = new ArrayList<String>();
//                for (int j = 0; j < hposJson.length(); j++) {
//                    String hpo = hposJson.getString(j);
//                    hpos.add(hpo);
//                }
//
//                JSONArray umlsJson = hgmdObject.getJSONArray("umls");
//                List<String> umls = new ArrayList<String>();
//                for (int j = 0; j < umlsJson.length(); j++) {
//                    String ulms = umlsJson.getString(j);
//                    umls.add(ulms);
//                }
//
//                HGMDData hgmdData = new HGMDData();
//                hgmdData.setAlt(alt);
//                hgmdData.setChromosome(chromosome);
//                hgmdData.setHgmd_acc(hgmdAcc);
//                hgmdData.setHpos(hpos);
//                hgmdData.setPos(pos);
//                hgmdData.setRef(ref);
//                hgmdData.setUlms(umls);
//
//                hgmdDatas.add(hgmdData);
//            }
//        } catch (JSONException ex) {
//            Logger.getLogger(ImportDataHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return hgmdDatas;
//    }
//
//    public String createVcfFromHgmd(List<HGMDData> hgmdDatas) {
//        StringBuilder vcfString = new StringBuilder();
//        vcfString.append("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\n");
//        for (HGMDData hgmd : hgmdDatas) {
//            String alt = hgmd.getAlt();
//            String chromosome = hgmd.getChromosome();
//            String hgmd_acc = hgmd.getHgmd_acc();
//            Integer pos = hgmd.getPos();
//            String ref = hgmd.getRef();
//            vcfString.append(chromosome).append("\t").append(pos).append("\t").append(hgmd_acc).append("\t").append(ref).append("\t").append(alt).append("\t.\t.\t.\n");
//        }
//        return vcfString.toString();
//    }
//
//    public String createVcfFromVWVariants(List<VWVariant> variants) {
//        StringBuilder vcfString = new StringBuilder();
//        vcfString.append("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\n");
//        for (VWVariant variant : variants) {
//            String alt = variant.getAlternateBases();
//            String chromosome = variant.getReferenceName();
//            String hgmd_acc = variant.getVepIdentifier();
//            Integer pos = variant.getStart();
//            String ref = variant.getReferenceBases();
//            vcfString.append(chromosome).append("\t").append(pos).append("\t").append(hgmd_acc).append("\t").append(ref).append("\t").append(alt).append("\t.\t.\t.\n");
//        }
//        return vcfString.toString();
//    }
//
//    public List<DatasetHGMDSQL> createHgmdDatasets(List<HGMDData> hgmdDatas, List<VariantEffectSQL> variantEffects) {
//        List<DatasetHGMDSQL> datasets = new ArrayList<DatasetHGMDSQL>();
//        for (HGMDData hgmdData : hgmdDatas) {
//            for (VariantEffectSQL variantEffect : variantEffects) {
//                if (hgmdData.getHgmd_acc().equals(variantEffect.getUploaded_variation())) {
//                    if (!variantEffect.getCanonicalTranscript() || !(variantEffect.getImpactFactor().equals("HIGH") || variantEffect.getImpactFactor().equals("MODERATE"))) {
//                        continue;
//                    }
//
//                    DatasetHGMDSQL dataset = new DatasetHGMDSQL();
//                    dataset.setHgmdAcc(hgmdData.getHgmd_acc());
//                    dataset.setAltBase(hgmdData.getAlt());
//                    dataset.setChrName(hgmdData.getChromosome());
//                    dataset.setChrPos(hgmdData.getPos());
//                    Set<PhenotypeSQL> phenotypeTerms = new HashSet<PhenotypeSQL>();
//                    for (String hpoString : hgmdData.getHpos()) {
//                        HPOTermSQL hpoTermSQL = new HPOTermSQL();
//                        hpoTermSQL.setIdentifier(hpoString);
//                        PhenotypeSQL phenotype = new PhenotypeSQL();
//                        phenotype.setPhenotype(hpoTermSQL);
//                        phenotypeTerms.add(phenotype);
//                    }
//                    dataset.setPhenotypes(phenotypeTerms);
//                    dataset.setPolyphen(variantEffect.getPolyphen());
//                    dataset.setRefBase(hgmdData.getRef());
//                    dataset.setSift(variantEffect.getSift());
//                    TranscriptSQL transcript = new TranscriptSQL();
//                    transcript.setName(variantEffect.getTranscriptName());
//                    dataset.setTranscript(transcript);
//                    dataset.setVariantEffect(variantEffect);
//                    datasets.add(dataset);
//                }
//            }
//        }
//        return datasets;
//    }
//
//    public List<DatasetHGMDSQL> filterCanonicalTranscripts(List<DatasetHGMDSQL> rawHgmdDatasets) {
//        List<DatasetHGMDSQL> filteredList = new ArrayList<DatasetHGMDSQL>();
//        for (DatasetHGMDSQL dataset : rawHgmdDatasets) {
//            if (dataset.getVariantEffect().getCanonicalTranscript()) {
//                filteredList.add(dataset);
//            }
//        }
//        return filteredList;
//    }
//
//    public List<DatasetHGMDSQL> filterHighImpactVariants(List<DatasetHGMDSQL> rawHgmdDatasets) {
//        List<DatasetHGMDSQL> filteredList = new ArrayList<DatasetHGMDSQL>();
//        for (DatasetHGMDSQL dataset : rawHgmdDatasets) {
//            if (dataset.getVariantEffect().getImpactFactor().equals("HIGH") || dataset.getVariantEffect().getImpactFactor().equals("MODERATE")) {
//                filteredList.add(dataset);
//            }
//        }
//        return filteredList;
//    }
//}
