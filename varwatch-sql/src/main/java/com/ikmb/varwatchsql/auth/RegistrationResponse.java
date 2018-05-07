/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.auth;

/**
 *
 * @author broder
 */
public enum RegistrationResponse {

    //client
    CLIENT_INVALID(0, "Client authentication failed (e.g., unknown client, no client authentication included, or unsupported authentication method)"),
    CLIENT_NAME_NOT_VALID(1, "Client not valid"),
    CLIENT_PASSWORD_NOT_VALID(2, "Client password not valid"),
    CLIENT_SUCCES_REGISTER(3, "Client successfully registered"),
    CLIENT_SAVED(4, "Client saved"),
    CLIENT_ALREADY_IN_DB(5, "Client exists already in db"),
    CLIENT_NOT_SAVED(6, "Client not saved"),
    //token
    TOKEN_NOT_VALID(7, "Token is not valid"),
    TOKEN_VALID(8, "Token is valid"),
    TOKEN_DELETION_SUCCESFULL(8, "Token is deleted"),
    TOKEN_DELETION_ERROR(8, "Token deletion error"),
    //password
    PASSWORD_CHANGED(9, "Password changed"),
    PASSWORD_RESETTED(10, "Password recovery and email sent"),
    USER_NOT_ACTIVE(11, "User account activation pending");

    private final int code;
    private final String description;

    private RegistrationResponse(int code, String description) {
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
