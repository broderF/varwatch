/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.variant;

import com.ikmb.core.auth.user.User;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.matching.MatchVariant;
import com.ikmb.core.varwatchcommons.entities.VWStatus;
import com.ikmb.core.varwatchcommons.entities.Status;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author broder
 */
public class VariantStatusBuilder {

    private Long id;
//    private String dateTimeFormat = "yyy-MM-dd HH:mm:ss";
    private String status;
    private String message;
    private String messageAddition = "";

    private DatasetVW dataset;
    private User user;
    private String rawVariant;
    private DateTime dateTime;
//    private MatchSQL match;
    private String variantHash;
    private Long variandId;
    private MatchVariant matchvariant;
    private Long matchVariantId;

    public VariantStatusBuilder withStatus(VariantStatusTerm status) {
        this.status = status.toString();
        return this;
    }

    public VariantStatusBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public VWStatus buildVWStatus() {
        VWStatus vwstatus = new VWStatus();
        vwstatus.setStatus(status);
        vwstatus.setStatusValue(message + messageAddition);
        this.clear();
        return vwstatus;
    }

    public VariantStatusBuilder withStatusVW(VWStatus status, String rawVariant, String variantHash, DatasetVW dataset, User user) {
        this.status = status.getStatus();
        this.message = status.getStatusValue();
        this.variantHash = variantHash;
        this.dataset = dataset;
        this.user = user;
        this.rawVariant = rawVariant;
        return this;
    }

    public VariantStatus buildSql() {
        VariantStatus variantStatus = new VariantStatus();
        variantStatus.setVariantRaw(rawVariant);
        variantStatus.setStatus(status);
        variantStatus.setStatusValue(message);
        variantStatus.setUser(user);
        variantStatus.setDataset(dataset);
        variantStatus.setVariantHash(variantHash);
        variantStatus.setTimestamp(new DateTime());
        if (matchvariant != null) {
            variantStatus.setMatchVariant(matchvariant);
        }
        this.clear();
        return variantStatus;
    }

    public VariantStatusBuilder withMessageAddition(String transcriptName) {
        messageAddition = transcriptName;
        return this;
    }

    public VariantStatusBuilder withStatusSQL(VariantStatus lastStatus) {
        this.id = lastStatus.getId();
        this.status = lastStatus.getStatus();
        this.message = lastStatus.getStatusValue();
        this.dateTime = lastStatus.getTimestamp();
        this.variandId = null;
        if (lastStatus.getVariant() != null) {
            this.variandId = lastStatus.getVariant().getId();
        }
        this.matchVariantId = null;
        if (lastStatus.getStatus().equals("MATCHED") && lastStatus.getMatchVariant() != null) {
            this.matchVariantId = lastStatus.getMatchVariant().getId();
        }
        return this;
    }

    public Status build() {
        Status status = new Status();
        status.setId(id);
        status.setDescription(message);
        status.setStatus(this.status);
        status.setDatetime(dateTime.toString(DateTimeFormat.longDateTime()));
        status.setVariantId(variandId);
        if (matchVariantId != null) {
            status.setMatchVariantId(matchVariantId);
        }
        this.clear();
        return status;
    }

//    public VariantStatusBuilder withMatch(MatchSQL matchsql) {
//        this.match = matchsql;
//        return this;
//    }
    private void clear() {
        messageAddition = "";
    }

    public VariantStatusBuilder withVariantMatch(MatchVariant matchVariant) {
        this.matchvariant = matchVariant;
        return this;
    }

    public enum VariantStatusTerm {

        REJECTED, MATCHED, STORED;
    }

    public enum VariantStatusMessage {

        HGVS_NOT_CONVERTABLE("HGVS not convertable"),
        ASSEMBLY_NOT_MAPABLE("Variant not mappable to GRCh38"),
        MAX_INDEL_EXEEDED("Max Indel exceeded"),
        IMPACT_TOO_LOW("Variant consequence with low severity"),
        NO_TRANSCRIPT_FOUND("Transcript not found in Database: "),
        MATCHED_TO_VARWATCH("Variant matched to VarWatch"),
        MATCHED_TO_HGMD("Variant matched to HGMD"),
        MATCHED_TO_BEACON("Identical Variant in "),
        STORED_IN_VARWATCH("Variant is stored in VarWatch");

        private String message;

        private VariantStatusMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

    }
}
