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
public class ExternalMatchInformation extends MatchInformation {

    private Long queryVariantId;

    private String accIdentifier;
    private Double hpoDist;
    private Boolean identicalMatch;

    public Boolean getIsIdentical() {
        return identicalMatch;
    }

    public void setIsIdentical(Boolean isIdentical) {
        this.identicalMatch = isIdentical;
    }
    
    public Double getHpoDist() {
        return hpoDist;
    }

    public void setHpoDist(Double hpoDist) {
        this.hpoDist = hpoDist;
    }

    public Long getQueryVariantId() {
        return queryVariantId;
    }

    public void setQueryVariantId(Long queryVariantId) {
        this.queryVariantId = queryVariantId;
    }

    public String getAccIdentifier() {
        return accIdentifier;
    }

    public void setAccIdentifier(String accIdentifier) {
        this.accIdentifier = accIdentifier;
    }

}
