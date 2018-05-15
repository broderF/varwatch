/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.registration;

import com.google.inject.Inject;
import com.ikmb.core.data.auth.token.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates and validates the token for a user
 *
 * @author broder
 */
public class TokenCreator {

    @Inject
    private TokenManager tokenManager;
    private static final Logger logger = LoggerFactory.getLogger(TokenCreator.class);

    /**
     * Returns a valid token for a user. This could be an prior existing token
     * from the database or a newly created one
     *
     * @param token
     * @param expiresIn
     * @param userName
     * @param clientName
     * @return
     */
    public String getValidToken(String token, Integer expiresIn, String userName, String clientName) {
        String currentToken = tokenManager.getValidToken(userName, clientName);
        logger.info("Current token is {}", currentToken);
        if (currentToken != null) {
            tokenManager.refreshToken(currentToken, userName, clientName, expiresIn);
        } else {
            tokenManager.createNewToken(token, userName, clientName, expiresIn);
            currentToken = token;
        }
        return currentToken;
    }
}
