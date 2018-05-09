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
public interface VariantDao {

   

    public void persist(Variant variantSql);

    public void refresh(Variant variantSql);

    public void remove(Variant variant);

    public void removeComplete(Long varId);

    public void removeVariantWithMatches(Variant variantSql);

    public List<Variant> getVariantsByDataset(DatasetVW dataset) ;

    public List<Variant> getVariantsByDatasetWithMatches(DatasetVW dataset);

    public Variant get(Long variantId) ;

}
