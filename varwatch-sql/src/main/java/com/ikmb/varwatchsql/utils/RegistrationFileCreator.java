/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.ikmb.varwatchcommons.entities.RegistrationUser;
import com.ikmb.varwatchcommons.utils.PdfCreator;
import com.ikmb.varwatchsql.guice.VarWatchInjector;
import com.ikmb.varwatchsql.guice.VarWatchPersist;
import com.ikmb.varwatchsql.auth.user.UserBuilder;
import com.ikmb.varwatchsql.auth.user.UserManager;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.wipe.WipeDataManager;

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
        for (UserSQL user : udm.getAllUser()) {
            UserBuilder ub = new UserBuilder();
            RegistrationUser contact = ub.withContactSql(user).buildRegistrationUser();
            String filePath = PdfCreator.createPdfFromContact(contact);
            System.out.println(filePath);
        }
    }
}
