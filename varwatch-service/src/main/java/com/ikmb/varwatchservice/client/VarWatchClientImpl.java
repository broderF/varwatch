package com.ikmb.varwatchservice.client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.inject.Inject;
import com.ikmb.varwatchservice.ResponseBuilder;
import com.ikmb.varwatchservice.registration.ClientChecker;
import com.ikmb.varwatchsql.auth.client.AuthClientSQL;
import com.ikmb.varwatchsql.auth.client.ClientManager;
import java.util.logging.Level;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("client")
public class VarWatchClientImpl implements VarWatchClient {

    private static final Logger logger = LoggerFactory.getLogger(VarWatchClientImpl.class);

    @Inject
    private ClientChecker clientChecker;
    @Inject
    private ClientManager clientManager;

    @Override
    public Response isClientUriValid(String clientId, String redirectUri) {
        logger.info("current client id is {} with uri {}", clientId, redirectUri);
        clientChecker.isClientValid(clientId);
        if (!clientChecker.isClientValid(clientId)) {
            String errorMessage = clientChecker.getResponse();
            return new ResponseBuilder().withVwError().withVwMessage(errorMessage).withStatusType(Response.Status.NOT_FOUND).build();
        }
        AuthClientSQL client = clientManager.getClient(clientId);

        if (!client.getRedirect().equals(redirectUri)) {
            return new ResponseBuilder().withVwError().withVwMessage("Redirect Uri is invalid").withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }

        return new ResponseBuilder().withVwSuccessful().withVwMessage("Redirect Uri is valid").withStatusType(Response.Status.OK).build();
    }

    @Override
    public Response getClientName(String clientId) {
        logger.info("current client id is {}", clientId);
        clientChecker.isClientValid(clientId);
        if (!clientChecker.isClientValid(clientId)) {
            String errorMessage = clientChecker.getResponse();
            return new ResponseBuilder().withVwError().withVwMessage(errorMessage).withStatusType(Response.Status.NOT_FOUND).build();
        }
        AuthClientSQL client = clientManager.getClient(clientId);

        JSONObject js = new JSONObject();
        try {
            js.put("client_id", client.getName());
            js.put("client_name", client.getReal_name());
        } catch (JSONException ex) {
            java.util.logging.Logger.getLogger(VarWatchClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.OK).entity(js.toString()).build();
    }

}
