/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.information;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The /information endpoint for VarWatch
 *
 * @author broder
 */
public interface InformationService {

    /**
     * Get the basic information for all datasets given by a user
     *
     * @param header - user token
     * @return
     */
    @GET
    @Path("datasets")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDatasetsByUser(@HeaderParam("Authorization") String header);

    /**
     * Get all variants given by a dataset
     *
     * @param header - user token
     * @param datasetId
     * @return
     */
    @GET
    @Path("datasets/{dataset_id}/variants")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVariants(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId);

    /**
     * Get basic information about a specific dataset
     *
     * @param header - user token
     * @param datasetId
     * @return
     */
    @GET
    @Path("datasets/{dataset_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataset(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId);

    /**
     * Get all variants for a submitted, specific dataset, which could not be
     * parsed by VarWatch
     *
     * @param header - user token
     * @param datasetId
     * @return
     */
    @GET
    @Path("datasets/{dataset_id}/errorvariants")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getErrorVariants(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId);

    @PUT
    @Path("datasets/{dataset_id}/errorvariants")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteErrorVariants(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId);

    /**
     * Get basic information for a specific variant
     *
     * @param header - user token
     * @param variant_id
     * @return
     */
    @GET
    @Path("variants/{variant_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVariantById(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variant_id);

}
