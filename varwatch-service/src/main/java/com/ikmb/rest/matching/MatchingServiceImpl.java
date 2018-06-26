/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.matching;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.matching.MatchVariant;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.varwatchcommons.entities.MatchInformation;
import com.ikmb.core.varwatchcommons.entities.Variant;
import com.ikmb.rest.util.DataPermissionRequestFilter.DataPermissionFilter;
import com.ikmb.rest.util.HTTPVarWatchInputConverter;
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
@Path("matching")
@TokenFilter
public class MatchingServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(MatchingServiceImpl.class);

    @Inject
    private HTTPVarWatchInputConverter inputConverter;
    @Inject
    private MatchVariantDataManager variantmatchingManager;

    @GET
    @Path("variants/{variant_id}/matches/{match_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @DataPermissionFilter(dataType = Variant.class)
    public Response getMatchingDetails(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variantID, @PathParam("match_id") Long matchStatusID) {

        MatchInformation matchInformation = variantmatchingManager.getMatchInformation(variantID, matchStatusID);

        String currentOutput = new Gson().toJson(matchInformation);
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @GET
    @Path("matches/{match_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @DataPermissionFilter(dataType = MatchVariant.class)
    public Response getMatchingDetail(@HeaderParam("Authorization") String header, @PathParam("match_id") Long matchID) {

        MatchInformation matchInformation = variantmatchingManager.getMatchInformation(matchID, inputConverter.getUserFromHeader(header));

        String currentOutput = new Gson().toJson(matchInformation);
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @GET
    @Path("new") //news
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNewMatches(@HeaderParam("Authorization") String header) {

        User user = inputConverter.getUserFromHeader(header);
        List<MatchInformation> matchInformations = variantmatchingManager.getNewMatchInformation(user);
        JsonArray result = (JsonArray) new Gson().toJsonTree(matchInformations,
                new TypeToken<List<MatchInformation>>() {
                }.getType());
        String currentOutput = result.toString();
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @GET
    @Path("{match_id}/ack")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setMatchAck(@HeaderParam("Authorization") String header, @PathParam("match_id") Long matchStatusID) {
        logger.info("ack match with match status id {}", matchStatusID);

        User user = inputConverter.getUserFromHeader(header);
        variantmatchingManager.setMatchAck(matchStatusID, user);

        return Response.status(Response.Status.OK).entity("match acknowledged").build();
    }

    @PUT
    @Path("ack")
    public Response setMatchesAck(@HeaderParam("Authorization") String header) {

        User user = inputConverter.getUserFromHeader(header);
        variantmatchingManager.setMatchesAck(user);

        return Response.status(Response.Status.OK).entity("matches acknowledged").build();
    }

    public void setVariantmatchingManager(MatchVariantDataManager variantmatchingManager) {
        this.variantmatchingManager = variantmatchingManager;
    }

    public void setInputConverter(HTTPVarWatchInputConverter inputConverter) {
        this.inputConverter = inputConverter;
    }
    
    

}
