/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.guice;

import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import com.ikmb.core.auth.client.ClientBuilder;
import com.ikmb.core.auth.client.ClientDao;
import com.ikmb.core.auth.client.ClientManager;
import com.ikmb.core.auth.token.TokenDao;
import com.ikmb.core.auth.token.TokenManager;
import com.ikmb.core.auth.user.UserBuilder;
import com.ikmb.core.auth.user.UserDao;
import com.ikmb.core.auth.user.UserManager;
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
import com.ikmb.core.varwatchcommons.utils.PasswordValidator;
import com.ikmb.core.workflow.analysis.AnalysisDao;
import com.ikmb.core.workflow.job.JobDao;
import com.ikmb.core.workflow.worker.WorkerDao;
import com.ikmb.varwatchservice.HTTPTokenValidator;
import com.ikmb.varwatchservice.VarWatchInputConverter;
import com.ikmb.varwatchservice.registration.ClientChecker;
import com.ikmb.varwatchservice.registration.RegistrationManager;
import com.ikmb.varwatchservice.registration.TokenCreator;
import com.ikmb.varwatchservice.registration.UserChecker;
import com.ikmb.varwatchsql.data.client.ClientDaoSQL;
import com.ikmb.varwatchsql.data.dataset.DatasetDaoSQL;
import com.ikmb.varwatchsql.data.ensembl.EnsemblDaoSQL;
import com.ikmb.varwatchsql.data.hpo.HPOTermDaoSQL;
import com.ikmb.varwatchsql.data.hpo.PhenotypeDaoSQL;
import com.ikmb.varwatchsql.data.matching.MatchVariantDaoSQL;
import com.ikmb.varwatchsql.data.reference_db.ReferenceDBDaoSQL;
import com.ikmb.varwatchsql.data.token.TokenDaoSQL;
import com.ikmb.varwatchsql.data.transcript.TranscriptDaoSQL;
import com.ikmb.varwatchsql.data.user.UserDaoSQL;
import com.ikmb.varwatchsql.data.variant.VariantDaoSQL;
import com.ikmb.varwatchsql.data.variant.VariantStatusDaoSQL;
import com.ikmb.varwatchsql.data.varianteffect.VariantEffectDaoSQL;
import com.ikmb.varwatchsql.guice.VarWatchInjector;
import com.ikmb.varwatchsql.guice.VarWatchPersist;
import com.ikmb.varwatchsql.wipe.WipeDataDaoSQL;
import com.ikmb.varwatchsql.workflow.AnalysisDaoSQL;
import com.ikmb.varwatchsql.workflow.JobDaoSQL;
import com.ikmb.varwatchsql.workflow.WorkerDaoSQL;
import com.squarespace.jersey2.guice.JerseyGuiceServletContextListener;
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
        return Collections.singletonList(new ServletModule() {
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
                bind(VarWatchInputConverter.class);
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

                bind(WorkerDao.class).to(WorkerDaoSQL.class);
                bind(AnalysisDao.class).to(AnalysisDaoSQL.class);
                bind(JobDao.class).to(JobDaoSQL.class);

                bind(DatasetDao.class).to(DatasetDaoSQL.class);
                bind(EnsemblDao.class).to(EnsemblDaoSQL.class);

                bind(HPOTermDao.class).to(HPOTermDaoSQL.class);
                bind(PhenotypeDao.class).to(PhenotypeDaoSQL.class);
                bind(MatchVariantDao.class).to(MatchVariantDaoSQL.class);
                bind(ReferenceDBDao.class).to(ReferenceDBDaoSQL.class);
                bind(TranscriptDao.class).to(TranscriptDaoSQL.class);
                bind(VariantDao.class).to(VariantDaoSQL.class);
                bind(VariantStatusDao.class).to(VariantStatusDaoSQL.class);
                bind(VariantEffectDao.class).to(VariantEffectDaoSQL.class);

                bind(WipeDataDao.class).to(WipeDataDaoSQL.class);
                bind(ClientDao.class).to(ClientDaoSQL.class);
                bind(TokenDao.class).to(TokenDaoSQL.class);
                bind(UserDao.class).to(UserDaoSQL.class);
                bind(MatchVariantDao.class).to(MatchVariantDaoSQL.class);
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
