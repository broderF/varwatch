/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.submit;

import com.google.inject.Inject;
import com.ikmb.core.data.auth.RegistrationResponse;
import com.ikmb.core.data.auth.client.AuthClient;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.varwatchcommons.entities.VWMatchRequest;
import com.ikmb.core.varwatchcommons.entities.Dataset;
import com.ikmb.varwatchservice.HTTPTokenValidator;
import com.ikmb.varwatchservice.ResponseBuilder;
import com.ikmb.core.data.workflow.analysis.AnalysisBuilder;
import com.ikmb.core.data.dataset.DatasetBuilder;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetStatusBuilder;
import com.ikmb.varwatchservice.VarWatchInputConverter;
import com.ikmb.varwatchservice.VarWatchInputConverter.HTTPParsingResponse;
import com.ikmb.core.data.dataset.DatasetStatusManager;
import com.ikmb.core.data.workflow.job.AnalysisJob;
import com.ikmb.core.data.workflow.job.JobManager;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

/**
 *
 * @author bfredrich
 */
@Path("submit")
public class VarWatchImpl implements VarWatch {

    @Inject
    private VarWatchInputConverter inputConverter;
    @Inject
    private HTTPTokenValidator tokenValidator;
    @Inject
    private DatasetManager datasetManager;
    @Inject
    private JobManager jobManager;
    @Inject
    private DatasetStatusManager statusManager;

    @Override
    public Response test(HttpServletRequest request) {
        return Response.status(Response.Status.OK).entity("hallo varwatch test").build();
    }

    @Override
    public Response submitVCF(String header, HttpServletRequest request) throws IOException, OAuthSystemException, OAuthProblemException {
        VWMatchRequest submitRequest = null;
        try {
            inputConverter.setHTTPRequest(request, Dataset.class);
            submitRequest = inputConverter.getVWMatchRequest();
        } catch (IOException ex) {
            return new ResponseBuilder().withVwError().withVwMessage(HTTPParsingResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }

        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.isTokenValid();
        if (!tokenValid) {
            return new ResponseBuilder().withVwError().withVwMessage(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }

        User user = tokenValidator.getUser();

        if (!user.getActive()) {
            return new ResponseBuilder().withVwError().withVwMessage(RegistrationResponse.USER_NOT_ACTIVE.getDescription()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }
        AuthClient client = tokenValidator.getClient();
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

    @Override
    public Response submitVariant(String header, HttpServletRequest request) throws IOException, OAuthSystemException, OAuthProblemException {
        VWMatchRequest submitRequest = null;
        try {
            inputConverter.setHTTPRequest(request, Dataset.class);
            submitRequest = inputConverter.getVWMatchRequest();
        } catch (IOException ex) {
            return Response.status(Response.Status.OK).entity(HTTPParsingResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription()).build();
        }

        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.isTokenValid();
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        User user = tokenValidator.getUser();
        
        if (!user.getActive()) {
            return new ResponseBuilder().withVwError().withVwMessage(RegistrationResponse.USER_NOT_ACTIVE.getDescription()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }
        AuthClient client = tokenValidator.getClient();
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

    @Override
    public Response submitHGVS(String header, HttpServletRequest request) throws IOException, OAuthSystemException, OAuthProblemException {
        VWMatchRequest submitRequest = null;
        try {
            inputConverter.setHTTPRequest(request, Dataset.class);
            submitRequest = inputConverter.getVWMatchRequest();
        } catch (IOException ex) {
            return new ResponseBuilder().withVwError().withVwMessage(HTTPParsingResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }

        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.isTokenValid();
        if (!tokenValid) {
            return new ResponseBuilder().withVwError().withVwMessage(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }

        User user = tokenValidator.getUser();
        
        
        if (!user.getActive()) {
            return new ResponseBuilder().withVwError().withVwMessage(RegistrationResponse.USER_NOT_ACTIVE.getDescription()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }
        AuthClient client = tokenValidator.getClient();
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
