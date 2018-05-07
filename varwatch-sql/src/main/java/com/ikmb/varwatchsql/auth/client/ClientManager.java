/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.auth.client;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.varwatchcommons.entities.Client;
import com.ikmb.varwatchcommons.utils.PasswordValidator;
import com.ikmb.varwatchsql.auth.RegistrationResponse;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

/**
 *
 * @author broder
 */
public class ClientManager {

    @Inject
    private ClientBuilder clientBuilder;

    @Inject
    private ClientDao clientDao;

    @Inject
    private PasswordValidator passValidator;

    @Transactional
    public AuthClientSQL getClient(String clientName) {
        AuthClientSQL clientByName = clientDao.getClientByName(clientName);
        return clientByName;
    }

    @Transactional
    public String save(Client client) {
        AuthClientSQL clientsql = clientBuilder.withVWClient(client).secretPass().buildSQL();
        String response = RegistrationResponse.CLIENT_NOT_SAVED.getDescription();
        if (clientDao.containsClient(clientsql)) {
            response = RegistrationResponse.CLIENT_ALREADY_IN_DB.getDescription();
        } else {
            clientDao.save(clientsql);
            response = RegistrationResponse.CLIENT_SAVED.getDescription();
        }
        return response;
    }
}
