/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.guice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;

@Singleton
public class VarWatchPersist {

    @Inject
    VarWatchPersist(final PersistService service) {
        service.start();
        // At this point JPA is started and ready.
        // other application initializations if necessary
    }
}
