/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.registration;

import com.ikmb.core.varwatchcommons.utils.PasswordValidator;
import com.google.inject.Inject;
import com.ikmb.core.auth.RegistrationResponse;
import com.ikmb.core.auth.client.AuthClient;
import com.ikmb.core.auth.client.ClientManager;

/**
 * Validates the client
 *
 * @author broder
 */
public class ClientChecker {

    private String response;

    @Inject
    private ClientManager clientManager;

    @Inject
    private PasswordValidator passWordValidator;

    /**
     * Checks if the client is saved in the database and if the password is
     * valid
     *
     * @param clientName
     * @param clientPass
     * @return
     */
    public boolean isClientAndSecretValid(String clientName, String clientPass) {
        AuthClient client = clientManager.getClient(clientName);
        if (!client.getValid()) {
            response = RegistrationResponse.CLIENT_NAME_NOT_VALID.getDescription();
            return false;
        }

        if (!passWordValidator.isPasswortValid(client.getSecret(), clientPass)) {
            response = RegistrationResponse.CLIENT_PASSWORD_NOT_VALID.getDescription();
            return false;
        }
        response = RegistrationResponse.CLIENT_SUCCES_REGISTER.getDescription();
        return true;
    }

    /**
     * Checks if the client is in the database and set as a valid client in the
     * database
     *
     * @param clientName
     * @return
     */
    public boolean isClientValid(String clientName) {
        AuthClient client = clientManager.getClient(clientName);
        if (client == null || !client.getValid()) {
            response = RegistrationResponse.CLIENT_NAME_NOT_VALID.getDescription();
            return false;
        }
        response = RegistrationResponse.CLIENT_SUCCES_REGISTER.getDescription();
        return true;
    }

    public String getResponse() {
        return response;
    }

}
