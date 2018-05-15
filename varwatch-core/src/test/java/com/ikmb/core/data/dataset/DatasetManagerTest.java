/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.dataset;

import com.google.common.collect.Sets;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.data.hpo.Phenotype;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author broder
 */
public class DatasetManagerTest {

    DatasetManager dsManager = new DatasetManager();
    DatasetDao datasetDao = mock(DatasetDao.class);
    Long datasetId = 15l;

    @Test
    public void shouldReturnAllPhenotypesFromDataset() {
        List<HPOTerm> phenotypes = dsManager.getPhenotypes(datasetId);
        Assert.assertEquals(2,phenotypes.size());
        //Todo compare each value
    }

    @Before
    public void setUp() {
        dsManager.setDatasetDao(datasetDao);
        DatasetVW datasetvw = mock(DatasetVW.class);
        when(datasetDao.getDataset(datasetId)).thenReturn(datasetvw);
        when(datasetvw.getPhenotypes()).thenReturn(getPhenotypes());
    }

    private Set<Phenotype> getPhenotypes() {
        Phenotype p1 = new Phenotype();
        HPOTerm t1 = new HPOTerm();
        t1.setIdentifier("HP:0012115");
        p1.setPhenotype(t1);

        Phenotype p2 = new Phenotype();
        HPOTerm t2 = new HPOTerm();
        t2.setIdentifier("HP:0002240");
        p2.setPhenotype(t2);
        return Sets.newHashSet(p1,p2);
    }

}
