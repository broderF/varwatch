/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.hpo;

import com.google.inject.Inject;
import com.ikmb.core.data.hpo.HPOUpdateManager;
import com.ikmb.core.data.workflow.analysis.AnalysisBuilder;
import com.ikmb.core.data.workflow.job.JobManager;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bfredrich
 */
@Path("hpo")
public class HpoServiceImpl {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(HpoServiceImpl.class);

    @Inject
    private HPOUpdateManager hpoManager;
    @Inject
    private JobManager jobManager;

    @GET
    @Path("data")
    @Produces(MediaType.TEXT_PLAIN)
    public String getHpoDataList() {
        return hpoManager.getHpoList();
    }

    @POST
    @Path("new")
    public void parseNewHpo() {
        hpoManager.updateHpoBrowserFile();
        jobManager.createJob(AnalysisBuilder.ModuleName.HPO_UPDATE, null, "NEW");
    }

}
