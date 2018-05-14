/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.dataset;

import com.ikmb.core.data.auth.user.User;
import java.util.List;

/**
 *
 * @author broder
 */
public interface DatasetDao {

    public void save(DatasetVW datasetSQL);

    public DatasetVW getDataset(Long datasetID);

    void update(DatasetVW dataset);

    public void refresh(DatasetVW dataset);

    public List<DatasetVW> getDatasetsByUser(User user);

    public List<Long> getDatasetIDsByUser(User user);

    public DatasetHGMD getHGMDDataset(long datasetID);

    public List<Long> getAllDatasetIds();

}
