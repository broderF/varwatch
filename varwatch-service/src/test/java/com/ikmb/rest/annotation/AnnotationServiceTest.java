/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.annotation;

import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.data.hpo.PhenotypeDataManager;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.Response;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author broder
 */
public class AnnotationServiceTest {

    AnnotationServiceImpl annotationService = new AnnotationServiceImpl();
    DatasetManager datasetManager = mock(DatasetManager.class);

    //dataset information tests -> return hpo list
    @Test
    public void shouldReturnCorrectHpoList() {
        when(datasetManager.getPhenotypes(datasetId)).thenReturn(hpoTerms);

        Response datasetAnnotation = annotationService.getDatasetAnnotation(header, datasetId);
        Assert.assertEquals(hpoResponse, datasetAnnotation.getEntity().toString());
    }

    @Before
    public void setUp() throws Exception {
        annotationService.setDatasetManager(datasetManager);
    }

    Long datasetId = 15l;
    String header = "Bearer 03a558316b6bd15ac4e503f28ce0cf4a";
    List<HPOTerm> hpoTerms = createHpoTerms();

    String hpoResponse = "["
            + "{"
            + "\"identifier\":\"HP:0012115\""
            + "},"
            + "{"
            + "\"identifier\":\"HP:0002240\""
            + "},"
            + "{"
            + "\"identifier\":\"HP:0100280\""
            + "},"
            + "{"
            + "\"identifier\":\"HP:0000952\""
            + "}"
            + "]";

    private List<HPOTerm> createHpoTerms() {
        HPOTerm hpo1 = new HPOTerm();
        hpo1.setIdentifier("HP:0012115");

        HPOTerm hpo2 = new HPOTerm();
        hpo2.setIdentifier("HP:0002240");

        HPOTerm hpo3 = new HPOTerm();
        hpo3.setIdentifier("HP:0100280");

        HPOTerm hpo4 = new HPOTerm();
        hpo4.setIdentifier("HP:0000952");

        return Arrays.asList(hpo1, hpo2, hpo3, hpo4);
    }
}
