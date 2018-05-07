/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.annotation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.ikmb.varwatchcommons.entities.Family;
import com.ikmb.varwatchcommons.entities.Gene;
import com.ikmb.varwatchcommons.entities.HPOTerm;
import com.ikmb.varwatchcommons.entities.Pathway;
import com.ikmb.varwatchservice.HTTPTokenValidator;
import com.ikmb.varwatchsql.auth.RegistrationResponse;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.data.family.FamilyDataManager;
import com.ikmb.varwatchsql.data.gene.GeneDataManager;
import com.ikmb.varwatchsql.data.hpo.PhenotypeDataManager;
import com.ikmb.varwatchsql.data.pathway.PathwayDataManager;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("annotation")
public class AnnotationServiceImpl implements AnnotationService {

    @Inject
    private HTTPTokenValidator tokenValidator;
    @Inject
    private PhenotypeDataManager phenotypeManager;
    @Inject
    private PathwayDataManager pathwayManager;
    @Inject
    private GeneDataManager geneManager;
    @Inject
    private FamilyDataManager familyManager;

    @Override
    public Response getDatasetAnnotation(String header, Long datasetId) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(datasetId, DatasetVWSQL.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        List<HPOTerm> hpoTerms = phenotypeManager.getPhenotypes(datasetId);

        JsonArray result = (JsonArray) new Gson().toJsonTree(hpoTerms,
                new TypeToken<List<HPOTerm>>() {
                }.getType());
        String currentOutput = result.toString();
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response getVariantPathways(String header, Long variantId) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(variantId, VariantSQL.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        List<Pathway> pathway = pathwayManager.getPathways(variantId);

        JsonArray result = (JsonArray) new Gson().toJsonTree(pathway,
                new TypeToken<List<Pathway>>() {
                }.getType());
        String currentOutput = result.toString();
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response getVariantGenes(String header, Long variantId) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(variantId, VariantSQL.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        List<Gene> genes = geneManager.getGenes(variantId);

        JsonArray result = (JsonArray) new Gson().toJsonTree(genes,
                new TypeToken<List<Gene>>() {
                }.getType());
        String currentOutput = result.toString();
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response getVariantFamilies(String header, Long variantId) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(variantId, VariantSQL.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        List<Family> families = familyManager.getFamilies(variantId);

        JsonArray result = (JsonArray) new Gson().toJsonTree(families,
                new TypeToken<List<Family>>() {
                }.getType());
        String currentOutput = result.toString();
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

}
