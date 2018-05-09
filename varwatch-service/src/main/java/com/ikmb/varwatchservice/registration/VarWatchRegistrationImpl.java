/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.registration;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.ikmb.core.auth.RegistrationResponse;
import com.ikmb.core.auth.user.User;
import com.ikmb.core.auth.user.UserBuilder;
import com.ikmb.core.auth.user.UserManager;
import com.ikmb.core.varwatchcommons.entities.VWResponse;
import com.ikmb.core.varwatchcommons.entities.DefaultUser;
import com.ikmb.core.varwatchcommons.entities.RegistrationUser;
import com.ikmb.core.varwatchcommons.entities.Client;
import com.ikmb.core.varwatchcommons.entities.PasswordReset;
import com.ikmb.core.varwatchcommons.notification.NotificationSubmitter;
import com.ikmb.core.varwatchcommons.utils.PdfCreator;
import com.ikmb.varwatchservice.HTTPTokenValidator;
import com.ikmb.varwatchservice.OAuthRequestWrapper;
import com.ikmb.varwatchservice.ResponseBuilder;
import com.ikmb.varwatchservice.VarWatchInputConverter;
import com.ikmb.varwatchservice.VarWatchInputConverter.HTTPParsingResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
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
public class VarWatchRegistrationImpl implements VarWatchRegistration {

    private Integer expiresIn = 3600;

    private static final Logger logger = LoggerFactory.getLogger(VarWatchRegistrationImpl.class);

//    @Context
//    ServletContext context;
    @Inject
    private VarWatchInputConverter inputConverter;
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

    @Override
    public Response registerClient(HttpServletRequest request) {
        try {
            inputConverter.setHTTPRequest(request, Client.class);
        } catch (IOException ex) {
            return Response.status(Response.Status.OK).entity(HTTPParsingResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription()).build();
        }

        Client client;
        try {
            client = inputConverter.getVWClient();
        } catch (IOException ex) {
            return Response.status(Response.Status.OK).entity(HTTPParsingResponse.HTTP_CLIENT_NOT_PARSABLE.getDescription()).build();
        }
        String response = registrationManager.saveClient(client);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @Override
    public Response registerUser(HttpServletRequest request) {
        RegistrationUser contact;
        try {
            contact = inputConverter.getRegistrationUser(request);
        } catch (IOException ex) {
            VWResponse response = new VWResponse();
            response.setMessage("Error");
            response.setMessage(HTTPParsingResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription());
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
        String text = title+"\n"
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

    @Override
    public Response test(HttpServletRequest request) {
        return Response.status(Response.Status.OK).entity("hallo varwatch registration test22").build();
    }

    @Override
    public Response token(HttpServletRequest request, MultivaluedMap<String, String> form) throws OAuthSystemException {
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

    @Override
    public Response logout(String header) throws OAuthSystemException {
        tokenValidator.setHeader(header);
        Boolean deleted = tokenValidator.deleteToken();
        if (!deleted) {
            return new ResponseBuilder().withVwError().withVwMessage(RegistrationResponse.TOKEN_DELETION_ERROR.getDescription()).withStatusType(Response.Status.NOT_ACCEPTABLE).build();
        } else {
            return new ResponseBuilder().withVwSuccessful().withVwMessage(RegistrationResponse.TOKEN_DELETION_SUCCESFULL.getDescription()).withStatusType(Response.Status.OK).build();
        }
    }

    @Override
    public Response isTokenValid(String header) throws OAuthSystemException {
        Response response = tokenValidator.isTokenValid(header);
        return response;
    }

    @Override
    public Response getUserInformation(String header) throws OAuthSystemException {
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

    @Override
    public Response setNewPassword(String header, HttpServletRequest request) throws OAuthSystemException {
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
        } catch (IOException ex) {
            VWResponse response = new VWResponse();
            response.setMessage("Error");
            response.setDescription(HTTPParsingResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription());
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

    @Override
    public Response resetPassword(HttpServletRequest request) throws OAuthSystemException {
        String contact = null;
        try {
            inputConverter.setHTTPRequest(request, DefaultUser.class);
            contact = inputConverter.getDefaultUser().getMail();
        } catch (IOException ex) {
            VWResponse response = new VWResponse();
            response.setMessage("Error");
            response.setDescription(HTTPParsingResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription());
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
}
