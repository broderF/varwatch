/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest;

/**
 *
 * @author broder
 */
public enum HTTPVarWatchResponse {

    SUBMIT_SUCCESFULL(0, "Dataset successfully saved"),
    //http requests
    HTTP_REQUEST_NOT_PARSABLE(1, "http request not parsable"),
    HTTP_CONTACT_NOT_PARSABLE(2, "http contact not parsable for string"),
    HTTP_CLIENT_NOT_PARSABLE(3, "http client not parsable for string");

    private final int code;
    private final String description;

    private HTTPVarWatchResponse(int code, String description) {
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
