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
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.matching.MatchVariant;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantDataManager;
import com.ikmb.core.data.variant.VariantStatus;
import com.ikmb.core.data.variant.VariantStatusManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

/**
 * Checks, if the token from the HTTPRequest is valid and if the given user has
 * the rights to see the information of this data (is the owner of the dataset
 * for examples)
 *
 * @author broder
 */
public class HTTPTokenValidator {

    @Inject
    private TokenManager tokenManager;

    @Inject
    private DatasetManager dsManager;

    @Inject
    private VariantDataManager variantManager;

    @Inject
    private VariantStatusManager variantStatusManager;

    @Inject
    private MatchVariantDataManager matchVariantDataManager;

    private String accessToken;

    private String response;

    /**
     * Sets the token for validation via http request
     *
     * @param request
     */
    public void setRequest(HttpServletRequest request) {
        try {
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);
            accessToken = oauthRequest.getAccessToken();
        } catch (OAuthSystemException ex) {
            Logger.getLogger(HTTPTokenValidator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthProblemException ex) {
            Logger.getLogger(HTTPTokenValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * SEts the token for validation via http header
     *
     * @param header
     */
    public void setHeader(String header) {
        if (header != null && header.split(" ") != null) {
            accessToken = header.split(" ")[1];
        }
    }

    public boolean isTokenValid(String header) {
        setHeader(header);
        return isTokenValid();
    }

    /**
     * Checks if the token is valid
     *
     * @return
     */
    public boolean isTokenValid() {
        boolean isValid = false;
        if (accessToken != null) {
            isValid = tokenManager.isTokenValid(accessToken);
        }
        return isValid;
    }

    public String getToken() {
        return accessToken;
    }

    public User getUser() {
        return tokenManager.getUserByToken(accessToken);
    }

    public AuthClient getClient() {
        return tokenManager.getClientByToken(accessToken);
    }

    /**
     * Checks if the user has the rights to see the requested data
     *
     * @param dataId
     * @param aClass
     * @return
     */
    public Boolean hasUserReadPermissions(Long dataId, Class aClass) {
        boolean isValid = isTokenValid();
        if (!isValid) {
            return false;
        }
        String currentUser = getUser().getMail();
        String dataUser = null;
        if (aClass == DatasetVW.class) {
            DatasetVW ds = dsManager.getDatasetByID(dataId);
            dataUser = ds.getUser().getMail();
        } else if (aClass == Variant.class) {
            Variant ds = variantManager.get(dataId);
            dataUser = ds.getDataset().getUser().getMail();
        } else if (aClass == VariantStatus.class) {
            VariantStatus ds = variantStatusManager.get(dataId);
            dataUser = ds.getUser().getMail();
        } else if (aClass == MatchVariant.class) {
            MatchVariant ds = matchVariantDataManager.getVariantMatch(dataId);
            Long variantId = ds.getVariantId();
            Variant variant = variantManager.get(variantId);
            dataUser = variant.getDataset().getUser().getMail();
        }
        if (dataUser == null) {
            return false;
        }
        return dataUser.equals(currentUser);
    }

    public String getResponse() {
        return response;
    }

    public Boolean deleteToken() {
        return tokenManager.deleteToken(accessToken);
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    /**
     * Sets the header, extracts the token and checks if its valid
     *
     * @param header
     * @return
     */
//    public Response isTokenValid(String header) {
//        setHeader(header);
//        boolean isValid = isTokenValid();
//        if (isValid) {
//            VWResponse curResponse = new VWResponse();
//            curResponse.setMessage("Successful");
//            curResponse.setDescription("Token is valid");
//            return Response.status(Response.Status.OK).entity(curResponse).build();
//        } else {
//            VWResponse curResponse = new VWResponse();
//            curResponse.setMessage("Error");
//            curResponse.setDescription("Token is not valid");
//            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(curResponse).build();
//        }
//    }
}
