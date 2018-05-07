/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.variant_data.dataset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikmb.varwatchsql.auth.client.AuthClientSQL;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.data.hpo.HPOTermSQL;
import com.ikmb.varwatchcommons.entities.GenomicFeature;
import com.ikmb.varwatchcommons.entities.VWMatchRequest;
import com.ikmb.varwatchcommons.entities.Dataset;
import com.ikmb.varwatchcommons.entities.Variant;
import com.ikmb.varwatchcommons.utils.ParserHelper;
import com.ikmb.varwatchsql.data.hpo.PhenotypeSQL;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;

/**
 *
 * @author broder
 */
public class DatasetBuilder {

    private DateTime submittedDate;
    private String description;
    private byte[] rawData;
    private DatasetBuilder.RawDataType rawDataType;
    private String rawDataAssembly;
    private boolean completed;

    private Set<PhenotypeSQL> hpoTerms;
    private UserSQL user;
    private AuthClientSQL client;

    private List<Dataset> datasets;
    private List<DatasetVWSQL> datasetsSql;
    private HPOTermSQL ageOfOnset;
    private HPOTermSQL modeOfInheritance;

    public DatasetBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DatasetBuilder withRawData(byte[] rawData) {
        this.rawData = rawData;
        return this;
    }

    public DatasetBuilder withRawDataType(DatasetBuilder.RawDataType rawDataType) {
        this.rawDataType = rawDataType;
        return this;
    }

    public DatasetBuilder withAssembly(String assembly) {
        this.rawDataAssembly = assembly;
        return this;
    }

    public DatasetBuilder withPhenotypes(Set<PhenotypeSQL> hpoTerms) {
        this.hpoTerms = hpoTerms;
        return this;
    }

    public DatasetBuilder withUser(UserSQL user) {
        this.user = user;
        return this;
    }

    public DatasetBuilder withClient(AuthClientSQL client) {
        this.client = client;
        return this;
    }

    public DatasetBuilder withVWMatchRequest(VWMatchRequest submitrequest, DatasetBuilder.RawDataType rawDataType) {
        this.description = submitrequest.getPatient().getDescription();
        this.rawDataType = rawDataType;
        //get raw data.. vcf..raw.. you dont know
        if (rawDataType.equals(RawDataType.VCF)) {
            this.rawData = submitrequest.getPatient().getVcfFile();
        } else {
            this.rawData = getRawDataFromVariants(submitrequest.getPatient().getGenomicFeatures());
        }

        this.rawDataAssembly = submitrequest.getPatient().getAssembly();
        return this;
    }

    public DatasetBuilder withDataset(Dataset submitrequest, DatasetBuilder.RawDataType rawDataType) {
        this.description = submitrequest.getDescription();
        this.rawDataType = rawDataType;
        //get raw data.. vcf..raw.. you dont know
        if (rawDataType.equals(RawDataType.VCF)) {
            this.rawData = submitrequest.getVcfFile();
        } else {
            this.rawData = getRawFromVariants(submitrequest.getVariants());
        }
        return this;
    }

    public DatasetVWSQL buildRawVWSql() {
        DatasetVWSQL datasetSQL = new DatasetVWSQL();
        datasetSQL.setDateSubmitted(new DateTime());
        datasetSQL.setDescription(description);
        datasetSQL.setRawData(rawData);
        datasetSQL.setRawDataType(rawDataType.toString());
        datasetSQL.setRawDataAssembly(rawDataAssembly);
        datasetSQL.setCompleted(false);
        datasetSQL.setPhenotypes(hpoTerms);
        datasetSQL.setClient(client);
        datasetSQL.setUser(user);
        datasetSQL.setInheritanceMode(modeOfInheritance);
        datasetSQL.setAgeOfOnset(ageOfOnset);
        return datasetSQL;
    }

    private byte[] getRawDataFromVariants(List<GenomicFeature> genomicFeatures) {
        byte[] data = null;
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(out, genomicFeatures);

            data = out.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(ParserHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    private byte[] getRawFromVariants(List<Variant> genomicFeatures) {
        byte[] data = null;
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(out, genomicFeatures);

            data = out.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(ParserHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    public DatasetBuilder withDatasetsSQL(List<DatasetVWSQL> datasetsSql) {
        this.datasetsSql = datasetsSql;
        return this;
    }

    public List<Dataset> buildSimpleVW() {
        datasets = new ArrayList<>();
        for (DatasetVWSQL dataset : datasetsSql) {
            Dataset ds = new Dataset();
            ds.setDescription(dataset.getDescription());
            ds.setId(dataset.getId());
            if (dataset.getAgeOfOnset() != null) {
                ds.setAgeOfOnset(dataset.getAgeOfOnset().getIdentifier());
            }
            if (dataset.getInheritanceMode() != null) {
                ds.setModeOfInheritance(dataset.getInheritanceMode().getIdentifier());
            }
            datasets.add(ds);
        }
        return datasets;
    }

    public Dataset buildSimpleVWSingle() {
        if (datasetsSql.isEmpty()) {
            return null;
        }
        DatasetVWSQL dataset = datasetsSql.get(0);
        Dataset ds = new Dataset();
        ds.setDescription(dataset.getDescription());
        ds.setId(dataset.getId());
        if (dataset.getAgeOfOnset() != null) {
            ds.setAgeOfOnset(dataset.getAgeOfOnset().getIdentifier());
        }
        if (dataset.getInheritanceMode() != null) {
            ds.setModeOfInheritance(dataset.getInheritanceMode().getIdentifier());
        }
        ds.setAssembly(dataset.getRawDataAssembly());
        return ds;
    }

    public DatasetBuilder withAgeOfOnset(HPOTermSQL ageOfOnset) {
        this.ageOfOnset = ageOfOnset;
        return this;
    }

    public DatasetBuilder withModeOfInheritance(HPOTermSQL modeOfInheritance) {
        this.modeOfInheritance = modeOfInheritance;
        return this;
    }

    public DatasetBuilder withDatasetSQL(DatasetVWSQL datasetsSql) {
        this.datasetsSql = new ArrayList<>();
        this.datasetsSql.add(datasetsSql);
        return this;

    }

    public enum RawDataType {

        NORMAL, VCF, HGVS;
    }
}
