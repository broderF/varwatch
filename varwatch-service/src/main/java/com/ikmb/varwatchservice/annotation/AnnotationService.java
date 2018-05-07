/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.annotation;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The /annotation endpoint for VarWatch
 * @author broder
 */
public interface AnnotationService {

    /**
     * Get the basic information for a dataset
     * 
     * @param header - user token
     * @param datasetId
     * @return
     */
    @GET
    @Path("datasets/{dataset_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDatasetAnnotation(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId);

    /**
     * Get the pathways for a variant
     * 
     * @param header - user token
     * @param variantId
     * @return
     */
    @GET
    @Path("variants/{variant_id}/pathways")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVariantPathways(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variantId);

    /**
     * Get the genes for a variant
     * 
     * @param header - user token
     * @param variantId
     * @return
     */
    @GET
    @Path("variants/{variant_id}/genes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVariantGenes(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variantId);

    /**
     * Get the gene families for a variant
     * 
     * @param header - user token
     * @param variantId
     * @return
     */
    @GET
    @Path("variants/{variant_id}/families")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVariantFamilies(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variantId);

}
