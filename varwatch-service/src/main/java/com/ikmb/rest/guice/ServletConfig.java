/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.guice;

import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import com.ikmb.core.data.auth.client.ClientBuilder;
import com.ikmb.core.data.auth.client.ClientManager;
import com.ikmb.core.data.auth.token.TokenManager;
import com.ikmb.core.data.auth.user.UserBuilder;
import com.ikmb.core.data.auth.user.UserManager;
import com.ikmb.core.utils.PasswordValidator;
import com.ikmb.rest.util.HTTPTokenValidator;
import com.ikmb.rest.util.HTTPVarWatchInputConverter;
import com.ikmb.rest.registration.ClientChecker;
import com.ikmb.rest.registration.RegistrationManager;
import com.ikmb.rest.registration.TokenCreator;
import com.ikmb.rest.registration.UserChecker;
import com.ikmb.sql.guice.SQLModule;
import com.ikmb.sql.guice.VarWatchPersist;
import com.squarespace.jersey2.guice.JerseyGuiceServletContextListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import org.slf4j.LoggerFactory;

/**
 * The ServletConfig class parses the persistence_unit (dbname, dbuser,dbpw for
 * the given database) and creates the injections
 *
 * @author bfredrich
 */
public class ServletConfig extends JerseyGuiceServletContextListener {

    private ServletContext context;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ServletConfig.class);
    public static String database = "varwatch";

    @Override
    protected List<? extends Module> modules() {
        return Arrays.asList(new ServletModule() {
            @Override
            protected void configureServlets() {
                String persistanceModule = "varwatch";
                try {
                    Properties properties = new Properties();
                    properties.load(getClass().getClassLoader().getResourceAsStream("server.properties"));
                    persistanceModule = properties.getProperty("db");

                } catch (IOException ex) {
                    Logger.getLogger(ServletConfig.class.getName()).log(Level.SEVERE, null, ex);
                }

                logger.info("----------------------------");
                logger.info("Module: " + persistanceModule);
                logger.info("----------------------------");
                install(new JpaPersistModule(persistanceModule)); //Anbindung an Datenbank
                bind(VarWatchPersist.class).asEagerSingleton();

                bind(PasswordValidator.class);
                bind(HTTPVarWatchInputConverter.class);
                bind(RegistrationManager.class);
                bind(HTTPTokenValidator.class);

                bind(UserChecker.class);
                bind(UserManager.class);
                bind(UserBuilder.class);

                bind(ClientChecker.class);
                bind(ClientManager.class);
                bind(ClientBuilder.class);

                bind(TokenCreator.class);
                bind(TokenManager.class);

            }

        }, new SQLModule());

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();
        System.out.println(context);
        super.contextInitialized(sce); //To change body of generated methods, choose Tools | Templates.
    }

}
