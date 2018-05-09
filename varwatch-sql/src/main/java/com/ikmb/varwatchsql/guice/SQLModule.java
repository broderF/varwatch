/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.guice;

import com.google.inject.AbstractModule;
import com.ikmb.core.auth.client.ClientDao;
import com.ikmb.core.auth.token.TokenDao;
import com.ikmb.core.auth.user.UserDao;
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
import com.ikmb.core.workflow.analysis.AnalysisDao;
import com.ikmb.core.workflow.job.JobDao;
import com.ikmb.core.workflow.worker.WorkerDao;
import com.ikmb.varwatchsql.data.client.ClientDaoSQL;
import com.ikmb.varwatchsql.data.dataset.DatasetDaoSQL;
import com.ikmb.varwatchsql.data.dataset.DatasetStatusDaoSQL;
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
import com.ikmb.varwatchsql.wipe.WipeDataDaoSQL;
import com.ikmb.varwatchsql.workflow.AnalysisDaoSQL;
import com.ikmb.varwatchsql.workflow.JobDaoSQL;
import com.ikmb.varwatchsql.workflow.WorkerDaoSQL;

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
    }

}
