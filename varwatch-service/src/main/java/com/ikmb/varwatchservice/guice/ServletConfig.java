/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.guice;

import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import com.ikmb.varwatchcommons.utils.PasswordValidator;
import com.ikmb.varwatchservice.HTTPTokenValidator;
import com.ikmb.varwatchservice.VarWatchInputConverter;
import com.ikmb.varwatchservice.registration.ClientChecker;
import com.ikmb.varwatchservice.registration.RegistrationManager;
import com.ikmb.varwatchservice.registration.TokenCreator;
import com.ikmb.varwatchservice.registration.UserChecker;
import com.ikmb.varwatchsql.guice.VarWatchPersist;
import com.ikmb.varwatchsql.auth.client.ClientBuilder;
import com.ikmb.varwatchsql.auth.client.ClientDao;
import com.ikmb.varwatchsql.auth.client.ClientManager;
import com.ikmb.varwatchsql.auth.token.TokenDao;
import com.ikmb.varwatchsql.auth.token.TokenManager;
import com.ikmb.varwatchsql.auth.user.UserBuilder;
import com.ikmb.varwatchsql.auth.user.UserDao;
import com.ikmb.varwatchsql.auth.user.UserManager;
import com.squarespace.jersey2.guice.JerseyGuiceServletContextListener;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import org.jboss.logging.Logger;
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
        return Collections.singletonList(new ServletModule() {
            @Override
            protected void configureServlets() {
                Properties properties = new Properties();
                String persistanceModule = "varwatch_docker";
//                try {
//                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//                    InputStream input = classLoader.getResourceAsStream("server.properties");
//                    properties.load(input);
//                    persistanceModule = properties.getProperty("database");
//                } catch (FileNotFoundException ex) {
//                    Logger.getLogger(ServletConfig.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(ServletConfig.class.getName()).log(Level.SEVERE, null, ex);
//                }
                database = persistanceModule;

                logger.info("----------------------------");
                logger.info("Module: " + persistanceModule);
                logger.info("----------------------------");
                install(new JpaPersistModule(persistanceModule)); //Anbindung an Datenbank
                bind(VarWatchPersist.class).asEagerSingleton();

                bind(PasswordValidator.class);
                bind(VarWatchInputConverter.class);
                bind(RegistrationManager.class);
                bind(HTTPTokenValidator.class);

                bind(UserChecker.class);
                bind(UserManager.class);
                bind(UserDao.class);
                bind(UserBuilder.class);

                bind(ClientChecker.class);
                bind(ClientManager.class);
                bind(ClientBuilder.class);
                bind(ClientDao.class);

                bind(TokenCreator.class);
                bind(TokenManager.class);
                bind(TokenDao.class);
            }

        });

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();
        System.out.println(context);
        super.contextInitialized(sce); //To change body of generated methods, choose Tools | Templates.
    }

}
