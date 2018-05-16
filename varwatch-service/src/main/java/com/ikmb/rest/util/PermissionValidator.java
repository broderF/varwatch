/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.util;

import com.google.inject.Inject;
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

/**
 *
 * @author broder
 */
public class PermissionValidator {

    @Inject
    private HTTPVarWatchInputConverter tokenConverter;
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

    public Boolean hasUserReadPermissions(String header, Long dataId, Class aClass) {
        String token = tokenConverter.getToken(header);
        User user = tokenManager.getUserByToken(token);
        String currentUser = user.getMail();
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

}
