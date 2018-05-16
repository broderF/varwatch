/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.submit;

import com.ikmb.rest.HTTPVarWatchResponse;
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
import com.ikmb.core.data.dataset.DatasetStatusManager;
import com.ikmb.core.data.workflow.job.AnalysisJob;
import com.ikmb.core.data.workflow.job.JobManager;
import com.ikmb.core.varwatchcommons.utils.VarWatchException;
import com.ikmb.rest.util.HTTPTokenConverter;
import com.ikmb.rest.util.TokenRequestFilter.TokenFilter;
import com.ikmb.rest.util.UserActiveRequestFilter.UserActiveFilter;
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
        return parsistSubmissionData(request, header, DatasetBuilder.RawDataType.VCF);
    }

    @POST
    @Path("variant")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitVariant(@HeaderParam("Authorization") String header, @Context HttpServletRequest request) {
        return parsistSubmissionData(request, header, DatasetBuilder.RawDataType.NORMAL);
    }

    @POST
    @Path("hgvs")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitHGVS(@HeaderParam("Authorization") String header, @Context HttpServletRequest request) {
        return parsistSubmissionData(request, header, DatasetBuilder.RawDataType.HGVS);
    }

    private Response parsistSubmissionData(HttpServletRequest request, String header, DatasetBuilder.RawDataType rawDataType) {
        try {
            VWMatchRequest submitRequest = inputConverter.getVWMatchRequest(request, Dataset.class);
            User user = tokenConverter.getUserFromHeader(header);
            AuthClient client = tokenConverter.getClientFromHeader(header);
            Long datasetID = datasetManager.persistRawData(submitRequest, user, client, rawDataType);

            jobManager.createJob(datasetID, AnalysisBuilder.ModuleName.EXTRACT_VARIANTS, null, AnalysisJob.JobAction.NEW.toString());
            statusManager.createNewStatus(datasetID, DatasetStatusBuilder.DatasetStatusType.SUBMITTED);
            return new ResponseBuilder().withVwSuccessful().withVwMessage(HTTPVarWatchResponse.SUBMIT_SUCCESFULL.getDescription()).withStatusType(Response.Status.OK).build();
        } catch (VarWatchException ex) {
            return new ResponseBuilder().withVwError().withVwMessage(ex.getMessage()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();

        }
    }
}
