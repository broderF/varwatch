/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.registration;

import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

/**
 * The /registration endpoint for VarWatch
 *
 * @author bfredrich
 */
public interface VarWatchRegistration {

    /**
     * Register a new client
     *
     * @param request
     * @return
     */
    @POST
    @Path("client")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerClient(@Context HttpServletRequest request);

    /**
     * Register a new user
     *
     * @param request
     * @return
     */
    @POST
    @Path("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(@Context HttpServletRequest request);

    @GET
    @Path("test")
    public Response test(@Context HttpServletRequest request);

    /**
     * Validates if a token is valid or not
     *
     * @param header
     * @return
     * @throws OAuthSystemException
     */
    @GET
    @Path("token/validation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isTokenValid(@HeaderParam("Authorization") String header) throws OAuthSystemException;

    /**
     * Returns the user information for a given user
     *
     * @param header
     * @return
     * @throws OAuthSystemException
     */
    @GET
    @Path("user/information")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInformation(@HeaderParam("Authorization") String header) throws OAuthSystemException;

    /**
     * Removes the active token for a given user -> logout
     *
     * @param header
     * @return
     * @throws OAuthSystemException
     */
    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logout(@HeaderParam("Authorization") String header) throws OAuthSystemException;

    /**
     * Sets a new password for a given user
     *
     * @param header
     * @param request
     * @return
     * @throws OAuthSystemException
     */
    @POST
    @Path("password/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setNewPassword(@HeaderParam("Authorization") String header, @Context HttpServletRequest request) throws OAuthSystemException;

    /**
     * Resets a password for a given user and sends this password via mail
     *
     * @param request
     * @return
     * @throws OAuthSystemException
     */
    @POST
    @Path("password/reset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(@Context HttpServletRequest request) throws OAuthSystemException;

    /**
     * Returns a valid token for the given user
     *
     * @param request
     * @param form
     * @return
     * @throws OAuthSystemException
     */
    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response token(@Context HttpServletRequest request, MultivaluedMap<String, String> form) throws OAuthSystemException;

}
