/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchcommons.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author broder
 */
public class RegistrationUser extends DefaultUser {

    private Password password;

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        return json;
    }
}
