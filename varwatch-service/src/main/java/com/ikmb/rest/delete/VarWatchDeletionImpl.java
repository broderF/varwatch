/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.delete;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.wipe.WipeDataManager;
import com.ikmb.rest.util.DataPermissionRequestFilter;
import com.ikmb.rest.util.TokenRequestFilter.TokenFilter;
import com.ikmb.rest.util.HTTPVarWatchInputConverter;
import com.ikmb.rest.util.ResponseBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/delete")
@TokenFilter
public class VarWatchDeletionImpl {

    @Inject
    private HTTPVarWatchInputConverter inputConverter;
//    @Inject
//    private HTTPTokenConverter tokenConverter;
    @Inject
    private WipeDataManager wipeDb;

    @POST
    @Path("/dataset/{dataset_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @DataPermissionRequestFilter.DataPermissionFilter(dataType = DatasetVW.class)
    public Response wipeUserData(@HeaderParam("Authorization") String header, @PathParam("dataset_id") Long datasetId) {
        String response = wipeDb.wipeDataByDataset(datasetId);
        return Response.status(Response.Status.OK).entity(new Gson().toJson(response)).build();
    }

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response wipeUserData(@HeaderParam("Authorization") String header) {
        User user = inputConverter.getUserFromHeader(header);
        wipeDb.wipeUser(user.getMail());
        return new ResponseBuilder().withVwSuccessful().build();
    }
}
