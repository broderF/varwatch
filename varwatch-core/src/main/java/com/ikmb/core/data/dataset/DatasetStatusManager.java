/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.dataset;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.dataset.DatasetStatusBuilder.DatasetStatusType;
import com.ikmb.core.varwatchcommons.entities.Status;
import java.util.ArrayList;
import java.util.List;

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
        DatasetVW dataset = datasetDao.getDataset(datasetID);
        User user = dataset.getUser();
        DatasetStatus dsStatussql = dsStatusBuilder.withDataset(dataset).withUser(user).withStatus(status).buildSql();
        dsStatusDao.save(dsStatussql);
    }

    @Transactional
    public Status getLastStatus(Long id) {
        List<DatasetStatus> statusDatasets = dsStatusDao.getStatus(id);
        DatasetStatus lastStatus = null;
        for (DatasetStatus currentStatus : statusDatasets) {
            if (lastStatus == null || lastStatus.getTimestamp().isBefore(currentStatus.getTimestamp()) || (lastStatus.getTimestamp().isEqual(currentStatus.getTimestamp()) && lastStatus.getId() < currentStatus.getId())) {
                lastStatus = currentStatus;
            }
        }
        return dsStatusBuilder.withStatusSQL(lastStatus).build();
    }

    @Transactional
    public List<Status> getAllStatus(Long id) {
        List<DatasetStatus> statusDatasetsSql = dsStatusDao.getStatus(id);
        List<Status> statusDatasets = new ArrayList<>();
        for (DatasetStatus curDsSql : statusDatasetsSql) {
            Status build = dsStatusBuilder.withStatusSQL(curDsSql).build();
            statusDatasets.add(build);
        }
        return statusDatasets;
    }

}
