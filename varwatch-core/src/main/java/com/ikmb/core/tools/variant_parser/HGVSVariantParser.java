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

/**
 *
 * @author broder
 */
public class HGVSVariantParser implements VariantParser {

    @Inject
    private EnsemblHttpRequestHandler ensemblRequestHandler;

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
        }
        return variants;
    }

}
