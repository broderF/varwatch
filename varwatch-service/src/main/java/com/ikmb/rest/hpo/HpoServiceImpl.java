/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.hpo;

import com.google.inject.Inject;
import com.ikmb.core.data.hpo.HPOUpdateManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author bfredrich
 */
@Path("hpo")
public class HpoServiceImpl {

    @Inject
    private HPOUpdateManager hpoManager;

      @GET
    @Path("data")
    @Produces(MediaType.TEXT_PLAIN)
    public String getHpoDataList() {
        return hpoManager.getHpoList();
    }

}
