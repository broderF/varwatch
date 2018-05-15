/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.data.dataset;

import com.ikmb.core.data.auth.client.AuthClient;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.auth.user.UserManager;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
import db.TestDatabaseLoader;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author broder
 */
public class DatasetDaoSQLTest {

    TestDatabaseLoader tdl = new TestDatabaseLoader();
    DatasetManager datasetDaoSQL;
    User testUser;
    AuthClient client;
    UserManager um;

    @Test
    public void shouldBeAbleToSaveAndLoadDatasetFromDatabase() {
        DatasetVW savedDataset = new DatasetVW();
        savedDataset.setUser(testUser);
        savedDataset.setClient(client);
        savedDataset.setDescription("Testdataset");
        List<User> allUser = um.getAllUser();
        System.out.println(allUser.size());
//        datasetDaoSQL.persistRawData(savedDataset);
        List<Long> allDatasetIds = datasetDaoSQL.getAllDatasetIds();
        System.out.println(allDatasetIds.size());
//        DatasetVW loadedDataset = datasetDaoSQL.getDataset(1l);
//        Assert.assertEquals(savedDataset.getId(), loadedDataset.getId());
//        Assert.assertEquals(savedDataset.getDescription(), loadedDataset.getDescription());
    }

    @Before
    public void setUp() {
        tdl = new TestDatabaseLoader();
        testUser = tdl.getTestUser();
        client = tdl.getClient();
        datasetDaoSQL = tdl.getInj().getInstance(DatasetManager.class);
        um = tdl.getInj().getInstance(UserManager.class);
        //Todo get db dump from prod server and fill this database
    }

}
