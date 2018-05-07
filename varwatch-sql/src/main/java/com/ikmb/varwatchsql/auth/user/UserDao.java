/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.auth.user;

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
@Singleton
public class UserDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;
    
    public boolean save(UserSQL user) {
        if (containsUser(user)) {
            return false;
        }
        String persistenceUnitName = emProvider.get().getEntityManagerFactory().getProperties().get("hibernate.ejb.persistenceUnitName").toString();
        if (persistenceUnitName.equals("varwatch_dev")) {
            user.setActive(Boolean.TRUE);
        } else {
            user.setActive(Boolean.FALSE);
        }
        emProvider.get().persist(user);
        return true;
    }

    public boolean containsUser(UserSQL user) {
        TypedQuery<UserSQL> query = emProvider.get().createQuery("SELECT s FROM UserSQL s WHERE s.email = :email", UserSQL.class);
        List<UserSQL> submitter = query.setParameter("email", user.getMail()).getResultList();
        if (submitter.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    public UserSQL getUserByName(String userMail) {
        TypedQuery<UserSQL> query = emProvider.get().createQuery("SELECT u FROM UserSQL u WHERE u.email = :mail", UserSQL.class
        );
        UserSQL user = null;
        try {
            user = query.setParameter("mail", userMail).getSingleResult();
            emProvider.get().refresh(user);
        } catch (NoResultException nre) {
            System.out.println("no result found");
        }
        return user;
    }

    public UserSQL getUserByID(Integer id) {
        UserSQL user = emProvider.get().find(UserSQL.class, id);
        emProvider.get().refresh(user);
        return user;
    }

    void update(UserSQL user) {
        emProvider.get().merge(user);
    }

    public List<UserSQL> getAllUser() {
        TypedQuery<UserSQL> query = emProvider.get().createQuery("SELECT u FROM UserSQL u", UserSQL.class
        );
        return query.getResultList();
    }

}
