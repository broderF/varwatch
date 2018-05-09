/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.dataset;

import java.util.List;

/**
 *
 * @author broder
 */
public interface DatasetStatusDao {

    public void save(DatasetStatus dsStatussql);

    public List<DatasetStatus> getStatus(Long id);

}
