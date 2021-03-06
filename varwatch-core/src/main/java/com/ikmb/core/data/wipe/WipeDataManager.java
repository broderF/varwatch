/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.wipe;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
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
        DatasetVW dataset = dsManager.getDatasetByID(datasetID);
        wipeDataDao.wipeVariantsByDataset(dataset);
        return "data deleted";
    }

//    public static void main(String[] args) {
//        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
//        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
//        WipeDataManager vdm = inj.getInstance(WipeDataManager.class);
//        UserManager udm = inj.getInstance(UserManager.class);
////        vdm.wipeDataByDataset(108494l);
////        for (UserSQL user : udm.getAllUser()) {
////            vdm.wipeDataByUser(user.getMail());
////        }
//        vdm.wipeDataByUser("b.fredrich@ikmb.uni-kiel.de");
////        for (int i = 1; i <= 30; i++) {
////            vdm.wipeDataByUser("user"+i);
////        }
//        System.out.println("finish");
//    }

    @Transactional
    public void wipeUser(String mail) {
        wipeDataByUser(mail);
        wipeDataDao.deleteUser(mail);
    }
}
