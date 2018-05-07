/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.delete;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

/**
 *  The /wipe endpoint for VarWatch
 *  Only used for testing purpose
 * 
 * @author bfredrich
 */

public interface VarWatchDeletion {

    /**
     * Deletes all data from the given user
     * 
     * @param request
     * @return
     * @throws IOException
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     */
    @GET
    @Path("/user/data")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response wipeUserData(@Context HttpServletRequest request) throws IOException, OAuthSystemException, OAuthProblemException;

}
