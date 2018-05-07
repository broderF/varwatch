/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sanity;

/**
 *
 * @author bfredrich
 */
public class SanityResponse {

    private String responseType;
    private String responseMessage;

    SanityResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public String toString() {
        return responseType + " - " + responseMessage;
    }

}
