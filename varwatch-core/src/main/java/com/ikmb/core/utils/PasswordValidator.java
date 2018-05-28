/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.utils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author broder
 */
public class PasswordValidator {

    public boolean isPasswortValid(String passExp, String passAct) {
        boolean isPassValid = false;
        try {
            isPassValid = PasswordHash.validatePassword(passAct, passExp);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordValidator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(PasswordValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isPassValid;
    }

    public String createHash(String pass) {
        String resultPass = pass;
        try {
            resultPass = PasswordHash.createHash(pass);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordValidator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(PasswordValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultPass;
    }
}
