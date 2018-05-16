/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.guice;

import com.google.inject.AbstractModule;
import com.ikmb.core.data.auth.client.ClientDao;
import com.ikmb.core.data.auth.token.TokenDao;
import com.ikmb.core.data.auth.user.UserDao;
import com.ikmb.core.data.config.ConfigurationDao;
import com.ikmb.core.data.dataset.DatasetDao;
import com.ikmb.core.data.dataset.DatasetStatusDao;
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
import com.ikmb.core.data.workflow.analysis.AnalysisDao;
import com.ikmb.core.data.workflow.job.JobDao;
import com.ikmb.core.data.workflow.worker.WorkerDao;
import com.ikmb.sql.data.client.ClientDaoSQL;
import com.ikmb.sql.data.config.ConfigurationDaoSQL;
import com.ikmb.sql.data.dataset.DatasetDaoSQL;
import com.ikmb.sql.data.dataset.DatasetStatusDaoSQL;
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
import com.ikmb.sql.wipe.WipeDataDaoSQL;
import com.ikmb.sql.workflow.AnalysisDaoSQL;
import com.ikmb.sql.workflow.JobDaoSQL;
import com.ikmb.sql.workflow.WorkerDaoSQL;

public class SQLModule extends AbstractModule {

    public SQLModule() {
    }

    @Override
    protected void configure() {
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
        bind(DatasetStatusDao.class).to(DatasetStatusDaoSQL.class);
        bind(WipeDataDao.class).to(WipeDataDaoSQL.class);
        bind(ClientDao.class).to(ClientDaoSQL.class);
        bind(TokenDao.class).to(TokenDaoSQL.class);
        bind(UserDao.class).to(UserDaoSQL.class);
        bind(MatchVariantDao.class).to(MatchVariantDaoSQL.class);

        bind(ConfigurationDao.class).to(ConfigurationDaoSQL.class);
    }

}
