/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.wipe;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.ikmb.varwatchsql.guice.VarWatchInjector;
import com.ikmb.varwatchsql.guice.VarWatchPersist;
import com.ikmb.varwatchsql.auth.user.UserManager;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetManager;
import java.util.List;

/**
 *
 * @author bfredrich
 */
public class WipeDataManager {

    @Inject
    private WipeDataDao wipeDataDao;

    @Inject
    private DatasetManager dsManager;

    public String wipeDataByUser(String email) {
        List<Long> dsIDs = dsManager.getDatasetIDsByUser(email);
        for (Long currentID : dsIDs) {
//        System.out.println("Current id: " + dsIDs.get(0));
            wipeDataByDataset(currentID);
        }
        return "data deleted";
    }

    @Transactional
    public String wipeDataByDataset(Long dsID) {
        wipeDataDao.wipeDataset(dsID);
        return "data deleted";
    }

    @Transactional
    public String wipeVariantsFromDataset(Long datasetID) {
        DatasetVWSQL dataset = dsManager.getDatasetByID(datasetID);
        wipeDataDao.wipeVariantsByDataset(dataset);
        return "data deleted";
    }

    public static void main(String[] args) {
        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
        WipeDataManager vdm = inj.getInstance(WipeDataManager.class);
        UserManager udm = inj.getInstance(UserManager.class);
//        vdm.wipeDataByDataset(108494l);
//        for (UserSQL user : udm.getAllUser()) {
//            vdm.wipeDataByUser(user.getMail());
//        }
        vdm.wipeDataByUser("b.fredrich@ikmb.uni-kiel.de");
//        for (int i = 1; i <= 30; i++) {
//            vdm.wipeDataByUser("user"+i);
//        }
        System.out.println("finish");
    }
}
