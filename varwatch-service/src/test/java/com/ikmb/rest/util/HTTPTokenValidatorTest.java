/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.util;

import com.ikmb.core.data.auth.token.TokenManager;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author broder
 */
public class HTTPTokenValidatorTest {

    HTTPTokenValidator tokenValidator = new HTTPTokenValidator();
    TokenManager tokenManager = mock(TokenManager.class);
    String header = "Bearer 03a558316b6bd15ac4e503f28ce0cf4a";

    @Test
    public void shouldReturnTokenIsValid() {
        when(tokenManager.isTokenValid("03a558316b6bd15ac4e503f28ce0cf4a")).thenReturn(Boolean.TRUE);
        boolean tokenValid = tokenValidator.isTokenValid(header);
        Assert.assertTrue(tokenValid);
    }

    @Test
    public void shouldReturnTokenIsNotValid() {
        when(tokenManager.isTokenValid("03a558316b6bd15ac4e503f28ce0cf4a")).thenReturn(Boolean.FALSE);
        boolean tokenValid = tokenValidator.isTokenValid(header);
        Assert.assertFalse(tokenValid);
    }

    @Test
    public void shouldReturnTokenIsNotValidForInvalidHeader() {
        when(tokenManager.isTokenValid(null)).thenReturn(Boolean.FALSE);
        boolean tokenValid = tokenValidator.isTokenValid(null);
        Assert.assertFalse(tokenValid);
    }

    @Before
    public void setUp() {
        tokenValidator.setTokenManager(tokenManager);
    }

}
