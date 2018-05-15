package com.ikmb.rest.guice;

//package com.ikmb.varwatchservice.guice;
//
//import com.google.inject.persist.PersistFilter;
//import com.google.inject.persist.jpa.JpaPersistModule;
//import com.google.inject.servlet.ServletModule;
//import com.ikmb.varwatchcommons.utils.PasswordValidator;
//import com.ikmb.varwatchservice.HTTPTokenValidator;
//import com.ikmb.varwatchservice.VarWatchInputConverter;
//import com.ikmb.varwatchservice.registration.ClientChecker;
//import com.ikmb.varwatchservice.registration.RegistrationManager;
//import com.ikmb.varwatchservice.registration.TokenCreator;
//import com.ikmb.varwatchservice.registration.UserChecker;
//import com.ikmb.varwatchservice.registration.VarWatchRegistrationImpl;
//import com.ikmb.varwatchsql.VarWatchPersist;
//import com.ikmb.varwatchsql.auth.client.ClientBuilder;
//import com.ikmb.varwatchsql.auth.client.ClientDao;
//import com.ikmb.varwatchsql.auth.client.ClientManager;
//import com.ikmb.varwatchsql.auth.token.TokenDao;
//import com.ikmb.varwatchsql.auth.token.TokenManager;
//import com.ikmb.varwatchsql.auth.user.UserBuilder;
//import com.ikmb.varwatchsql.auth.user.UserDao;
//import com.ikmb.varwatchsql.auth.user.UserManager;
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
///**
// *
// * @author bfredrich
// */
//public class VwServletModule extends ServletModule {
//
//    @Override
//    protected void configureServlets() {
//        install(new JpaPersistModule("varwatch_docker")); //Anbindung an Datenbank
//        bind(VarWatchPersist.class).asEagerSingleton();
//
//        bind(VarWatchRegistrationImpl.class);
//        bind(PasswordValidator.class);
//        bind(VarWatchInputConverter.class);
//        bind(RegistrationManager.class);
//        bind(HTTPTokenValidator.class);
//
//        bind(UserChecker.class);
//        bind(UserManager.class);
//        bind(UserDao.class);
//        bind(UserBuilder.class);
//
//        bind(ClientChecker.class);
//        bind(ClientManager.class);
//        bind(ClientBuilder.class);
//        bind(ClientDao.class);
//
//        bind(TokenCreator.class);
//        bind(TokenManager.class);
//        bind(TokenDao.class);
//        filter("/*").through(PersistFilter.class);
//    }
//}
