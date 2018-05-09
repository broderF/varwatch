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
import com.ikmb.core.auth.RegistrationResponse;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.varwatchservice.HTTPTokenValidator;
import com.ikmb.core.data.family.FamilyDataManager;
import com.ikmb.core.data.family.GeneFamily;
import com.ikmb.core.data.gene.Gene;
import com.ikmb.core.data.gene.GeneDataManager;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.data.hpo.PhenotypeDataManager;
import com.ikmb.core.data.pathway.Pathway;
import com.ikmb.core.data.pathway.PathwayDataManager;
import com.ikmb.core.data.variant.Variant;
//import com.ikmb.core.varwatchcommons.entities.Family;
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
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(datasetId, DatasetVW.class);
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
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(variantId, Variant.class);
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
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(variantId, Variant.class);
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
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(variantId, Variant.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        List<GeneFamily> families = familyManager.getFamilies(variantId);

        JsonArray result = (JsonArray) new Gson().toJsonTree(families,
                new TypeToken<List<GeneFamily>>() {
                }.getType());
        String currentOutput = result.toString();
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

}
