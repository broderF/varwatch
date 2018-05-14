/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.auth.client;

/**
 *
 * @author broder
 */
public interface ClientDao {

    public AuthClient getClientByName(String clientName);

    public void save(AuthClient clientsql);

    public boolean containsClient(AuthClient client);

    public AuthClient getClientByID(Integer id);
}
