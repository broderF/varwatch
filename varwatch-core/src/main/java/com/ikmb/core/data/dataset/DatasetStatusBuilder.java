/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.dataset;

import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.varwatchcommons.entities.Status;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author broder
 */
public class DatasetStatusBuilder {

    private String dateTimeFormat = "yyy-MM-dd HH:mm:ss";
    private String status;
    private String description;
    private DateTime dateTime;
    private User user;
    private DatasetVW dataset;

    public DatasetStatusBuilder withStatus(DatasetStatusType status) {
        this.status = status.getStatus();
        this.description = status.getDescription();
        return this;
    }

    public DatasetStatusBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public DatasetStatusBuilder withDataset(DatasetVW dataset) {
        this.dataset = dataset;
        return this;
    }

    public DatasetStatus buildSql() {
        DatasetStatus dsStatussql = new DatasetStatus();
        dsStatussql.setDataset(dataset);
        dsStatussql.setDescription(description);
        dsStatussql.setStatus(status);
        dsStatussql.setTimestamp(new DateTime());
        dsStatussql.setUser(user);
        return dsStatussql;
    }

    public Status build() {
        Status status = new Status();
        status.setDescription(description);
        status.setStatus(this.status);
        status.setDatetime(dateTime.toString(DateTimeFormat.fullDateTime()));
        return status;
    }

    public DatasetStatusBuilder withStatusSQL(DatasetStatus lastStatus) {
        this.status = lastStatus.getStatus();
        this.description = lastStatus.getDescription();
        this.dateTime = lastStatus.getTimestamp();
        return this;
    }

    public enum DatasetStatusType {

        REFECTED("rejected", "rejected cause of ..."),
        REFECTED_DATA("rejected", "rejected because varwatch is not able to parse the data. maybe the format isnt correct"),
        REFECTED_HPO("rejected", "rejected cause of missing HPO"),
        SUBMITTED("submitted", "data is submitted and waiting for processing"),
        PROCESSED("processing", "data is being processed"),
        OBSERVED("watching", "data has been processed and all variants are in the watchlist"),
        REFECTED_MAX_NUMBER("rejected", "rejected cause of variant number > 100");

        private String description;
        private String status;

        DatasetStatusType(String status, String description) {
            this.status = status;
            this.description = description;
        }

        public String getStatus() {
            return status;
        }

        public String getDescription() {
            return description;
        }

    }
}
