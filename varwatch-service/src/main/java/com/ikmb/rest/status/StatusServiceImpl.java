/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.status;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.ikmb.core.data.auth.RegistrationResponse;
import com.ikmb.core.data.dataset.Dataset;
import com.ikmb.core.varwatchcommons.entities.Status;
import com.ikmb.rest.util.HTTPTokenValidator;
import com.ikmb.core.data.reference_db.ReferenceDBDataManager;
import com.ikmb.core.data.dataset.DatasetStatusManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.reference_db.RefDatabase;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantStatus;
import com.ikmb.core.data.variant.VariantStatusBuilder;
import com.ikmb.core.data.variant.VariantStatusManager;
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

/**
 *
 * @author broder
 */
@Path("status")
public class StatusServiceImpl {

    @Inject
    private DatasetStatusManager dsStatusManager;
    @Inject
    private VariantStatusManager varStatusManager;
    @Inject
    private VariantStatusBuilder variantStatusBuilder;
    @Inject
    private ReferenceDBDataManager refDBManager;

    @GET
    @Path("datasets/{dataset_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @TokenFilter
    @DataPermissionFilter(dataType = DatasetVW.class)
    public Response getDatasetStatus(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId) {

        //get dataset status
        Status status = dsStatusManager.getLastStatus(datasetId);

        String currentOutput = new Gson().toJson(status, Status.class);

        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @GET
    @Path("datasets/{dataset_id}/all")
    @Produces(MediaType.APPLICATION_JSON)
    @TokenFilter
    @DataPermissionFilter(dataType = DatasetVW.class)
    public Response getAllDatasetStatus(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId) {

        //get dataset status
        List<Status> allStatus = dsStatusManager.getAllStatus(datasetId);

        return new ResponseBuilder().buildList(allStatus);
    }

    @GET
    @Path("variants/{variant_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @TokenFilter
    @DataPermissionFilter(dataType = Variant.class)
    public Response getVariantStatus(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variantId) {

        Status status = varStatusManager.getLastStatus(variantId);

        String currentOutput = new Gson().toJson(status, Status.class);

        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @GET
    @Path("variants/{variant_id}/all")
    @Produces(MediaType.APPLICATION_JSON)
    @TokenFilter
    @DataPermissionFilter(dataType = Variant.class)
    public Response getAllVariantStatus(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variantId) {

        List<Status> allStatus = varStatusManager.getTmpAllStatus(variantId);

        return new ResponseBuilder().buildList(allStatus);
    }

    @GET
    @Path("{status_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVariantStatusById(@HeaderParam("Authorization") String header, @PathParam("status_id") Long statusId) {
        VariantStatus statusSql = varStatusManager.get(statusId);
        Status status = variantStatusBuilder.withStatusSQL(statusSql).build();
        String currentOutput = new Gson().toJson(status, Status.class);
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @GET
    @Path("service")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServiceStatus() {
        RefDatabase refDB = refDBManager.getVarWatchDatabase();
        if (refDB != null && refDB.getName().equals("VarWatch")) {
            return Response.status(Response.Status.OK).entity("service and database running").build();
        } else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("service or database not running").build();
        }
    }
}
