/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.ikmb.varwatchcommons.entities.DefaultUser;
import com.ikmb.varwatchcommons.entities.RegistrationUser;
import com.ikmb.varwatchcommons.entities.Client;
import com.ikmb.varwatchcommons.entities.Contact;
import com.ikmb.varwatchcommons.entities.VWMatchRequest;
import com.ikmb.varwatchcommons.entities.Dataset;
import com.ikmb.varwatchcommons.entities.Password;
import com.ikmb.varwatchcommons.entities.PasswordReset;
import com.ikmb.varwatchservice.registration.VarWatchRegistrationImpl;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class VarWatchInputConverter {

    private static final Logger logger = LoggerFactory.getLogger(VarWatchRegistrationImpl.class);
    private String inputString = null;
    private Class inputClass = null;

    @Inject
    private ObjectMapper mapper = new ObjectMapper();

    public void setHTTPRequest(HttpServletRequest request, Class inputClass) throws IOException {
        InputStream input = null;
        input = request.getInputStream();
        this.inputString = IOUtils.toString(input, "UTF-8");
        this.inputClass = inputClass;
        logger.info("inputstring is: " + inputString);
    }

    public Client getVWClient() throws IOException {
        Client client = null;
        if (inputClass.equals(Client.class)) {
            client = mapper.reader().forType(Client.class).readValue(inputString);
        }
        return client;
    }

    public Password getPassword() throws IOException {
        Password pw = null;
        if (inputClass.equals(Password.class)) {
            pw = mapper.reader().forType(Password.class).readValue(inputString);
        }
        return pw;
    }
    
      public PasswordReset getPasswordReset() throws IOException {
        PasswordReset pw = null;
        if (inputClass.equals(PasswordReset.class)) {
            pw = mapper.reader().forType(PasswordReset.class).readValue(inputString);
        }
        return pw;
    }

    public Contact getVWContact() throws IOException {
        Contact contact = null;
        if (inputClass.equals(Contact.class)) {
            contact = mapper.reader().forType(Contact.class).readValue(inputString);
        } else if (inputClass.equals(VWMatchRequest.class)) {
            VWMatchRequest request = mapper.reader().forType(VWMatchRequest.class).readValue(inputString);
            contact = request.getPatient().getContact();
        }
        return contact;
    }

    public RegistrationUser getRegistrationUser() throws IOException {
        RegistrationUser contact = null;
        if (inputClass.equals(RegistrationUser.class)) {
            contact = mapper.reader().forType(RegistrationUser.class).readValue(inputString);
        }
        return contact;
    }

    public DefaultUser getDefaultUser() throws IOException {
        DefaultUser contact = null;
        if (inputClass.equals(DefaultUser.class)) {
            contact = mapper.reader().forType(DefaultUser.class).readValue(inputString);
        }
        return contact;
    }

    public String getRequestString() {
        return inputString;
    }

    public VWMatchRequest getVWMatchRequest() throws IOException {
        VWMatchRequest request = mapper.reader().forType(VWMatchRequest.class).readValue(inputString);
        return request;
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

    public enum HTTPParsingResponse {

        //http requests
        HTTP_REQUEST_NOT_PARSABLE(0, "http request not parsable"),
        HTTP_CONTACT_NOT_PARSABLE(1, "http contact not parsable for string"),
        HTTP_CLIENT_NOT_PARSABLE(2, "http client not parsable for string");

        private final int code;
        private final String description;

        private HTTPParsingResponse(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            return code + ": " + description;
        }
    }
}
