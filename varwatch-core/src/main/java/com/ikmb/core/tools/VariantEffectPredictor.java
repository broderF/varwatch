/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.tools;

import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.varianteffect.VariantEffect;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class VariantEffectPredictor {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(VariantEffectPredictor.class);
    private EnsemblHttpRequestHandler ensemblHttpRequestHandler = new EnsemblHttpRequestHandler();
    private final static List<String> parameters = new ArrayList<>();

    static {
        parameters.add("canonical=true");
        parameters.add("ccds=true");
        parameters.add("hgvs=true");

    }

    public List<VariantEffect> run(Variant variant) {
        int refBaseLength = variant.getReferenceBase().isEmpty() ? 0 : variant.getReferenceBase().length() - 1;
        int endPos = variant.getChromosomePos()+ refBaseLength;
        String restCall = "/vep/human/region/" + variant.getChromosomeName() + ":" + variant.getChromosomePos() + ":" + endPos + "/" + variant.getAlternateBase() + "?";
        StringJoiner stringjoiner = new StringJoiner("&");
        parameters.forEach(element -> stringjoiner.add(element));
        String parameter = stringjoiner.toString();
        System.out.println(restCall + parameter);
        String response = ensemblHttpRequestHandler.sendHttpRequest(restCall + parameter);
        return getVariantEffectFromResponse(response);
    }

    public List<VariantEffect> getVariantEffectFromResponse(String response) {
        List<VariantEffect> variantEffects = new ArrayList<>();
        try {
            System.out.println(response);
            JSONObject result = new JSONArray(response).getJSONObject(0);
            String mostSevereConseq = result.getString("most_severe_consequence");
            JSONArray consequences = result.getJSONArray("transcript_consequences");
            for (int i = 0; i < consequences.length(); i++) {
                JSONObject currentConseq = consequences.getJSONObject(i);
                JSONArray conseqTerms = currentConseq.getJSONArray("consequence_terms");
                for (int j = 0; j < conseqTerms.length(); j++) {
                    String currentConseqTerm = conseqTerms.getString(j);
                    if (currentConseqTerm.equals(mostSevereConseq)) {
                        variantEffects.add(getVariantEffect(result, currentConseq, currentConseqTerm));
                        break;
                    }
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
            logger.error("Error while parsing crossmap results", ex);
        }
        return variantEffects;
    }

    private VariantEffect getVariantEffect(JSONObject input, JSONObject currentConseq, String consequence) {
        VariantEffect variantEffect = new VariantEffect();
        try {
            variantEffect.setUploaded_variation(input.getString("input"));
            variantEffect.setConsequence(consequence);
            variantEffect.setImpactFactor(currentConseq.getString("impact"));
            if (currentConseq.has("canonical")) {
                variantEffect.setCanonicalTranscript("1".equals(currentConseq.getString("canonical")));
            }
            variantEffect.setHgvs_c(currentConseq.getString("hgvsc"));
            variantEffect.setFeatureName(currentConseq.getString("transcript_id"));

            if (currentConseq.has("cdna_start")) {
                variantEffect.setCds_start(currentConseq.getInt("cdna_start"));
            }
            if (currentConseq.has("cdna_end")) {
                variantEffect.setCds_end(currentConseq.getInt("cdna_end"));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
            Logger.getLogger(VariantEffectPredictor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return variantEffect;
    }

}
