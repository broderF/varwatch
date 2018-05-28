/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.extraction;

import com.google.inject.Inject;
import com.ikmb.core.varwatchcommons.entities.VWStatus;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import com.ikmb.core.utils.VariantHash;
import com.ikmb.core.data.variant.VariantStatusBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author broder
 */
public class VariantIndelChecker {

    @Inject
    private VariantStatusBuilder variantStatusBuilder;

    private List<VWVariant> correctVariants = new ArrayList<>();

    private Map<VWVariant, VWStatus> errorVariants = new HashMap<>();

    private Integer MAX_BASE_LENGTH = VariantHash.MAX_BASE_LENGTH;

    public void filterVariants(List<VWVariant> inputVariants) {
        for (VWVariant variant : inputVariants) {
            if (variant.getAlternateBases().length() <= MAX_BASE_LENGTH && variant.getReferenceBases().length() <= MAX_BASE_LENGTH) {
                correctVariants.add(variant);
            } else {
                VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.MAX_INDEL_EXEEDED.getMessage()).buildVWStatus();
                errorVariants.put(variant, status);
            }
        }
    }

    public boolean isVariantCorrect(VWVariant variant) {
        if (variant.getAlternateBases().length() <= MAX_BASE_LENGTH && variant.getReferenceBases().length() <= MAX_BASE_LENGTH) {
            return true;
        } else {
            VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.MAX_INDEL_EXEEDED.getMessage()).buildVWStatus();
            return false;
        }
    }

    public List<VWVariant> getCorrectVariants() {
        return correctVariants;
    }

    public Map<VWVariant, VWStatus> getErrorVariants() {
        return errorVariants;
    }
}
