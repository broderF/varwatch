///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.importer.hgmd;
//
//import com.google.inject.Guice;
//import com.google.inject.Inject;
//import com.google.inject.Injector;
//import com.ikmb.annotation.VEPAnnotater;
////import com.ikmb.varwatchsql.databasehelper.EnsemblDatabaseHelper;
//import com.ikmb.core.varwatchcommons.entities.Feature;
//import com.ikmb.core.varwatchcommons.entities.GenomicFeature;
//import com.ikmb.core.varwatchcommons.entities.VWMatchRequest;
//import com.ikmb.core.varwatchcommons.entities.Patient;
//import com.ikmb.core.varwatchcommons.entities.VWVariant;
//import com.ikmb.core.varwatchcommons.utils.ParserHelper;
////import com.ikmb.varwatchsql.PersistenceManager;
//import com.ikmb.core.guice.VarWatchInjector;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import javax.persistence.EntityManager;
//import javax.persistence.TypedQuery;
//import org.codehaus.jettison.json.JSONArray;
//import org.codehaus.jettison.json.JSONException;
//import org.codehaus.jettison.json.JSONObject;
//
///**
// *
// * @author broder
// */
//public class ImportHGMDData {
//
//    @Inject
//    private VEPAnnotater vepAnnotator;
//    private long dsid = 1;
//    private long hpo2dsid = 1;
//
//    public static void main(String[] args) throws IOException, JSONException, SQLException {
//        Injector injector = Guice.createInjector(new VarWatchInjector());
//
//        ImportHGMDData hgmdimporter = injector.getInstance(ImportHGMDData.class);
////        String pathToFile = "/data/varwatch/data/hgmd/hgmd_2014_3/hgmd2json.with_alleles.vcf";
////        String pathToFile = "/mnt/shared/hgmd_2016_1.json";
//        String pathToFile = "/data/varwatch/data/hgmd/hgmd_2016_3.json";
//
//        hgmdimporter.run(pathToFile);
//    }
//    private PrintWriter noTranscriptFound;
//    private PrintWriter noHpoFound;
//
//    private void run(String path) throws IOException, JSONException, SQLException {
//        // Read file -> create string from whole file
//        String hgmdContent = new String(Files.readAllBytes(Paths.get(path)));
//        // parse string with json array
//        JSONArray jsonArray = new JSONArray(hgmdContent);
//        //iterate over array, create mmerequest
//        List<VWMatchRequest> matchrequests = new ArrayList<VWMatchRequest>();
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject jsonObject = jsonArray.getJSONObject(i);
//            VWMatchRequest matchRequest = parseGenomicFeature(jsonObject);
//            matchrequests.add(matchRequest);
//        }
//
//        FileWriter fw = new FileWriter("/home/bfredrich/NotFoundTranscripts.txt", false);
//        BufferedWriter bw = new BufferedWriter(fw);
//        noTranscriptFound = new PrintWriter(bw);
//        FileWriter nhpofw = new FileWriter("/home/bfredrich/NotFoundHpo.txt", false);
//        BufferedWriter nhpobw = new BufferedWriter(nhpofw);
//        noHpoFound = new PrintWriter(nhpobw);
//
//        Map<String, Long> hpoTerms = getHPOTerms();
//        System.out.println("hpos: " + hpoTerms.size());
//        Map<String, TranscriptSQL> transcripts = getTranscriptTerms();
//        System.out.println("transcripts: " + transcripts.size());
//
//        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/varwatch?"
//                + "user=root&password=yuBr3Pef");
//        connection.setAutoCommit(false);
//        Statement statement = connection.createStatement();
//
//        //annotate and save in db
//        Map<String, List<GenomicFeature>> genomicMap = new HashMap<>();
//        Map<String, VWMatchRequest> matchReqMap = new HashMap<>();
//        for (int i = 0; i < matchrequests.size(); i++) {
//            VWMatchRequest matchrequest = matchrequests.get(i);
//            List<GenomicFeature> tmpFeatures = matchrequest.getPatient().getGenomicFeatures();
//            String hgmdId = matchrequest.getPatient().getDescription();
//            genomicMap.put(hgmdId, tmpFeatures);
//            matchReqMap.put(hgmdId, matchrequest);
//            if (genomicMap.size() % 500 == 0) {
//                byte[] vcf = ParserHelper.json2vcf(genomicMap).getBytes();
//                vepAnnotator.runOfflineWithFasta(vcf, EnsemblDatabaseHelper.getActiveEnsembl(Boolean.FALSE).getName(), i + "", "/data/varwatch/VarWatchService/tmp/");
//                Map<String, List<VariantEffectSQL>> variantEffects = vepAnnotator.getVariantEffectMap();
//                this.saveHGMDData(matchReqMap, variantEffects, hpoTerms, transcripts, statement);
//                int[] rows = statement.executeBatch();
//                connection.commit();
//                genomicMap = new HashMap<>();
//                matchReqMap = new HashMap<>();
//            }
//        }
//        byte[] vcf = ParserHelper.json2vcf(genomicMap).getBytes();
//        vepAnnotator.runOfflineWithFasta(vcf, EnsemblDatabaseHelper.getActiveEnsembl(Boolean.FALSE).getName(), "last", "/data/varwatch/VarWatchService/tmp/");
//        Map<String, List<VariantEffectSQL>> variantEffects = vepAnnotator.getVariantEffectMap();
//        this.saveHGMDData(matchReqMap, variantEffects, hpoTerms, transcripts, statement);
//        int[] rows = statement.executeBatch();
//        connection.commit();
//    }
//
//    private Map<String, Long> getHPOTerms() {
//        EntityManager _em = PersistenceManager.INSTANCE.getEntityManager();
//        _em.getTransaction().begin();
//        TypedQuery<Object[]> query = _em.createQuery("SELECT s.id,s.identifier FROM HPOTermSQL s", Object[].class);
//        List<Object[]> hpoTermSQL = query.getResultList();
//        Map<String, Long> hpoMap = new HashMap<>();
//        for (Object[] hpoTerm : hpoTermSQL) {
//            Long id = (Long) hpoTerm[0];
//            String name = (String) hpoTerm[1];
//            hpoMap.put(name, id);
//        }
//        _em.getTransaction().commit();
//        _em.close();
//        return hpoMap;
//    }
//
//    private Map<String, TranscriptSQL> getTranscriptTerms() {
//        EntityManager _em = PersistenceManager.INSTANCE.getEntityManager();
//        _em.getTransaction().begin();
//        TypedQuery<TranscriptSQL> query = _em.createQuery("SELECT s FROM TranscriptSQL s", TranscriptSQL.class);
//        List<TranscriptSQL> hpoTermSQL = query.getResultList();
//        Map<String, TranscriptSQL> hpoMap = new HashMap<>();
//        for (TranscriptSQL hpoTerm : hpoTermSQL) {
//            hpoMap.put(hpoTerm.getName(), hpoTerm);
//        }
//        _em.getTransaction().commit();
//        _em.close();
//        return hpoMap;
//    }
//
//    private void saveHGMDData(Map<String, VWMatchRequest> matchrequests, Map<String, List<VariantEffectSQL>> variantEffectMap, Map<String, Long> hpoTerms, Map<String, TranscriptSQL> transcripts, Statement statement) throws SQLException {
//        for (String hgmdId : variantEffectMap.keySet()) {
//            Set<Long> hpoTermsSQL = new HashSet<Long>();
//            VWMatchRequest matchrequest = matchrequests.get(hgmdId);
//            for (Feature hpoTerm : matchrequest.getPatient().getFeatures()) {
//                String hpoTerm1 = hpoTerm.getId();
//                if (hpoTerm1 != null && hpoTerms.containsKey(hpoTerm1)) {
//                    hpoTermsSQL.add(hpoTerms.get(hpoTerm1));
//                }
//            }
//
//            VWVariant variant = matchrequest.getPatient().getGenomicFeatures().get(0).getVariant();
//            VariantEffectSQL variantEffect = null;
//            Integer transcriptSQL = null;
//            List<VariantEffectSQL> variantEffects = variantEffectMap.get(hgmdId);
//            for (VariantEffectSQL curVarEffect : variantEffects) {
//                TranscriptSQL curTranscriptSQL = transcripts.get(curVarEffect.getTranscriptName());
//                if (curTranscriptSQL != null && curTranscriptSQL.getCanonical()) {
//                    transcriptSQL = curTranscriptSQL.getId();
//                    variantEffect = curVarEffect;
//                    break;
//                }
//            }
//            if (transcriptSQL == null) {
//                return;
//            }
////        id   | hgmd_acc  | chr_name | chr_pos   | ref_base | alt_base | polyphen | sift | transcript_id 
//            String hgmd = matchrequest.getPatient().getDescription();
//            String chrom = variant.getReferenceName();
//            Integer pos = variant.getStart();
//            String alt = variant.getAlternateBases();
//            String ref = variant.getReferenceBases();
//            Integer proteinStart = null;
//            Integer proteinEnd = null;
//            String loftee = null;
//            String conseq = null;
//            String impact = null;
//            Double poly = 0.0;
//            Double sift = 0.0;
//            if (variantEffect != null) {
//                proteinStart = variantEffect.getProtein_start();
//                proteinEnd = variantEffect.getProtein_end();
//                if (variantEffect.getLoftee() != null) {
//                    loftee = "\"" + variantEffect.getLoftee() + "\"";
//                }
//                poly = variantEffect.getPolyphen();
//                sift = variantEffect.getSift();
//                conseq = variantEffect.getConsequence();
//                impact = variantEffect.getImpactFactor();
//            }
//            Integer transId = transcriptSQL;
//
//            String dsInsert = "INSERT INTO dataset (id) VALUES(" + dsid + ");";
//            statement.addBatch(dsInsert);
//            String insertdist = "INSERT INTO dataset_hgmd (id,hgmd_acc,chr_name,chr_pos,ref_base,alt_base,consequence,impact,polyphen,sift,protein_start,protein_end,loftee,transcript_id) "
//                    + "VALUES(" + dsid + ",\"" + hgmd + "\",\"" + chrom + "\"," + pos + ",\"" + ref + "\",\"" + alt + "\",\"" + conseq + "\",\"" + impact + "\"," + poly + "," + sift + "," + proteinStart + "," + proteinEnd + "," + loftee + "," + transId + ");";
//            System.out.println(insertdist);
//            statement.addBatch(insertdist);
//            for (Long hpoId : hpoTermsSQL) {
////            id   | dataset_id | hpo_id
//                String inserthpo = "INSERT INTO dataset2hpo (id,dataset_id,phenotype_id) VALUES(" + hpo2dsid + "," + dsid + "," + hpoId + ");";
//                statement.addBatch(inserthpo);
//                System.out.println(inserthpo);
//                hpo2dsid++;
//            }
//            dsid++;
//        }
//    }
//
//    private VWMatchRequest parseGenomicFeature(JSONObject jsonObject) throws JSONException {
//        //{"hgmd_acc":"BM0042985","chromosome":"3","strand":"-","pos":164777814,"ref":"A","alt":"G","hpo":[],"umls":[]}
//        String hgmdAcc = jsonObject.getString("hgmd_acc");
//        String chromosome = jsonObject.getString("chromosome");
//        int position = jsonObject.getInt("pos");
//        String ref = jsonObject.getString("ref");
//        String alt = jsonObject.getString("alt");
//        JSONArray hpoTerms = jsonObject.getJSONArray("hpo");
//
//        Patient patient = new Patient();
//        patient.setDescription(hgmdAcc);
//
//        List<Feature> features = new ArrayList<Feature>();
//        for (int i = 0; i < hpoTerms.length(); i++) {
//            Feature feature = new Feature();
//            feature.setId(hpoTerms.getString(i));
//            features.add(feature);
//        }
//        patient.setFeatures(features);
//
//        List<GenomicFeature> genomicFeatures = new ArrayList<GenomicFeature>();
//        GenomicFeature genomicFeature = new GenomicFeature();
//        VWVariant variant = new VWVariant();
//        variant.setAlternateBases(alt);
//        variant.setReferenceBases(ref);
//        variant.setStart(position);
//        variant.setReferenceName(chromosome);
//        genomicFeature.setVariant(variant);
//        genomicFeatures.add(genomicFeature);
//        patient.setGenomicFeatures(genomicFeatures);
//
//        VWMatchRequest matchrq = new VWMatchRequest();
//        matchrq.setPatient(patient);
//        return matchrq;
//    }
//}
