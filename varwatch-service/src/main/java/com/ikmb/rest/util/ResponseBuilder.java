/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.varwatchcommons.entities.VWResponse;
import com.ikmb.core.varwatchcommons.entities.Status;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

/**
 *
 * @author bfredrich
 * @param <T>
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

    public Response buildListWithExpose(List<?> value) {
        JsonArray result = (JsonArray) new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJsonTree(value,
                new TypeToken<List<?>>() {
                }.getType());
        String currentOutput = result.toString();
        System.out.println(currentOutput);
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }
        public Response buildList(List<?> value) {
        JsonArray result = (JsonArray) new GsonBuilder().create().toJsonTree(value,
                new TypeToken<List<?>>() {
                }.getType());
        String currentOutput = result.toString();
        System.out.println(currentOutput);
        return Response.status(Response.Status.OK).entity(currentOutput).build();
    }
}
