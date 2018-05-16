/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.annotation;

import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.family.GeneFamily;
import com.ikmb.core.data.gene.Gene;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.data.pathway.Pathway;
import com.ikmb.core.data.variant.VariantDataManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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

    AnnotationService annotationService = new AnnotationService();
    DatasetManager datasetManager = mock(DatasetManager.class);
    VariantDataManager variantManager = mock(VariantDataManager.class);
    Long dataId = 15l;
    String header = "Bearer 03a558316b6bd15ac4e503f28ce0cf4a";

    @Test
    public void shouldReturnCorrectHpos() {
        List<HPOTerm> hpoTerms = createHpoTerms();
        String expectedHpoResponse = getHpoResponseFromFile();
        when(datasetManager.getPhenotypes(dataId)).thenReturn(hpoTerms);
        Response datasetAnnotation = annotationService.getDatasetAnnotation(header, dataId);
        Assert.assertEquals(expectedHpoResponse, datasetAnnotation.getEntity().toString());
    }

    @Test
    public void shouldReturnCorrecGenes() {
        List<Gene> genes = createGenes();
        String expectedGeneResponse = getGeneResponseFromFile();
        when(variantManager.getGenesFromVariant(dataId)).thenReturn(genes);
        Response response = annotationService.getVariantGenes(header, dataId);
        Assert.assertEquals(expectedGeneResponse, response.getEntity().toString());
    }

    @Test
    public void shouldReturnCorrecPathways() {
        List<Pathway> pathways = createPathways();
        String expectedPathwayResponse = getPathwayResponseFromFile();
        when(variantManager.getPathwaysFromVariant(dataId)).thenReturn(pathways);
        Response response = annotationService.getVariantPathways(header, dataId);
        Assert.assertEquals(expectedPathwayResponse, response.getEntity().toString());
    }

    @Test
    public void shouldReturnCorrecFamilies() {
        List<GeneFamily> families = createGeneFamilies();
        String expectedFamilyResponse = getFamilyResponseFromFile();
        when(variantManager.getFamiliesFromVariant(dataId)).thenReturn(families);
        Response response = annotationService.getVariantFamilies(header, dataId);
        Assert.assertEquals(expectedFamilyResponse, response.getEntity().toString());
    }

    @Before
    public void setUp() throws Exception {
        annotationService.setDatasetManager(datasetManager);
        annotationService.setVariantManager(variantManager);
    }

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

    private String getHpoResponseFromFile() {
        return getStringFromFile("src/test/resources/ExpectedHpoResponse.json");
    }

    private List<Gene> createGenes() {
        Gene gene1 = new Gene();
        gene1.setSymbol("BDNF");
        gene1.setName("ENSG00000176697");

        return Arrays.asList(gene1);
    }

    private String getGeneResponseFromFile() {
        return getStringFromFile("src/test/resources/ExpectedGeneResponse.json");

    }

    private List<Pathway> createPathways() {
        Pathway path1 = new Pathway();
        path1.setName("path:hsa05034");

        Pathway path2 = new Pathway();
        path2.setName("path:hsa04722");

        Pathway path3 = new Pathway();
        path3.setName("path:hsa05030");

        Pathway path4 = new Pathway();
        path4.setName("path:hsa04024");

        return Arrays.asList(path1, path2, path3, path4);
    }

    private String getPathwayResponseFromFile() {
        return getStringFromFile("src/test/resources/ExpectedPathwayResponse.json");
    }

    private List<GeneFamily> createGeneFamilies() {
        GeneFamily fam1 = new GeneFamily();
        fam1.setName("PTHR11589");
        fam1.setDescription("PRECURSOR");

        return Arrays.asList(fam1);
    }

    private String getFamilyResponseFromFile() {
        return getStringFromFile("src/test/resources/ExpectedFamilyResponse.json");
    }

    private String getStringFromFile(String filePath) {
        List<String> readAllLines = new ArrayList<>();
        try {
            readAllLines = Files.readAllLines(Paths.get(filePath));
        } catch (IOException ex) {
            Logger.getLogger(AnnotationServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        String collectJoin = readAllLines.stream().map(String::trim).collect(Collectors.joining()).replaceAll("\\s", "");
        return collectJoin;
    }

}
