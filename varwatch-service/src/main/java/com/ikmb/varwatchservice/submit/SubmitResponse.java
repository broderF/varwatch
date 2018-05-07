/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice.submit;

/**
 *
 * @author broder
 */
public enum SubmitResponse {

    SUBMIT_SUCCESFULL(0, "Dataset successfully saved");

    private final int code;
    private final String description;

    private SubmitResponse(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
