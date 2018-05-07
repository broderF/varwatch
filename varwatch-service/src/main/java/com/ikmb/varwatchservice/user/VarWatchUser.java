/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.user;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

/**
 * The /user endpoint for VarWatch
 *
 * @author bfredrich
 */
public interface VarWatchUser {

    /**
     * Sets a new report schedule for a given user
     *
     * @param header
     * @param reportSchedule
     * @return
     */
    @POST
    @Path("report")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setUserReportSchedule(@HeaderParam("Authorization") String header, @QueryParam("schedule") String reportSchedule);

    /**
     * Updates the user information for a given user
     *
     * @param header
     * @param request
     * @return
     * @throws OAuthSystemException
     */
    @PUT
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@HeaderParam("Authorization") String header, @Context HttpServletRequest request) throws OAuthSystemException;
}
