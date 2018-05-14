/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.registration;

import com.ikmb.core.varwatchcommons.utils.PasswordValidator;
import com.google.inject.Inject;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.auth.user.UserManager;

/**
 * Validates a user
 *
 * @author broder
 */
public class UserChecker {

    private String response;

    @Inject
    private UserManager userManager;

    @Inject
    private PasswordValidator passWordValidator;

    /**
     * Determines if a user is saved in the database and if his password is
     * valid
     *
     * @param userMail
     * @param userPass
     * @return
     */
    public boolean isUserValid(String userMail, String userPass) {
        User user = userManager.getUser(userMail);

        if (user == null || !passWordValidator.isPasswortValid(user.getPassword(), userPass)) {
            response = "User or Password not valid";
            return false;
        } 
        response = "User succesfull registered";
        return true;
    }

    public String getResponse() {
        return response;
    }

}
