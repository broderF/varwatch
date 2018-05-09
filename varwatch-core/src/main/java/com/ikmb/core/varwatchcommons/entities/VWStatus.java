/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.varwatchcommons.entities;

/**
 *
 * @author bfredrich
 */
public class VWStatus {

    private String status;
    private String statusValue;
    private String timestamp;
//    private VWMatchDetails matchDetails;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

//    public VWMatchDetails getMatchDetails() {
//        return matchDetails;
//    }
//
//    public void setMatchDetails(VWMatchDetails matchDetails) {
//        this.matchDetails = matchDetails;
//    }
}
