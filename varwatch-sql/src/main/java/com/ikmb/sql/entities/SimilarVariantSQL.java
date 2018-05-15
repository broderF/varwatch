package com.ikmb.sql.entities;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.beans;
//
//import java.io.Serializable;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import org.hibernate.annotations.Type;
//import org.joda.time.DateTime;
//
///**
// *
// * @author bfredrich
// */
//@Entity
//@Table(name = "similar_variant")
//public class SimilarVariantSQL implements Serializable {
//
//    @Id
//    @GeneratedValue
//    private Integer id;
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "compared_dataset_id", nullable = false)
//    private ComparedDatasetSQL comparedDataset;
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "query_id", nullable = false)
//    private VariantSQL queryVariant;
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "target_id", nullable = false)
//    private VariantSQL targetVariant;
//    @Column(name = "distance")
//    private Double distance;
//    @Column(name = "notified")
//    private Boolean notified;
//    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    @Column(name = "creation_timestamp")
//    private DateTime creationTimestamp;
//    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    @Column(name = "deletion_timestamp")
//    private DateTime deletionTimestamp;
//
//    public SimilarVariantSQL(VariantSQL currentVariant, VariantSQL targetVariant, Double distance) {
//        this.queryVariant = currentVariant;
//        this.targetVariant = targetVariant;
//        this.distance = distance;
//    }
//
//    public SimilarVariantSQL() {
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public VariantSQL getQueryVariant() {
//        return queryVariant;
//    }
//
//    public void setQueryVariant(VariantSQL queryVariant) {
//        this.queryVariant = queryVariant;
//    }
//
//    public VariantSQL getTargetVariant() {
//        return targetVariant;
//    }
//
//    public void setTargetVariant(VariantSQL targetVariant) {
//        this.targetVariant = targetVariant;
//    }
//
//    public Double getDistance() {
//        return distance;
//    }
//
//    public void setDistance(Double distance) {
//        this.distance = distance;
//    }
//
//    public ComparedDatasetSQL getComparedDataset() {
//        return comparedDataset;
//    }
//
//    public void setComparedDataset(ComparedDatasetSQL comparedDataset) {
//        this.comparedDataset = comparedDataset;
//    }
//
//    public Boolean isNotified() {
//        return notified;
//    }
//
//    public void setNotified(Boolean notified) {
//        this.notified = notified;
//    }
//
//    public DateTime getCreationTimestamp() {
//        return creationTimestamp;
//    }
//
//    public void setCreationTimestamp(DateTime creationTimestamp) {
//        this.creationTimestamp = creationTimestamp;
//    }
//
//    public DateTime getDeletionTimestamp() {
//        return deletionTimestamp;
//    }
//
//    public void setDeletionTimestamp(DateTime deletionTimestamp) {
//        this.deletionTimestamp = deletionTimestamp;
//    }
//
//}
