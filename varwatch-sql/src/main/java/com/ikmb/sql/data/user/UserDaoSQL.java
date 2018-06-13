/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.data.user;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.auth.user.UserDao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
@Singleton
public class UserDaoSQL implements UserDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    public boolean save(User user) {
        if (containsUser(user)) {
            return false;
        }
        String persistenceUnitName = emProvider.get().getEntityManagerFactory().getProperties().get("hibernate.ejb.persistenceUnitName").toString();
        if (persistenceUnitName.equals("varwatch")) {
            user.setActive(Boolean.FALSE);
        } else {
            user.setActive(Boolean.TRUE);
        }
        emProvider.get().persist(user);
        return true;
    }

    public boolean containsUser(User user) {
        TypedQuery<User> query = emProvider.get().createQuery("SELECT s FROM User s WHERE s.email = :email", User.class);
        List<User> submitter = query.setParameter("email", user.getMail()).getResultList();
        if (submitter.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public User getUserByName(String userMail) {
        TypedQuery<User> query = emProvider.get().createQuery("SELECT u FROM User u WHERE u.email = :mail", User.class
        );
        User user = null;
        try {
            user = query.setParameter("mail", userMail).getSingleResult();
            emProvider.get().refresh(user);
        } catch (NoResultException nre) {
            System.out.println("no result found");
        }
        return user;
    }

    public User getUserByID(Integer id) {
        User user = emProvider.get().find(User.class, id);
        emProvider.get().refresh(user);
        return user;
    }

    public void update(User user) {
        emProvider.get().merge(user);
    }

    public List<User> getAllUser() {
        TypedQuery<User> query = emProvider.get().createQuery("SELECT u FROM User u", User.class
        );
        List<User> resultList = query.getResultList();
        List<User> tmp = new ArrayList<>();
        for (User user : resultList) {
            tmp.add(user);
        }
        return tmp;
    }

    @Override
    public void deleteUser(User user) {
        emProvider.get().remove(user);
    }

}
