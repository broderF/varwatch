/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.auth.user;

import java.util.List;

/**
 *
 * @author broder
 */
public interface UserDao {

    public boolean save(User user);

    public boolean containsUser(User user);

    public User getUserByName(String userMail);

    public User getUserByID(Integer id);

    void update(User user);

    public List<User> getAllUser();

}
