/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.wipe;

import com.ikmb.core.data.dataset.DatasetVW;

/**
 *
 * @author bfredrich
 */
public interface WipeDataDao {

    public void wipeDataset(Long datasetID);

    public void wipeVariantsByDataset(DatasetVW dataset);

    public void deleteUser(String mail);
}
