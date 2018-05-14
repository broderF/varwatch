/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.client;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.auth.client.AuthClient;
import com.ikmb.core.data.auth.client.ClientDao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class ClientDaoSQL implements ClientDao {

    @Inject
    private Provider<EntityManager> emProvider;

    @Transactional
    public AuthClient getClientByName(String clientName) {
        TypedQuery<AuthClient> query = emProvider.get().createQuery("SELECT s FROM AuthClient s WHERE s.name = :name", AuthClient.class
        );
        AuthClient client = null;
        try {
            client = query.setParameter("name", clientName).getSingleResult();
            emProvider.get().refresh(client);
        } catch (NoResultException nre) {
            System.out.println("no client result found");
        }
        return client;
    }

    public void save(AuthClient client) {
        emProvider.get().persist(client);
    }

    public boolean containsClient(AuthClient client) {
        TypedQuery<AuthClient> query = emProvider.get().createQuery("SELECT s FROM AuthClient s WHERE s.name = :name", AuthClient.class);
        List<AuthClient> clients = query.setParameter("name", client.getName()).getResultList();
        if (clients.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public AuthClient getClientByID(Integer id) {
        AuthClient find = emProvider.get().find(AuthClient.class, id);
        return find;
    }
}
