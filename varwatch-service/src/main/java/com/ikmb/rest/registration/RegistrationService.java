/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.registration;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.ikmb.core.data.auth.RegistrationResponse;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.auth.user.UserBuilder;
import com.ikmb.core.data.auth.user.UserManager;
import com.ikmb.core.varwatchcommons.entities.VWResponse;
import com.ikmb.core.varwatchcommons.entities.DefaultUser;
import com.ikmb.core.varwatchcommons.entities.RegistrationUser;
import com.ikmb.core.varwatchcommons.entities.Client;
import com.ikmb.core.varwatchcommons.entities.PasswordReset;
import com.ikmb.core.notification.NotificationSubmitter;
import com.ikmb.core.utils.PdfCreator;
import com.ikmb.core.utils.VarWatchException;
import com.ikmb.rest.HTTPVarWatchResponse;
import com.ikmb.rest.util.HTTPTokenValidator;
import com.ikmb.rest.util.OAuthRequestWrapper;
import com.ikmb.rest.util.ResponseBuilder;
import com.ikmb.rest.util.HTTPVarWatchInputConverter;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bfredrich
 */
@Path("registration")
public class RegistrationService {

    private Integer expiresIn = 3600;

    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

//    @Context
//    ServletContext context;
    @Inject
    private HTTPVarWatchInputConverter inputConverter;
    @Inject
    private RegistrationManager registrationManager;
    @Inject
    private ClientChecker clientChecker;
    @Inject
    private UserChecker userChecker;
    @Inject
    private TokenCreator tokenCreator;
    @Inject
    private HTTPTokenValidator tokenValidator;
    @Inject
    private UserManager userManager;
    @Inject
    private UserBuilder userBuilder;

