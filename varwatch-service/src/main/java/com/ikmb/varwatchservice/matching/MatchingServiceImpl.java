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
import com.ikmb.varwatchcommons.entities.MatchInformation;
import com.ikmb.varwatchservice.HTTPTokenValidator;
import com.ikmb.varwatchsql.auth.RegistrationResponse;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.entities.VariantStatusSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.matching.MatchVariantDataManager;
import com.ikmb.varwatchsql.matching.MatchVariantSQL;
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
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(variantID, VariantSQL.class);
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
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(matchID, MatchVariantSQL.class);
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

        UserSQL user = tokenValidator.getUser();
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
        Boolean tokenValid = tokenValidator.hasUserReadPermissions(matchStatusID, MatchVariantSQL.class);
        if (!tokenValid) {
            logger.error("token is not valid");
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        UserSQL user = tokenValidator.getUser();
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

        UserSQL user = tokenValidator.getUser();
        variantmatchingManager.setMatchesAck(user);

        return Response.status(Response.Status.OK).entity("matches acknowledged").build();
    }
}
