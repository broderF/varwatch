/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.status;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The /status endpoint for VarWatch
 *
 * @author broder
 */
public interface StatusService {

    /**
     * Returns the last dataset status for a given dataset
     *
     * @param header
     * @param datasetId
     * @return
     */
    @GET
    @Path("datasets/{dataset_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDatasetStatus(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId);

    /**
     * Returns all dataset status for a given dataset
     *
     * @param header
     * @param datasetId
     * @return
     */
    @GET
    @Path("datasets/{dataset_id}/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDatasetStatus(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId);

    /**
     * Returns the variant status for a given variant
     *
     * @param header
     * @param variantId
     * @return
     */
    @GET
    @Path("variants/{variant_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVariantStatus(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variantId);

    /**
     * Returns all variant status for a given variant
     *
     * @param header
     * @param variantId
     * @return
     */
    @GET
    @Path("variants/{variant_id}/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllVariantStatus(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variantId);

    /**
     * Returns a status for a given status
     *
     * @param header
     * @param statusId
     * @return
     */
    @GET
    @Path("{status_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVariantStatusById(@HeaderParam("Authorization") String header, @PathParam("status_id") Long statusId);

    @GET
    @Path("service")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServiceStatus();
}
