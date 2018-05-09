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
public class VWMatchRequest {

    private Patient patient;
    private String clientID;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String cliendID) {
        this.clientID = cliendID;
    }
}
