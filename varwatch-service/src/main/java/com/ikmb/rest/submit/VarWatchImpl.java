/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.submit;

import com.google.inject.Inject;
import com.ikmb.core.data.auth.client.AuthClient;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.varwatchcommons.entities.VWMatchRequest;
import com.ikmb.core.varwatchcommons.entities.Dataset;
import com.ikmb.rest.util.ResponseBuilder;
import com.ikmb.core.data.workflow.analysis.AnalysisBuilder;
import com.ikmb.core.data.dataset.DatasetBuilder;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetStatusBuilder;
import com.ikmb.rest.util.VarWatchInputConverter;
import com.ikmb.rest.util.VarWatchInputConverter.HTTPParsingResponse;
import com.ikmb.core.data.dataset.DatasetStatusManager;
import com.ikmb.core.data.workflow.job.AnalysisJob;
import com.ikmb.core.data.workflow.job.JobManager;
import com.ikmb.rest.util.HTTPTokenConverter;
import com.ikmb.rest.util.TokenRequestFilter.TokenFilter;
import com.ikmb.rest.util.UserActiveRequestFilter.UserActiveFilter;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author bfredrich
 */
@Path("submit")
@TokenFilter
@UserActiveFilter
public class VarWatchImpl {

    @Inject
    private VarWatchInputConverter inputConverter;
    @Inject
    private HTTPTokenConverter tokenConverter;
    @Inject
    private DatasetManager datasetManager;
    @Inject
    private JobManager jobManager;
    @Inject
    private DatasetStatusManager statusManager;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("test")
    public Response test(@Context HttpServletRequest request) {
        return Response.status(Response.Status.OK).entity("hallo varwatch test").build();
    }

    @POST
    @Path("vcf")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitVCF(@HeaderParam("Authorization") String header, @Context HttpServletRequest request) {
        VWMatchRequest submitRequest;
        try {
            submitRequest = inputConverter.getVWMatchRequest(request, Dataset.class);
        } catch (IOException ex) {
            return new ResponseBuilder().withVwError().withVwMessage(HTTPParsingResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }

        User user = tokenConverter.getUserFromHeader(header);
        AuthClient client = tokenConverter.getClientFromHeader(header);
        Long datasetID = datasetManager.persistRawData(submitRequest, user, client, DatasetBuilder.RawDataType.VCF);
        String response = SubmitResponse.SUBMIT_SUCCESFULL.getDescription();
        if (datasetID == null) {
            response = datasetManager.getErrorResponse();
            return new ResponseBuilder().withVwError().withVwMessage(response).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }

        jobManager.createJob(datasetID, AnalysisBuilder.ModuleName.EXTRACT_VARIANTS, null, AnalysisJob.JobAction.NEW.toString());
        statusManager.createNewStatus(datasetID, DatasetStatusBuilder.DatasetStatusType.SUBMITTED);
        return new ResponseBuilder().withVwSuccessful().withVwMessage(response).withStatusType(Response.Status.OK).build();

    }

    @POST
    @Path("variant")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitVariant(@HeaderParam("Authorization") String header, @Context HttpServletRequest request) {
        VWMatchRequest submitRequest;
        try {
            submitRequest = inputConverter.getVWMatchRequest(request, Dataset.class);
        } catch (IOException ex) {
            return new ResponseBuilder().withVwError().withVwMessage(HTTPParsingResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }

        User user = tokenConverter.getUserFromHeader(header);
        AuthClient client = tokenConverter.getClientFromHeader(header);
        Long datasetID = datasetManager.persistRawData(submitRequest, user, client, DatasetBuilder.RawDataType.NORMAL);
        String response = SubmitResponse.SUBMIT_SUCCESFULL.getDescription();
        if (datasetID == null) {
            response = datasetManager.getErrorResponse();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
        }

        jobManager.createJob(datasetID, AnalysisBuilder.ModuleName.EXTRACT_VARIANTS, null, AnalysisJob.JobAction.NEW.toString());
        statusManager.createNewStatus(datasetID, DatasetStatusBuilder.DatasetStatusType.SUBMITTED);
        return Response.status(Response.Status.OK).entity(SubmitResponse.SUBMIT_SUCCESFULL.getDescription()).build();
    }

    @POST
    @Path("hgvs")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitHGVS(@HeaderParam("Authorization") String header, @Context HttpServletRequest request) {
        VWMatchRequest submitRequest;
        try {
            submitRequest = inputConverter.getVWMatchRequest(request, Dataset.class);
        } catch (IOException ex) {
            return new ResponseBuilder().withVwError().withVwMessage(HTTPParsingResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }

        User user = tokenConverter.getUserFromHeader(header);
        AuthClient client = tokenConverter.getClientFromHeader(header);
        Long datasetID = datasetManager.persistRawData(submitRequest, user, client, DatasetBuilder.RawDataType.HGVS);
        String response = SubmitResponse.SUBMIT_SUCCESFULL.getDescription();
        if (datasetID == null) {
            response = datasetManager.getErrorResponse();
            return new ResponseBuilder().withVwError().withVwMessage(response).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }

        jobManager.createJob(datasetID, AnalysisBuilder.ModuleName.EXTRACT_VARIANTS, null, AnalysisJob.JobAction.NEW.toString());
        statusManager.createNewStatus(datasetID, DatasetStatusBuilder.DatasetStatusType.SUBMITTED);
        return new ResponseBuilder().withVwSuccessful().withVwMessage(response).withStatusType(Response.Status.OK).build();
    }
}
