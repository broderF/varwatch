/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.ikmb.core.auth.user.User;
import com.ikmb.core.auth.user.UserBuilder;
import com.ikmb.core.auth.user.UserManager;
import com.ikmb.core.data.wipe.WipeDataManager;
import com.ikmb.core.varwatchcommons.entities.RegistrationUser;
import com.ikmb.core.varwatchcommons.utils.PdfCreator;
import com.ikmb.varwatchsql.guice.VarWatchInjector;
import com.ikmb.varwatchsql.guice.VarWatchPersist;

/**
 *
 * @author bfredrich
 */
public class RegistrationFileCreator {

    public static void main(String[] args) {
        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
        WipeDataManager vdm = inj.getInstance(WipeDataManager.class);
        UserManager udm = inj.getInstance(UserManager.class);
        for (User user : udm.getAllUser()) {
            UserBuilder ub = new UserBuilder();
            RegistrationUser contact = ub.withUser(user).buildRegistrationUser();
            String filePath = PdfCreator.createPdfFromContact(contact);
            System.out.println(filePath);
        }
    }
}
