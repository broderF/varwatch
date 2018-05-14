/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.matching;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.ikmb.core.data.auth.RegistrationResponse;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.matching.MatchVariant;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.varwatchcommons.entities.MatchInformation;
import com.ikmb.core.varwatchcommons.entities.Variant;
import com.ikmb.varwatchservice.HTTPTokenValidator;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
@Path("matching")
public class MatchingServiceImpl implements MatchingService {

    private final Logger logger = LoggerFactory.getLogger(MatchingServiceImpl.class);

    @Inject
    private HTTPTokenValidator tokenValidator;
    @Inject
    private MatchVariantDataManager variantmatchingManager;

    @Override
    public Response getMatchingDetails(String header, Long variantID, Long matchStatusID) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(variantID, Variant.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        MatchInformation matchInformation = variantmatchingManager.getMatchInformation(variantID, matchStatusID);

        String currentOutput = new Gson().toJson(matchInformation);
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response getMatchingDetail(String header, Long matchID) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(matchID, MatchVariant.class);
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        MatchInformation matchInformation = variantmatchingManager.getMatchInformation(matchID, tokenValidator.getUser());

        String currentOutput = new Gson().toJson(matchInformation);
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response getNewMatches(String header) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.isTokenValid();
        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        User user = tokenValidator.getUser();
        List<MatchInformation> matchInformations = variantmatchingManager.getNewMatchInformation(user);
        JsonArray result = (JsonArray) new Gson().toJsonTree(matchInformations,
                new TypeToken<List<MatchInformation>>() {
                }.getType());
        String currentOutput = result.toString();
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }

    @Override
    public Response setMatchAck(String header, Long matchStatusID) {
        logger.info("ack match with match status id {}", matchStatusID);
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(matchStatusID, MatchVariant.class);
        if (!tokenValid) {
            logger.error("token is not valid");
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        User user = tokenValidator.getUser();
        variantmatchingManager.setMatchAck(matchStatusID, user);

        return Response.status(Response.Status.OK).entity("match acknowledged").build();
    }

    @Override
    public Response setMatchesAck(String header) {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.isTokenValid();
        if (!tokenValid) {
            logger.error("token is not valid");
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        User user = tokenValidator.getUser();
        variantmatchingManager.setMatchesAck(user);

        return Response.status(Response.Status.OK).entity("matches acknowledged").build();
    }
}
