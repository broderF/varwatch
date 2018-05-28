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
import com.ikmb.core.data.auth.client.ClientDao;
import com.ikmb.core.data.auth.client.ClientManager;
import com.ikmb.core.data.auth.token.TokenDao;
import com.ikmb.core.data.auth.token.TokenManager;
import com.ikmb.core.data.auth.user.UserBuilder;
import com.ikmb.core.data.auth.user.UserDao;
import com.ikmb.core.data.auth.user.UserManager;
import com.ikmb.core.data.dataset.DatasetDao;
import com.ikmb.core.data.ensembl.EnsemblDao;
import com.ikmb.core.data.hpo.HPOTermDao;
import com.ikmb.core.data.hpo.PhenotypeDao;
import com.ikmb.core.data.matching.MatchVariantDao;
import com.ikmb.core.data.reference_db.ReferenceDBDao;
import com.ikmb.core.data.transcript.TranscriptDao;
import com.ikmb.core.data.variant.VariantDao;
import com.ikmb.core.data.variant.VariantStatusDao;
import com.ikmb.core.data.varianteffect.VariantEffectDao;
import com.ikmb.core.data.wipe.WipeDataDao;
import com.ikmb.core.utils.PasswordValidator;
import com.ikmb.core.data.workflow.analysis.AnalysisDao;
import com.ikmb.core.data.workflow.job.JobDao;
import com.ikmb.core.data.workflow.worker.WorkerDao;
import com.ikmb.rest.util.HTTPTokenValidator;
import com.ikmb.rest.util.HTTPVarWatchInputConverter;
import com.ikmb.rest.registration.ClientChecker;
import com.ikmb.rest.registration.RegistrationManager;
import com.ikmb.rest.registration.TokenCreator;
import com.ikmb.rest.registration.UserChecker;
import com.ikmb.sql.data.client.ClientDaoSQL;
import com.ikmb.sql.data.dataset.DatasetDaoSQL;
import com.ikmb.sql.data.ensembl.EnsemblDaoSQL;
import com.ikmb.sql.data.hpo.HPOTermDaoSQL;
import com.ikmb.sql.data.hpo.PhenotypeDaoSQL;
import com.ikmb.sql.data.matching.MatchVariantDaoSQL;
import com.ikmb.sql.data.reference_db.ReferenceDBDaoSQL;
import com.ikmb.sql.data.token.TokenDaoSQL;
import com.ikmb.sql.data.transcript.TranscriptDaoSQL;
import com.ikmb.sql.data.user.UserDaoSQL;
import com.ikmb.sql.data.variant.VariantDaoSQL;
import com.ikmb.sql.data.variant.VariantStatusDaoSQL;
import com.ikmb.sql.data.varianteffect.VariantEffectDaoSQL;
import com.ikmb.sql.guice.SQLModule;
import com.ikmb.sql.guice.VarWatchMainModule;
import com.ikmb.sql.guice.VarWatchPersist;
import com.ikmb.sql.wipe.WipeDataDaoSQL;
import com.ikmb.sql.workflow.AnalysisDaoSQL;
import com.ikmb.sql.workflow.JobDaoSQL;
import com.ikmb.sql.workflow.WorkerDaoSQL;
import com.squarespace.jersey2.guice.JerseyGuiceServletContextListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
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
                Properties properties = new Properties();
                String persistanceModule = "varwatch_docker";
                database = persistanceModule;

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
