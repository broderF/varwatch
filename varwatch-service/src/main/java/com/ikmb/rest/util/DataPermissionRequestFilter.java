/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.util;

import com.ikmb.core.data.auth.RegistrationResponse;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.matching.MatchVariant;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.rest.util.DataPermissionRequestFilter.DataPermissionFilter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.NameBinding;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
@Provider
@DataPermissionFilter
public class DataPermissionRequestFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;
    @Context
    private PermissionValidator permissionValidator;

    private static final Logger logger = LoggerFactory.getLogger(DataPermissionRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        String authHeader = ctx.getHeaderString("authorization");
        DataPermissionFilter annotation = resourceInfo.getResourceMethod().getAnnotation(DataPermissionFilter.class);
        if (annotation != null) {
            Class dataType = annotation.dataType();
            MultivaluedMap<String, String> pathParameters = ctx.getUriInfo().getPathParameters();

            String id = pathParameters.getFirst(classToParamMap().get(dataType));
            if (!permissionValidator.hasUserReadPermissions(authHeader, Long.parseLong(id), annotation.dataType())) {
                ctx.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.USER_NO_PERMISSION.getDescription()).build());
            }
        } else {
            ctx.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE).entity(RegistrationResponse.USER_NO_PERMISSION.getDescription()).build());
        }
    }

    public static Map<Class, String> classToParamMap() {
        Map<Class, String> classToParam = new HashMap<>();
        classToParam.put(DatasetVW.class, "dataset_id");
        classToParam.put(Variant.class, "variant_id");
        classToParam.put(MatchVariant.class, "match_id");
        return classToParam;
    }

    @NameBinding
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DataPermissionFilter {

        Class dataType() default DatasetVW.class;

    }

}
