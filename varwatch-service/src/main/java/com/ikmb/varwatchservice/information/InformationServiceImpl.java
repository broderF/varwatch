/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.information;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.ikmb.core.auth.RegistrationResponse;
import com.ikmb.core.auth.user.User;
import com.ikmb.core.varwatchcommons.entities.Dataset;
import com.ikmb.core.varwatchcommons.entities.RejectedVariant;
import com.ikmb.varwatchservice.HTTPTokenValidator;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantBuilder;
import com.ikmb.core.data.variant.VariantDataManager;
import com.ikmb.core.data.variant.VariantStatusManager;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
@Path("information")
public class InformationServiceImpl implements InformationService {

    private final Logger logger = LoggerFactory.getLogger(InformationServiceImpl.class);

    @Inject
    private HTTPTokenValidator tokenValidator;
    @Inject
    private DatasetManager dsManager;
    @Inject
    private VariantDataManager variantManager;
    @Inject
    private VariantStatusManager variantStatusManager;
    @Inject
    private VariantBuilder variantBuilder;

    @Override
    public Response getDatasetsByUser(String header) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.isTokenValid();
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        User user = tokenValidator.getUser();
        List<Dataset> simpleDatasetByUserId = dsManager.getSimpleDatasetByUserId(user.getId());

        JsonArray result = (JsonArray) new Gson().toJsonTree(simpleDatasetByUserId,
                new TypeToken<List<Dataset>>() {
                }.getType());
        String currentOutput = result.toString();
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response getDataset(String header, Long datasetId) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(datasetId, DatasetVW.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        Dataset dataset = dsManager.getSimpleDatasetByID(datasetId);
        String currentOutput = new Gson().toJson(dataset, Dataset.class);
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response getVariants(String header, Long datasetId) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(datasetId, DatasetVW.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        List<Variant> variants = variantManager.getVariantsByDataset(datasetId);

        JsonArray result = (JsonArray) new Gson().toJsonTree(variants,
                new TypeToken<List<Variant>>() {
                }.getType());
        String currentOutput = result.toString();
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response getErrorVariants(String header, Long datasetId) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(datasetId, DatasetVW.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        List<RejectedVariant> variants = variantStatusManager.getRejectedVariants(datasetId);

        JsonArray result = (JsonArray) new Gson().toJsonTree(variants,
                new TypeToken<List<RejectedVariant>>() {
                }.getType());
        String currentOutput = result.toString();
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response deleteErrorVariants(String header, Long datasetId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getVariantById(String header, Long variantId) {
        logger.info("get variant information by id {}", variantId);
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(variantId, Variant.class);
        if (!tokenValid) {
            logger.error("token is not valid");
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        Variant variantSql = variantManager.get(variantId);
        Variant variant = variantBuilder.withVariant(variantSql).build();

        String currentOutput = new Gson().toJson(variant, Variant.class);
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

}
