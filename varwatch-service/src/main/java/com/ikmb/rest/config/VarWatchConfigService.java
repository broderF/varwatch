/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.config;

import com.google.inject.Inject;
import com.ikmb.core.data.config.ConfigurationManager;
import com.ikmb.core.data.config.FilterConfig;
import com.ikmb.core.data.config.VarWatchConfig;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.reference_db.RefDatabase;
import com.ikmb.core.data.reference_db.ReferenceDBDataManager;
import com.ikmb.core.data.workflow.analysis.AnalysisBuilder;
import com.ikmb.core.data.workflow.job.AnalysisJob;
import com.ikmb.core.data.workflow.job.JobManager;
import com.ikmb.rest.util.ResponseBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author broder
 */
@Path("config")
public class VarWatchConfigService {

    @Inject
    private ConfigurationManager configManager;
    @Inject
    private ReferenceDBDataManager databaseManager;
    @Inject
    private DatasetManager dsManager;
    @Inject
    private JobManager jobManager;

    @GET
    @Path("show")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfiguration() {
        List<VarWatchConfig> configurations = configManager.getConfigurations();
        return new ResponseBuilder().buildList(configurations);
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addConfiguration(@QueryParam("key") String key, @QueryParam("value") String value) {
        configManager.addConfiguration(key, value);
        List<VarWatchConfig> configurations = configManager.getConfigurations();
        return new ResponseBuilder().buildList(configurations);
    }

    @GET
    @Path("filter/show")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilterConfiguration() {
        List<FilterConfig> filterOptions = configManager.getFilterOptions();
        return new ResponseBuilder().buildList(filterOptions);
    }

    @POST
    @Path("filter/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFilterConfiguration(@QueryParam("key") String key, @QueryParam("value") String value, @QueryParam("type") String type, @QueryParam("enabled") boolean enabled) {
        configManager.addFilterConfiguration(key, value, type, enabled);
        List<FilterConfig> filterOptions = configManager.getFilterOptions();
        return new ResponseBuilder().buildList(filterOptions);
    }

    @GET
    @Path("beacon/show")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBeaconConfiguration() {
        List<RefDatabase> activeBeacons = databaseManager.getActiveBeacons();
        return new ResponseBuilder().buildListWithExpose(activeBeacons);
    }

    @POST
    @Path("beacon/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBeaconConfiguration(@QueryParam("name") String name, @QueryParam("path") String path, @QueryParam("assembly") String assembly, @QueryParam("image_url") String imageUrl, @QueryParam("enabled") boolean enabled) {
        byte[] image = recoverImageFromUrl(imageUrl);
        RefDatabase referenceDB = databaseManager.saveBeacon(name, path, assembly, image, enabled);

        for (Long id : dsManager.getAllDatasetIds()) {
            jobManager.createJob(id, AnalysisBuilder.ModuleName.SCREENING_BEACON, referenceDB.getId().toString(), AnalysisJob.JobAction.UPDATE.toString());
            jobManager.createJob(id, AnalysisBuilder.ModuleName.SCREEN_BEACON_RESULT_COLLECT, null, AnalysisJob.JobAction.UPDATE.toString());
        }
        List<RefDatabase> activeBeacons = databaseManager.getActiveBeacons();
        return new ResponseBuilder().buildListWithExpose(activeBeacons);
    }

    private byte[] recoverImageFromUrl(String urlText) {
        URL url;
        try {
            url = new URL(urlText);
        } catch (MalformedURLException ex) {
            Logger.getLogger(VarWatchConfigService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte[] buffer = new byte[1024];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } catch (IOException ex) {
            Logger.getLogger(VarWatchConfigService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return output.toByteArray();
    }
}
