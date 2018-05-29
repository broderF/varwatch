/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.annotation;

import com.google.inject.Inject;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.family.GeneFamily;
import com.ikmb.core.data.gene.Gene;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.data.pathway.Pathway;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantDataManager;
import com.ikmb.rest.util.DataPermissionRequestFilter.DataPermissionFilter;
import com.ikmb.rest.util.ResponseBuilder;
import com.ikmb.rest.util.TokenRequestFilter.TokenFilter;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("annotation")
@TokenFilter
public class AnnotationService {

//    @Inject
    private DatasetManager datasetManager;
//    @Inject
    private VariantDataManager variantManager;

    @GET
    @Path("datasets/{dataset_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @DataPermissionFilter(dataType = DatasetVW.class)
    public Response getDatasetAnnotation(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId) {
        List<HPOTerm> hpoTerms = datasetManager.getPhenotypes(datasetId);
        return new ResponseBuilder().buildListWithExpose(hpoTerms);
    }

    @GET
    @Path("variants/{variant_id}/pathways")
    @Produces(MediaType.APPLICATION_JSON)
    @DataPermissionFilter(dataType = Variant.class)
    public Response getVariantPathways(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variantId) {
        List<Pathway> pathway = variantManager.getPathwaysFromVariant(variantId);
        return new ResponseBuilder().buildListWithExpose(pathway);
    }

    @GET
    @Path("variants/{variant_id}/genes")
    @Produces(MediaType.APPLICATION_JSON)
    @DataPermissionFilter(dataType = Variant.class)
    public Response getVariantGenes(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variantId) {
        List<Gene> genes = variantManager.getGenesFromVariant(variantId);
        return new ResponseBuilder().buildListWithExpose(genes);
    }

    @GET
    @Path("variants/{variant_id}/families")
    @Produces(MediaType.APPLICATION_JSON)
    @DataPermissionFilter(dataType = Variant.class)
    public Response getVariantFamilies(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variantId) {
        List<GeneFamily> families = variantManager.getFamiliesFromVariant(variantId);
        return new ResponseBuilder().buildListWithExpose(families);
    }

    @Inject
    public void setDatasetManager(DatasetManager datasetManager) {
        this.datasetManager = datasetManager;
    }

    @Inject
    public void setVariantManager(VariantDataManager variantManager) {
        this.variantManager = variantManager;
    }
}
