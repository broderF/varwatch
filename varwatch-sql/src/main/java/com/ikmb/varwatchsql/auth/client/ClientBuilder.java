/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.auth.client;

import com.google.inject.Inject;
import com.ikmb.varwatchcommons.entities.Client;
import com.ikmb.varwatchcommons.utils.PasswordValidator;
import org.joda.time.DateTime;

/**
 *
 * @author broder
 */
public class ClientBuilder {

    private String name;
    private String password;
    private DateTime timestamp;
    private boolean isValid;

    @Inject
    private PasswordValidator passHandler;

    public ClientBuilder withVWClient(Client client) {
        name = client.getName();
        password = client.getPassword();
        timestamp = new DateTime();
        isValid = false;
        return this;
    }

    public ClientBuilder secretPass() {
        if (password != null) {
            password = passHandler.createHash(password);
        }
        return this;
    }

    public AuthClientSQL buildSQL() {
        AuthClientSQL clientsql = new AuthClientSQL();
        clientsql.setName(name);
        clientsql.setTimestamp(timestamp);
        clientsql.setValid(isValid);
        clientsql.setSecret(password);
        return clientsql;
    }
}