    @POST
    @Path("client")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerClient(@Context HttpServletRequest request) {
        try {
            inputConverter.setHTTPRequest(request, Client.class);
        } catch (VarWatchException ex) {
            return Response.status(Response.Status.OK).entity(HTTPVarWatchResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription()).build();
        }

        Client client;
        try {
            client = inputConverter.getVWClient();
        } catch (VarWatchException ex) {
            return Response.status(Response.Status.OK).entity(HTTPVarWatchResponse.HTTP_CLIENT_NOT_PARSABLE.getDescription()).build();
        }
        String response = registrationManager.saveClient(client);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @POST
    @Path("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(@Context HttpServletRequest request) {
        RegistrationUser contact;
        try {
            contact = inputConverter.getRegistrationUser(request);
        } catch (IOException ex) {
            VWResponse response = new VWResponse();
            response.setMessage("Error");
            response.setMessage(HTTPVarWatchResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription());
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
        }

        Response response = registrationManager.saveUser(contact);
        VWResponse readEntity = (VWResponse) response.getEntity();
//        if (readEntity.getMessage().equals(REGISTRATION_SUCCESFULL.getMessage()) && ServletConfig.database != null && ServletConfig.database.equals("varwatch")) {
        logger.info("Send mail");
        String filePath = PdfCreator.createPdfFromContact(contact);
        logger.info("filepath: " + filePath);
        String firstName = contact.getFirstName();
        String lastName = contact.getLastName();
        String title = "Dear " + firstName + " " + lastName + ",";
        String text = title + "\n"
                + "\n"
                + "Thank you for registering with VarWatch!\n"
                + "\n"
                + "Your account will be activated once you have authenticated yourself. To this end, please sign the attached form and return to the address given.\n"
                + "\n"
                + "With kind regards,\n"
                + "Your VarWatch Team";
        NotificationSubmitter.sendMail(contact.getMail(), text, "VarWatch Registration", filePath);
//        }else{
//            logger.info("no registration mail sent");
//            logger.info("Message:"+readEntity.getMessage());
//            logger.info("db:"+ServletConfig.database);
//        }
        return response;
    }

    @GET
    @Path("test")
    public Response test(@Context HttpServletRequest request) {
        return Response.status(Response.Status.OK).entity("hallo varwatch registration test22").build();
    }

    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response token(@Context HttpServletRequest request, MultivaluedMap<String, String> form) throws OAuthSystemException {
        OAuthTokenRequest oauthRequest = null;
        try {
            oauthRequest = new OAuthTokenRequest(new OAuthRequestWrapper(request, form));
        } catch (OAuthProblemException ex) {
            OAuthResponse res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(ex)
                    .buildJSONMessage();
            return new ResponseBuilder().withVwError().withVwMessage(ex.getDescription()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }

        if (!clientChecker.isClientValid(oauthRequest.getClientId())) {
            String errorMessage = clientChecker.getResponse();
            return new ResponseBuilder().withVwError().withVwMessage(errorMessage).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
//                return ResponseBuilder.buildInvalidClientIdResponse(errorMessage);
        }

        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        String accessToken = oauthIssuerImpl.accessToken();
        if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {

        } else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.PASSWORD.toString())) {
            boolean userValid = userChecker.isUserValid(oauthRequest.getUsername(), oauthRequest.getPassword());
            if (!userValid) {
                return new ResponseBuilder().withVwError().withVwMessage(userChecker.getResponse()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
            }
        }
        accessToken = tokenCreator.getValidToken(accessToken, expiresIn, oauthRequest.getUsername(), oauthRequest.getClientId());
        OAuthResponse response = OAuthASResponse
                .tokenResponse(HttpServletResponse.SC_OK)
                .setAccessToken(accessToken)
                .setExpiresIn(expiresIn.toString())
                .buildJSONMessage();
        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();

    }

    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logout(@HeaderParam("Authorization") String header) throws OAuthSystemException {
        tokenValidator.setHeader(header);
        Boolean deleted = tokenValidator.deleteToken();
        if (!deleted) {
            return new ResponseBuilder().withVwError().withVwMessage(RegistrationResponse.TOKEN_DELETION_ERROR.getDescription()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        } else {
            return new ResponseBuilder().withVwSuccessful().withVwMessage(RegistrationResponse.TOKEN_DELETION_SUCCESFULL.getDescription()).withStatusType(Response.Status.OK).build();
        }
    }

    @GET
    @Path("token/validation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isTokenValid(@HeaderParam("Authorization") String header) throws OAuthSystemException {

        if (tokenValidator.isTokenValid(header)) {
            VWResponse curResponse = new VWResponse();
            curResponse.setMessage("Successful");
            curResponse.setDescription("Token is valid");
            return Response.status(Response.Status.OK).entity(curResponse).build();
        } else {
            VWResponse curResponse = new VWResponse();
            curResponse.setMessage("Error");
            curResponse.setDescription("Token is not valid");
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(curResponse).build();
        }
    }

    @GET
    @Path("user/information")
    @Produces(MediaType.APPLICATION_JSON)
//    @PermissionFilter
//    @Priority(Priorities.AUTHENTICATION)
    public Response getUserInformation(@HeaderParam("Authorization") String header) throws OAuthSystemException {
        logger.info("user information request");

        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.isTokenValid();
        if (!tokenValid) {
            VWResponse response = new VWResponse();
            response.setMessage("Error");
            response.setDescription("Token is not valid");
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
        }

        User user = tokenValidator.getUser();
        DefaultUser contact = userBuilder.withUser(user).buildVWContactWoPassword();
        String contactJson = new Gson().toJson(contact, DefaultUser.class);
        return Response.status(Response.Status.ACCEPTED).entity(contactJson).build();
    }

    @POST
    @Path("password/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setNewPassword(@HeaderParam("Authorization") String header, @Context HttpServletRequest request) throws OAuthSystemException {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.isTokenValid();
        if (!tokenValid) {
            VWResponse response = new VWResponse();
            response.setMessage("Error");
            response.setDescription("Token is not valid");
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
        }

        User user = tokenValidator.getUser();
        String newPw = null;
        String oldPw = null;
        try {
            inputConverter.setHTTPRequest(request, PasswordReset.class);
            PasswordReset resetPW = inputConverter.getPasswordReset();
            newPw = resetPW.getPassword();
            oldPw = resetPW.getOldPassword();
        } catch (VarWatchException ex) {
            VWResponse response = new VWResponse();
            response.setMessage("Error");
            response.setDescription(HTTPVarWatchResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription());
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
        }
        if (!userChecker.isUserValid(user.getMail(), oldPw)) {
            return new ResponseBuilder().withVwError().withVwMessage(userChecker.getResponse()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        }
        userManager.setNewPassword(user.getId(), newPw);

        VWResponse response = new VWResponse();
        response.setMessage("Successful");
        response.setDescription(RegistrationResponse.PASSWORD_CHANGED.getDescription());
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @POST
    @Path("password/reset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(@Context HttpServletRequest request) throws OAuthSystemException {
        String contact = null;
        try {
            inputConverter.setHTTPRequest(request, DefaultUser.class);
            contact = inputConverter.getDefaultUser().getMail();
        } catch (VarWatchException ex) {
            VWResponse response = new VWResponse();
            response.setMessage("Error");
            response.setDescription(HTTPVarWatchResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription());
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
        }

        if (userManager.getUser(contact) == null) {
            return new ResponseBuilder().withVwError().withStatusType(Response.Status.NOT_ACCEPTABLE).withVwMessage("Cant find user").build();
        }

        String newPW = RandomStringUtils.randomAlphabetic(10);
        User user = userManager.getUser(contact);
        Integer userID = user.getId();
        userManager.setNewPassword(userID, newPW);
        String subject = "Passwort wurde zurückgesetzt";

        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String title = "Dear " + firstName + " " + lastName + ",";
        String mail = "\n\nWe got a request to reset your password. Your new password is \n" + newPW;
        String vwTeam = "\n\nWith kind regards,\n"
                + "Your VarWatch Team";

        String mailtext = title + mail + vwTeam;

        NotificationSubmitter.sendMail(contact, mailtext, subject);

        VWResponse response = new VWResponse();
        response.setMessage("Successful");
        response.setDescription(RegistrationResponse.PASSWORD_RESETTED.getDescription());
        return Response.status(Response.Status.OK).entity(response).build();
    }

    public void setInputConverter(HTTPVarWatchInputConverter inputConverter) {
        this.inputConverter = inputConverter;
    }

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

}
