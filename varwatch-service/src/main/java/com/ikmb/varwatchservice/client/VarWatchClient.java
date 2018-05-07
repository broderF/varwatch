/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The /client entpoint
 * 
 * @author bfredrich
 */
public interface VarWatchClient {

    /**
     * Checks if the given redirect uri is the uri saved in the database
     * 
     * @param cliendId
     * @param redirectUri
     * @return 
     */
    @GET
    @Path("uri/check")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response isClientUriValid(@QueryParam("client_id") String cliendId, @QueryParam("redirect_uri") String redirectUri);

    /**
     * Returns the name for a given client Id
     * 
     * @param cliendId
     * @return
     */
    @GET
    @Path("name")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getClientName(@QueryParam("client_id") String cliendId);
}
