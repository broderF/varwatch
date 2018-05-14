/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.auth.user;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.varwatchcommons.entities.Contact;
import java.util.List;

/**
 *
 * @author broder
 */
public class UserManager {

    @Inject
    private UserDao userDao;
    @Inject
    private UserBuilder userBuilder;

    @Transactional
    public String save(Contact user) {
        User userSQL = userBuilder.withContact(user).secretPass().build();
        String result = "not registered";
        if (userDao.containsUser(userSQL)) {
            result = "db contains user";
        } else {
            userDao.save(userSQL);
            result = "registered";
        }
        return result;
    }

    @Transactional
    public boolean save(User user) {
        return userDao.save(user);
    }

    @Transactional
    public User getUser(String userMail) {
        User userByMail = userDao.getUserByName(userMail);
        return userByMail;
    }

    @Transactional
    public void setNewPassword(Integer id, String password) {
        User user = userDao.getUserByID(id);
        String secretPW = userBuilder.getSecretPW(password);
        user.setPassword(secretPW);
        userDao.update(user);
    }

    @Transactional
    public void update(User user) {
        userDao.update(user);
    }

    @Transactional
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    @Transactional
    public User getUserById(Integer userId) {
        return userDao.getUserByID(userId);
    }
}
