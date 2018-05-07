/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.submit;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

/**
 * The /submit endpoint for VarWatch
 *
 * @author bfredrich
 */
public interface VarWatch {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("test")
    public Response test(@Context HttpServletRequest request);

    /**
     * Saves the given information as a new vcf dataset
     *
     * @param header
     * @param request
     * @return
     * @throws IOException
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     */
    @POST
    @Path("vcf")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitVCF(@HeaderParam("Authorization") String header, @Context HttpServletRequest request) throws IOException, OAuthSystemException, OAuthProblemException;

    /**
     * Saves the given information as a new hgvs dataset
     *
     * @param header
     * @param request
     * @return
     * @throws IOException
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     */
    @POST
    @Path("hgvs")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitHGVS(@HeaderParam("Authorization") String header, @Context HttpServletRequest request) throws IOException, OAuthSystemException, OAuthProblemException;

    /**
     * Saves the information as a new direct dataset
     *
     * @param header
     * @param request
     * @return
     * @throws IOException
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     */
    @POST
    @Path("variant")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitVariant(@HeaderParam("Authorization") String header, @Context HttpServletRequest request) throws IOException, OAuthSystemException, OAuthProblemException;

}
