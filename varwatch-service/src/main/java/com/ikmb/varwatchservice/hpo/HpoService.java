/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.hpo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The /hpo Endpoint for VarWatch
 *
 * @author bfredrich
 */
public interface HpoService {

    /**
     * Returns the hpo terms, which are used for VarWatch
     *
     * @return
     */
    @GET
    @Path("data")
    @Produces(MediaType.TEXT_PLAIN)
    public String getHpoDataList();

}
