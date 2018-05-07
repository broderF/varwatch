/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.matching;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The /matching Endpoint for VarWatch
 *
 * @author broder
 */
public interface MatchingService {

    /**
     * Get the match information for a specific match
     *
     * @param header - user token
     * @param variantD
     * @param matchStatusID
     * @return
     */
    @GET
    @Path("variants/{variant_id}/matches/{match_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMatchingDetails(@HeaderParam("Authorization") String header, @PathParam("variant_id") Long variantD, @PathParam("match_id") Long matchStatusID);

    /**
     * Get the match information for a specific match
     *
     * @param header - user token
     * @param matchStatusID
     * @return
     */
    @GET
    @Path("matches/{match_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMatchingDetail(@HeaderParam("Authorization") String header, @PathParam("match_id") Long matchStatusID);

    /**
     * Get all new matches for a user
     *
     * @param header - user token
     * @return
     */
    @GET
    @Path("new") //news
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNewMatches(@HeaderParam("Authorization") String header);

    /**
     * Acknowledge a given new match. This match will be removed from the new
     * matches list.
     *
     * @param header
     * @param matchStatusID
     * @return
     */
    @GET
    @Path("{match_id}/ack")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setMatchAck(@HeaderParam("Authorization") String header, @PathParam("match_id") Long matchStatusID);

    /**
     * Acknowledge all new matches for a given user. All matches will be
     * removed from the new matches list.
     *
     * @param header
     * @return
     */
    @PUT
    @Path("ack")
    public Response setMatchesAck(@HeaderParam("Authorization") String header);

}
