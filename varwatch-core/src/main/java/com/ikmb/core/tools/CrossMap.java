/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.tools;

import com.ikmb.core.data.variant.Variant;
import java.util.Optional;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class CrossMap {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(CrossMap.class);

    private final EnsemblHttpRequestHandler ensemblHttpRequestHandler = new EnsemblHttpRequestHandler();

    public CrossMap() {

    }

    public Optional<Variant> run(Variant variant, String assemblyFrom, String assemblyTo) {
        String restVariant = buildRestVariant(variant.getChromosomeName(), variant.getChromosomePos(), assemblyFrom, assemblyTo);

        String ensemblResponse = ensemblHttpRequestHandler.sendHttpRequest(restVariant);
        Variant variantFromResponse = getVariantFromResponse(ensemblResponse);
        return mergeResponseAndQuery(variantFromResponse,variant);
    }

    public String buildRestVariant(String chromosome, int position, String assemblyFrom, String assemblyTo) {
        String ext = "/map/human/" + assemblyFrom + "/" + chromosome + ":" + position + ".." + position + ":1/" + assemblyTo;
        return ext;
    }

    public Variant getVariantFromResponse(String response) {
        Variant variant = null;
        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray allMappings = jsonObject.getJSONArray("mappings");
            JSONObject firstMapping = allMappings.getJSONObject(0);
            JSONObject mappedVariant = firstMapping.getJSONObject("mapped");
//            JSONObject mappedObject = obj.g("mapped").getJSONObject(0);
            if (mappedVariant != null) {
                String chromosome = mappedVariant.getString("seq_region_name");
                int position = mappedVariant.getInt("start");
                variant = new Variant();
                variant.setChromosomeName(chromosome);
                variant.setChromosomePos(position);
            }
        } catch (JSONException ex) {
            logger.error("Error while parsing crossmap results", ex);
        }
        return variant;
    }

    private Optional<Variant> mergeResponseAndQuery(Variant variantFromResponse, Variant variant) {
        variant.setChromosomePos(variantFromResponse.getChromosomePos());
        return Optional.of(variant);
    }

}
