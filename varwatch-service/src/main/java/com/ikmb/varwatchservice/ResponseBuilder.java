/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchservice;

import com.google.gson.Gson;
import com.ikmb.core.varwatchcommons.entities.VWResponse;
import com.ikmb.core.varwatchcommons.entities.Status;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

/**
 *
 * @author bfredrich
 */
public class ResponseBuilder {

    private String vwType;
    private String vwMessage;
    private StatusType statusType;

    public static final String INVALID_CLIENT_DESCRIPTION = "Client authentication failed (e.g., unknown client, no client authentication included, or unsupported authentication method).";

    public ResponseBuilder withVwError() {
        this.vwType = "Error";
        return this;
    }

    public ResponseBuilder withVwMessage(String message) {
        this.vwMessage = message;
        return this;
    }

    public ResponseBuilder withStatusType(StatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public Response build() {
        VWResponse response = new VWResponse();
        response.setMessage(vwType);
        response.setDescription(vwMessage);
        String entity = new Gson().toJson(response, VWResponse.class);
        return Response.status(statusType).entity(entity).build();
    }

    public ResponseBuilder withVwSuccessful() {
        this.vwType = "Successful";
        this.statusType = Response.Status.OK;
        return this;
    }
}
