/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchcommons.entities;

import java.util.List;

/**
 *
 * @author broder
 */
public class Variant {

    private String id;
    private String datasetId;
    private String chromosomeName;
    private Integer position;
    private String referenceBase;
    private String alternateBase;
    private Integer quality;
    private String filter;
    private List<MetaData> metaData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getChromosomeName() {
        return chromosomeName;
    }

    public void setChromosomeName(String chromosomeName) {
        this.chromosomeName = chromosomeName;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getReferenceBase() {
        return referenceBase;
    }

    public void setReferenceBase(String referenceBase) {
        this.referenceBase = referenceBase;
    }

    public String getAlternateBase() {
        return alternateBase;
    }

    public void setAlternateBase(String alternateBase) {
        this.alternateBase = alternateBase;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<MetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<MetaData> metaData) {
        this.metaData = metaData;
    }
}
