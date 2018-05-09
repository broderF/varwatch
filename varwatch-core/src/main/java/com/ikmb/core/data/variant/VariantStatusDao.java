/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.variant;

import com.ikmb.core.data.dataset.DatasetVW;
import java.util.List;

/**
 *
 * @author broder
 */
public interface VariantStatusDao {


    public void save(VariantStatus variantStatus);

    public List<VariantStatus> get(Variant variant) ;

    List<VariantStatus> getStatusFromDataset(DatasetVW dataset) ;

    public List<VariantStatus> getStatus(Long id) ;

    VariantStatus getStatusByID(Long statusID);

    List<VariantStatus> getMatchedStatus(Long id) ;

    List<VariantStatus> getVwMatchedStatus(Long id);

    List<VariantStatus> getHgmdMatchedStatus(Long id) ;

    List<VariantStatus> getNonMatchedStatus(Long id) ;

    List<VariantStatus> getBeaconMatchedStatus(Long id) ;
}
