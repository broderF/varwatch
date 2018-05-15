/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.delete;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.wipe.WipeDataManager;
import com.ikmb.rest.util.HTTPTokenConverter;
import com.ikmb.rest.util.TokenRequestFilter.TokenFilter;
import com.ikmb.rest.util.VarWatchInputConverter;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/wipe")
@TokenFilter
public class VarWatchDeletionImpl  {

    @Inject
    private VarWatchInputConverter inputConverter;
    @Inject
    private HTTPTokenConverter tokenConverter;
    @Inject
    private WipeDataManager wipeDb;

    @GET
    @Path("/user/data")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response wipeUserData(@HeaderParam("Authorization") String header) {

        User user = tokenConverter.getUserFromHeader(header);
        String response = wipeDb.wipeDataByUser(user.getMail());
        return Response.status(Response.Status.OK).entity(new Gson().toJson(response)).build();
    }
}
