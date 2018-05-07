/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.status.dataset;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.varwatchcommons.entities.Status;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetDao;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetStatusSQL;
import com.ikmb.varwatchsql.status.dataset.DatasetStatusBuilder.DatasetStatusType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceException;

/**
 *
 * @author broder
 */
public class DatasetStatusManager {

    @Inject
    private DatasetDao datasetDao;

    @Inject
    private DatasetStatusDao dsStatusDao;

    @Inject
    private DatasetStatusBuilder dsStatusBuilder;

    @Transactional
    public void createNewStatus(Long datasetID, DatasetStatusType status) {
        DatasetVWSQL dataset = datasetDao.getDataset(datasetID);
        UserSQL user = dataset.getUser();
        DatasetStatusSQL dsStatussql = dsStatusBuilder.withDataset(dataset).withUser(user).withStatus(status).buildSql();
        dsStatusDao.save(dsStatussql);
    }

    @Transactional
    public Status getLastStatus(Long id) {
        List<DatasetStatusSQL> statusDatasets = dsStatusDao.getStatus(id);
        DatasetStatusSQL lastStatus = null;
        for (DatasetStatusSQL currentStatus : statusDatasets) {
            if (lastStatus == null || lastStatus.getTimestamp().isBefore(currentStatus.getTimestamp()) || (lastStatus.getTimestamp().isEqual(currentStatus.getTimestamp()) && lastStatus.getId() < currentStatus.getId())) {
                lastStatus = currentStatus;
            }
        }
        return dsStatusBuilder.withStatusSQL(lastStatus).build();
    }

    @Transactional
    public List<Status> getAllStatus(Long id) {
        List<DatasetStatusSQL> statusDatasetsSql = dsStatusDao.getStatus(id);
        List<Status> statusDatasets = new ArrayList<>();
        for (DatasetStatusSQL curDsSql : statusDatasetsSql) {
            Status build = dsStatusBuilder.withStatusSQL(curDsSql).build();
            statusDatasets.add(build);
        }
        return statusDatasets;
    }

}
