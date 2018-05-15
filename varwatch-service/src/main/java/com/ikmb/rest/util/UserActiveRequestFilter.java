/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.util;

import com.google.inject.Inject;
import com.ikmb.core.data.auth.RegistrationResponse;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.rest.util.UserActiveRequestFilter.UserActiveFilter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.ws.rs.NameBinding;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
@Provider
@UserActiveFilter
public class UserActiveRequestFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(UserActiveRequestFilter.class);
    @Inject
    private HTTPTokenConverter tokenConverter;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        String header = ctx.getHeaderString("authorization");
        User user = tokenConverter.getUserFromHeader(header);

        if (!user.getActive()) {
            ctx.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.USER_NOT_ACTIVE.getDescription()).build());
        }
    }

    @NameBinding
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserActiveFilter {
    }
}
