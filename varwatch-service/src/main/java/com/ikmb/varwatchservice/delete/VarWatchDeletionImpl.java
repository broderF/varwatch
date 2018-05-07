/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.delete;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.ikmb.varwatchsql.auth.RegistrationResponse;
import com.ikmb.varwatchsql.auth.token.TokenManager;
import com.ikmb.varwatchsql.wipe.WipeDataManager;
import com.ikmb.varwatchcommons.entities.VWMatchRequest;
import com.ikmb.varwatchservice.VarWatchInputConverter;
import com.ikmb.varwatchservice.VarWatchInputConverter.HTTPParsingResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

@Path("/wipe")
public class VarWatchDeletionImpl implements VarWatchDeletion {

    @Inject
    private VarWatchInputConverter inputConverter;
    @Inject
    private TokenManager tokenManager;
    @Inject
    private WipeDataManager wipeDb;

    @Override
    public Response wipeUserData(HttpServletRequest request) throws IOException, OAuthSystemException, OAuthProblemException {
        try {
            inputConverter.setHTTPRequest(request, VWMatchRequest.class);
        } catch (IOException ex) {
            return Response.status(Response.Status.OK).entity(HTTPParsingResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription()).build();
        }

        //get token and validate
        OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);
        String accessToken = oauthRequest.getAccessToken();
        boolean tokenValid = tokenManager.isTokenValid(accessToken);
        String mail = tokenManager.getUserByToken(accessToken).getMail();

        if (!tokenValid) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.TOKEN_NOT_VALID.getDescription()).build();
        }

        String response = wipeDb.wipeDataByUser(mail);
        return Response.status(Response.Status.OK).entity(new Gson().toJson(response)).build();
    }
}
