/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.utils;

import com.google.gson.Gson;
import com.ikmb.core.varwatchcommons.entities.GenomicFeature;
import com.ikmb.core.varwatchcommons.entities.VWMatchRequest;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

/**
 *
 * @author bfredrich
 */
public class VariantParser {

//    public static byte[] getVariantInfoFromObject(List<VWGenomicFeature> genomicFeatures) {
//        byte[] data = null;
//        try {
//            final ByteArrayOutputStream out = new ByteArrayOutputStream();
//            final ObjectMapper mapper = new ObjectMapper();
//
//            mapper.writeValue(out, genomicFeatures);
//
//            data = out.toByteArray();
//        } catch (IOException ex) {
//            Logger.getLogger(ParserHelper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return data;
//    }
    public static List<GenomicFeature> getVariantInfoFromByteArray(byte[] bytes) {
        List<GenomicFeature> featuresJson = new ArrayList<>();
        try {
            String base64String = new String(bytes, "UTF-8");

            JSONArray jsonObjects = new JSONArray(base64String);
            for (int i = 0; i < jsonObjects.length(); i++) {
                String curObject = jsonObjects.getJSONObject(i).toString();
                GenomicFeature genomicFeature = new Gson().fromJson(curObject, GenomicFeature.class);
                featuresJson.add(genomicFeature);
            }
        } catch (JSONException | UnsupportedEncodingException ex) {
            Logger.getLogger(VariantParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return featuresJson;
    }

    public static VWMatchRequest getVWMatchRequestFromByteArray(byte[] bytes) {
        VWMatchRequest matchRequest = null;
        try {
            String base64String = new String(bytes, "UTF-8");

            matchRequest = new Gson().fromJson(base64String, VWMatchRequest.class);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(VariantParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return matchRequest;
    }

    public static String json2vcf(List<GenomicFeature> genomicFeatures) {
        StringBuilder vcfString = new StringBuilder();
        vcfString.append("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\n");
        for (GenomicFeature genomicFeature : genomicFeatures) {
            VWVariant variant = genomicFeature.getVariant();
            String id = ".";
            if (variant.getVepIdentifier() != null) {
                id = variant.getVepIdentifier();
            }
            vcfString.append(variant.getReferenceName()).append("\t").append(variant.getStart()).append("\t").append(id).append("\t").append(variant.getReferenceBases()).append("\t").append(variant.getAlternateBases()).append("\t.\t.\t.\n");
        }
        return vcfString.toString();
    }

    public static String json2hgvs(List<GenomicFeature> genomicFeatures) {
        StringBuilder hgvsString = new StringBuilder();
        for (GenomicFeature genomicFeature : genomicFeatures) {
            VWVariant variant = genomicFeature.getVariant();
            hgvsString.append(variant.getHgvs()).append("\n");
        }
        return hgvsString.toString();
    }

    public static Map<String, String> json2hgvsMap(List<GenomicFeature> genomicFeatures, String assembly) {
        Map<String, String> hgvs2Assembly = new HashMap<>();
        for (GenomicFeature genomicFeature : genomicFeatures) {
            VWVariant variant = genomicFeature.getVariant();
            if (hgvs2Assembly.containsKey(assembly)) {
                String tmpString = hgvs2Assembly.get(assembly);
                tmpString = tmpString + variant.getHgvs() + "\n";
                hgvs2Assembly.remove(assembly);
                hgvs2Assembly.put(assembly, tmpString);
            } else {
                hgvs2Assembly.put(assembly, variant.getHgvs() + "\n");
            }
        }
        return hgvs2Assembly;
    }

    public static String json2vcf(Map<String, List<GenomicFeature>> genomicMap) {
        StringBuilder vcfString = new StringBuilder();
        vcfString.append("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\n");
        for (String hgmdId: genomicMap.keySet()) {
            for (GenomicFeature genomicFeature : genomicMap.get(hgmdId)) {
                VWVariant variant = genomicFeature.getVariant();
                vcfString.append(variant.getReferenceName()).append("\t").append(variant.getStart()).append("\t").append(hgmdId).append("\t").append(variant.getReferenceBases()).append("\t").append(variant.getAlternateBases()).append("\t.\t.\t.\n");
            }
        }
        return vcfString.toString();
    }

}
