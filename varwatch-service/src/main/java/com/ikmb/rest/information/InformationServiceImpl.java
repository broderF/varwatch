/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.information;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.ikmb.core.data.auth.RegistrationResponse;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.varwatchcommons.entities.Dataset;
import com.ikmb.core.varwatchcommons.entities.RejectedVariant;
import com.ikmb.rest.util.HTTPTokenValidator;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantBuilder;
import com.ikmb.core.data.variant.VariantDataManager;
import com.ikmb.core.data.variant.VariantStatusManager;
import com.ikmb.rest.util.DataPermissionRequestFilter.DataPermissionFilter;
import com.ikmb.rest.util.HTTPVarWatchInputConverter;
import com.ikmb.rest.util.ResponseBuilder;
import com.ikmb.rest.util.TokenRequestFilter.TokenFilter;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
@Path("information")
@TokenFilter
public class InformationServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(InformationServiceImpl.class);

    @Inject
    private HTTPVarWatchInputConverter inputConverter;
    @Inject
    private DatasetManager dsManager;
    @Inject
    private VariantDataManager variantManager;
    @Inject
    private VariantStatusManager variantStatusManager;
    @Inject
    private VariantBuilder variantBuilder;
//    @Inject
//    private HTTPTokenConverter tokenConverter;

    @GET
    @Path("datasets")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDatasetsByUser(@HeaderParam("Authorization") String header) {
        User user = inputConverter.getUserFromHeader(header);
        List<Dataset> simpleDatasetByUserId = dsManager.getSimpleDatasetByUserId(user.getId());
        logger.info("nr of datasets {}",simpleDatasetByUserId.size());
        return new ResponseBuilder().buildList(simpleDatasetByUserId);
    }

    @GET
    @Path("datasets/{dataset_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @DataPermissionFilter(dataType = DatasetVW.class)
    public Response getDataset(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId) {
        Dataset dataset = dsManager.getSimpleDatasetByID(datasetId);
        String currentOutput = new Gson().toJson(dataset, Dataset.class);
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @GET
    @Path("datasets/{dataset_id}/variants")
    @Produces(MediaType.APPLICATION_JSON)
    @DataPermissionFilter(dataType = DatasetVW.class)
    public Response getVariants(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId) {

        List<com.ikmb.core.varwatchcommons.entities.Variant> variants = variantManager.getVariantsByDataset(datasetId);

        return new ResponseBuilder().buildList(variants);
    }

    @GET
    @Path("datasets/{dataset_id}/errorvariants")
    @Produces(MediaType.APPLICATION_JSON)
    @DataPermissionFilter(dataType = DatasetVW.class)
    public Response getErrorVariants(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId) {

        List<RejectedVariant> variants = variantStatusManager.getRejectedVariants(datasetId);

        return new ResponseBuilder().buildList(variants);
    }

    @PUT
    @Path("datasets/{dataset_id}/errorvariants")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteErrorVariants(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @GET
    @Path("variants/{variant_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @DataPermissionFilter(dataType = Variant.class)
    public Response getVariantById(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variantId) {
        logger.info("get variant information by id {}", variantId);

        Variant variantSql = variantManager.get(variantId);
        com.ikmb.core.varwatchcommons.entities.Variant variant = variantBuilder.withVariant(variantSql).build();

        String currentOutput = new Gson().toJson(variant, com.ikmb.core.varwatchcommons.entities.Variant.class);
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

}
