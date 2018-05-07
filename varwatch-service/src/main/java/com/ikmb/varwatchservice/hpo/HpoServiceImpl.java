/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.hpo;

import com.google.inject.Inject;
import com.ikmb.update.hpo.HPOUpdateManager;
import javax.ws.rs.Path;

/**
 *
 * @author bfredrich
 */
@Path("hpo")
public class HpoServiceImpl implements HpoService {

    @Inject
    private HPOUpdateManager hpoManager;

    @Override
    public String getHpoDataList() {
        return hpoManager.getHpoList();
    }

}
