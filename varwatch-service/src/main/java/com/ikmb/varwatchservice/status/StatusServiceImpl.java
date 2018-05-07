/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.status;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.ikmb.varwatchcommons.entities.Dataset;
import com.ikmb.varwatchcommons.entities.Status;
import com.ikmb.varwatchcommons.entities.Variant;
import com.ikmb.varwatchservice.HTTPTokenValidator;
import com.ikmb.varwatchsql.auth.RegistrationResponse;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.entities.VariantStatusSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.data.reference_db.RefDatabaseSQL;
import com.ikmb.varwatchsql.data.reference_db.ReferenceDBDataManager;
import com.ikmb.varwatchsql.status.dataset.DatasetStatusManager;
import com.ikmb.varwatchsql.status.variant.VariantStatusBuilder;
import com.ikmb.varwatchsql.status.variant.VariantStatusManager;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author broder
 */
@Path("status")
public class StatusServiceImpl implements StatusService {

    @Inject
    private HTTPTokenValidator tokenValidator;
    @Inject
    private DatasetStatusManager dsStatusManager;
    @Inject
    private VariantStatusManager varStatusManager;
    @Inject
    private VariantStatusBuilder variantStatusBuilder;
    @Inject
    private ReferenceDBDataManager refDBManager;

    @Override
    public Response getDatasetStatus(String header, Long datasetId) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(datasetId, DatasetVWSQL.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        //get dataset status
        Status status = dsStatusManager.getLastStatus(datasetId);

        String currentOutput = new Gson().toJson(status, Status.class);

        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response getAllDatasetStatus(String header, Long datasetId) {
        //get token and validate
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(datasetId, DatasetVWSQL.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        //get dataset status
        List<Status> allStatus = dsStatusManager.getAllStatus(datasetId);

        JsonArray result = (JsonArray) new Gson().toJsonTree(allStatus,
                new TypeToken<List<Dataset>>() {
                }.getType());
        String currentOutput = result.toString();

        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response getVariantStatus(String header, Long variantId) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(variantId, VariantSQL.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        Status status = varStatusManager.getLastStatus(variantId);

        String currentOutput = new Gson().toJson(status, Status.class);

        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response getAllVariantStatus(String header, Long variantId) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(variantId, VariantSQL.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        List<Status> allStatus = varStatusManager.getTmpAllStatus(variantId);

        JsonArray result = (JsonArray) new Gson().toJsonTree(allStatus,
                new TypeToken<List<Variant>>() {
                }.getType());
        String currentOutput = result.toString();

        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response getVariantStatusById(String header, Long statusId) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.isTokenValid();
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }
        VariantStatusSQL statusSql = varStatusManager.get(statusId);
        Status status = variantStatusBuilder.withStatusSQL(statusSql).build();
        String currentOutput = new Gson().toJson(status, Status.class);
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response getServiceStatus() {
        RefDatabaseSQL refDB = refDBManager.getVarWatchDatabase();
        if (refDB != null && refDB.getName().equals("VarWatch")){
        return Response.status(Response.Status.OK).entity("service and database running").build();
        } else{
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("service or database not running").build();
        }
    }
}
