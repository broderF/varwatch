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
import com.ikmb.core.data.reference_db.RefDatabase;
import com.ikmb.core.data.reference_db.ReferenceDBDataManager;
import com.ikmb.rest.util.ResponseBuilder;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

    @GET
    @Path("show")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfiguration() {
        List<VarWatchConfig> configurations = configManager.getConfigurations();
        return new ResponseBuilder().buildList(configurations);
    }

    @GET
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addConfiguration(@QueryParam("key") String key, @QueryParam("value") String value) {
        configManager.addConfiguration(key, value);
    }

    @GET
    @Path("filter/show")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilterConfiguration() {
        List<FilterConfig> filterOptions = configManager.getFilterOptions();
        return new ResponseBuilder().buildList(filterOptions);
    }

    @GET
    @Path("filter/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addFilterConfiguration(@QueryParam("key") String key, @QueryParam("value") String value, @QueryParam("type") String type, @QueryParam("enabled") boolean enabled) {
        configManager.addFilterConfiguration(key, value, type, enabled);
    }

    @GET
    @Path("beacon/show")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBeaconConfiguration() {
        List<RefDatabase> activeBeacons = databaseManager.getActiveBeacons();
        return new ResponseBuilder().buildList(activeBeacons);
    }
}
