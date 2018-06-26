/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.tools.variant_parser;

import com.google.inject.Inject;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.tools.EnsemblHttpRequestHandler;
import com.ikmb.core.varwatchcommons.entities.GenomicFeature;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author broder
 */
public class HGVSVariantParser implements VariantParser {

    private EnsemblHttpRequestHandler ensemblRequestHandler;

    @Inject
    public void setEnsemblRequestHandler(EnsemblHttpRequestHandler ensemblRequestHandler) {
        this.ensemblRequestHandler = ensemblRequestHandler;
    }

//    https://rest.ensembl.org/vep/human/hgvs/AGT:c.803T%3EC?content-type=application/json
//    http://grch37.rest.ensembl.org/vep/human/hgvs/AGT:c.803T%3EF?content-type=application/json
    @Override
    public List<Variant> getVariants(DatasetVW dataset) {
        String rawDataAssembly = dataset.getRawDataAssembly();
        ensemblRequestHandler.setServerByAssembly(rawDataAssembly);

        List<GenomicFeature> tmpfeatures = com.ikmb.core.utils.VariantParser.getVariantInfoFromByteArray(dataset.getRawData());
        List<Variant> variants = new ArrayList<>();
        String basicCall = "/vep/human/hgvs/";

        for (GenomicFeature genomicFeature : tmpfeatures) {
            String hgvs = genomicFeature.getVariant().getHgvs();
            String sendHttpRequest = ensemblRequestHandler.sendHttpRequest(basicCall + hgvs);
            System.out.println(sendHttpRequest);
            Variant variant = getVariantFromResponse(genomicFeature, sendHttpRequest);
            variants.add(variant);
        }
        return variants;
    }

    private Variant getVariantFromResponse(GenomicFeature genomicFeature, String vepResponse) {
        Variant variant = new Variant();
        variant.setVEPIdentifier(genomicFeature.getVariant().getHgvs());
        variant.setFilter("not pass:cant convert hgvs");
        if (vepResponse == null) {
            return variant;
        }
        try {
            JSONArray jsonArray = new JSONArray(vepResponse);
            if (jsonArray.length() > 0) {
                JSONObject firstObject = jsonArray.getJSONObject(0);
                String chrom = firstObject.getString("seq_region_name");
                Integer pos = firstObject.getInt("start");
                variant.setChromosomeName(chrom);
                variant.setChromosomePos(pos);

                String allelesString = firstObject.getString("allele_string");
                String[] split = allelesString.split("/");
                if (split.length == 2) {
                    variant.setReferenceBase(split[0]);
                    variant.setAlternateBase(split[1]);
                }
                variant.setFilter("pass");
                return variant;
            }

        } catch (JSONException ex) {
            Logger.getLogger(HGVSVariantParser.class.getName()).log(Level.SEVERE, null, ex);

        }
        return variant;
    }

}
