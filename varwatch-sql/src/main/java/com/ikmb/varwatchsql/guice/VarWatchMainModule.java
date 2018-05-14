/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.guice;

import com.google.inject.AbstractModule;
import com.ikmb.core.data.auth.client.ClientDao;
import com.ikmb.core.data.auth.token.TokenDao;
import com.ikmb.core.data.auth.user.UserDao;
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
import com.ikmb.core.data.workflow.analysis.AnalysisDao;
import com.ikmb.core.data.workflow.job.JobDao;
import com.ikmb.core.data.workflow.worker.WorkerDao;
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
import com.ikmb.varwatchsql.wipe.WipeDataDaoSQL;
import com.ikmb.varwatchsql.workflow.AnalysisDaoSQL;
import com.ikmb.varwatchsql.workflow.JobDaoSQL;
import com.ikmb.varwatchsql.workflow.WorkerDaoSQL;

/**
 *
 * @author broder
 */
public class VarWatchMainModule extends AbstractModule {

    public VarWatchMainModule() {
    }

    @Override
    protected void configure() {
        
    }
}
