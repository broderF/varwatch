/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.auth.user;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.ikmb.varwatchcommons.entities.Contact;
import com.ikmb.varwatchsql.guice.VarWatchInjector;
import com.ikmb.varwatchsql.guice.VarWatchPersist;
import com.ikmb.varwatchsql.matching.MatchVariantDataManager;
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
//    @Inject
//    private EntityManager em;

    @Transactional
    public String save(Contact user) {
        UserSQL userSQL = userBuilder.withContact(user).secretPass().buildSQL();
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
    public boolean save(UserSQL user) {
        return userDao.save(user);
    }

    @Transactional
    public UserSQL getUser(String userMail) {
        UserSQL userByMail = userDao.getUserByName(userMail);
        return userByMail;
    }

    @Transactional
    public void setNewPassword(Integer id, String password) {
        UserSQL user = userDao.getUserByID(id);
        String secretPW = userBuilder.getSecretPW(password);
        user.setPassword(secretPW);
        userDao.update(user);
    }

    @Transactional
    public void update(UserSQL user) {
        userDao.update(user);
    }

    @Transactional
    public List<UserSQL> getAllUser() {
        return userDao.getAllUser();
    }

    @Transactional
    public UserSQL getUserById(Integer userId) {
        return userDao.getUserByID(userId);
    }

    public static void main(String[] args) {
        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
        UserManager vdm = inj.getInstance(UserManager.class);
        vdm.setNewPassword(37, "123456");
        System.out.println("finish");
    }
}
