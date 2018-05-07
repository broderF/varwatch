/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.auth.client;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class ClientDao {

    @Inject
    private Provider<EntityManager> emProvider;

    @Transactional
    public AuthClientSQL getClientByName(String clientName) {
        TypedQuery<AuthClientSQL> query = emProvider.get().createQuery("SELECT s FROM AuthClientSQL s WHERE s.name = :name", AuthClientSQL.class
        );
        AuthClientSQL client = null;
        try {
            client = query.setParameter("name", clientName).getSingleResult();
            emProvider.get().refresh(client);
        } catch (NoResultException nre) {
            System.out.println("no client result found");
        }
        return client;
    }

    public void save(AuthClientSQL clientsql) {
        emProvider.get().persist(clientsql);
    }

    public boolean containsClient(AuthClientSQL client) {
        TypedQuery<AuthClientSQL> query = emProvider.get().createQuery("SELECT s FROM AuthClientSQL s WHERE s.name = :name", AuthClientSQL.class);
        List<AuthClientSQL> clients = query.setParameter("name", client.getName()).getResultList();
        if (clients.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public AuthClientSQL getClientByID(Integer id) {
        return emProvider.get().find(AuthClientSQL.class, id);
    }
}
