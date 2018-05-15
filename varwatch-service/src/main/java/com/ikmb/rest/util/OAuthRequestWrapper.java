/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author bfredrich
 */
public class OAuthRequestWrapper extends HttpServletRequestWrapper {

    private MultivaluedMap<String, String> form;

    public OAuthRequestWrapper(HttpServletRequest request, MultivaluedMap<String, String> form) {
        super(request);
        this.form = form;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (value == null) {
            value = form.getFirst(name);
        }
        return value;
    }
}
