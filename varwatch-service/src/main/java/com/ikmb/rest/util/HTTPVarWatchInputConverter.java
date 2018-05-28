/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.ikmb.core.data.auth.client.AuthClient;
import com.ikmb.core.data.auth.token.TokenManager;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.varwatchcommons.entities.DefaultUser;
import com.ikmb.core.varwatchcommons.entities.RegistrationUser;
import com.ikmb.core.varwatchcommons.entities.Client;
import com.ikmb.core.varwatchcommons.entities.Contact;
import com.ikmb.core.varwatchcommons.entities.VWMatchRequest;
import com.ikmb.core.varwatchcommons.entities.Dataset;
import com.ikmb.core.varwatchcommons.entities.Password;
import com.ikmb.core.varwatchcommons.entities.PasswordReset;
import com.ikmb.core.utils.VarWatchException;
import com.ikmb.rest.HTTPVarWatchResponse;
import com.ikmb.rest.registration.RegistrationService;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class HTTPVarWatchInputConverter {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    private String inputString = null;
    private Class inputClass = null;

    private ObjectMapper mapper = new ObjectMapper();

    @Inject
    TokenManager tokenManager;

    public String getToken(String header) {
        String accessToken = null;
        if (header != null && header.split(" ") != null) {
            accessToken = header.split(" ")[1];
        }
        return accessToken;
    }

    public User getUserFromHeader(String header) {
        String token = getToken(header);
        return tokenManager.getUserByToken(token);
    }

    public AuthClient getClientFromHeader(String header) {
        String token = getToken(header);
        return tokenManager.getClientByToken(token);
    }

    public void setHTTPRequest(HttpServletRequest request, Class inputClass) throws VarWatchException {
        try {
            InputStream input = null;
            input = request.getInputStream();
            this.inputString = IOUtils.toString(input, "UTF-8");
            this.inputClass = inputClass;
            logger.info("inputstring is: " + inputString);
        } catch (IOException ex) {
            throw new VarWatchException(HTTPVarWatchResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription());
        }
    }

    private <T extends Object> T getObject(String inputString, Class<T> type) throws VarWatchException {
        try {
            T object = mapper.reader().forType(type).readValue(inputString);
            return object;
        } catch (IOException ex) {
            throw new VarWatchException(HTTPVarWatchResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription());
        }
    }

    public Client getVWClient() throws VarWatchException {
        return getObject(inputString, Client.class);
    }

    public Password getPassword() throws VarWatchException {
        return getObject(inputString, Password.class);
    }

    public PasswordReset getPasswordReset() throws VarWatchException {
        return getObject(inputString, PasswordReset.class);
    }

    public Contact getVWContact() throws VarWatchException {
        if (inputClass.equals(Contact.class)) {
            return getObject(inputString, Contact.class);
        } else {
            return getObject(inputString, VWMatchRequest.class).getPatient().getContact();
        }
    }

    public RegistrationUser getRegistrationUser() throws VarWatchException {
        return getObject(inputString, RegistrationUser.class);
    }

    public DefaultUser getDefaultUser() throws VarWatchException {
        return getObject(inputString, DefaultUser.class);
    }

    public String getRequestString() {
        return inputString;
    }

    public VWMatchRequest getVWMatchRequest() throws VarWatchException {
        try {
            VWMatchRequest request = mapper.reader().forType(VWMatchRequest.class).readValue(inputString);
            return request;
        } catch (IOException ex) {
            throw new VarWatchException(HTTPVarWatchResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription());
        }
    }

    public Dataset getDataset() throws IOException {
        Dataset request = mapper.reader().forType(Dataset.class).readValue(inputString);
        return request;
    }

    public RegistrationUser getRegistrationUser(HttpServletRequest request) throws IOException {
        InputStream input = null;
        input = request.getInputStream();
        String tmpinputString = IOUtils.toString(input, "UTF-8");

        RegistrationUser contact = mapper.reader().forType(RegistrationUser.class).readValue(tmpinputString);
        return contact;
    }

    public VWMatchRequest getVWMatchRequest(HttpServletRequest request, Class inputClass) throws VarWatchException {
        setHTTPRequest(request, inputClass);
        return getVWMatchRequest();
    }

}
