/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.util;

import com.google.inject.Inject;
import com.ikmb.core.data.auth.client.AuthClient;
import com.ikmb.core.data.auth.token.TokenManager;
import com.ikmb.core.data.auth.user.User;

/**
 *
 * @author broder
 */
public class HTTPTokenConverter {

    @Inject
    TokenManager tokenManager;

    public String getToken(String header) {
        String accessToken = null;
        if (header != null && header.split(" ") != null) {
            accessToken = header.split(" ")[1];
        }
        return accessToken;
    }

    public User getUserFromHeader(String header) {
        String token = getToken(header);
        return tokenManager.getUserByToken(token);
    }

    public AuthClient getClientFromHeader(String header) {
        String token = getToken(header);
        return tokenManager.getClientByToken(token);
    }
}
