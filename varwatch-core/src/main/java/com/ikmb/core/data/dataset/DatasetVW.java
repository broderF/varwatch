/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.dataset;

import com.ikmb.core.auth.client.AuthClient;
import com.ikmb.core.auth.user.User;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantStatus;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author bfredrich
 */
@Entity
@Table(name = "dataset_vw")
public class DatasetVW extends Dataset implements Serializable {

    @Column(name = "description")
    private String description;
    @Column(name = "date_submitted")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateSubmitted;
//    @Column(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private AuthClient client;
    @Lob
    @Column(name = "raw_data", nullable = true)
    @Basic(fetch = FetchType.LAZY)
    private byte[] rawData;
    @Column(name = "raw_data_type")
    private String rawDataType;
    @Column(name = "raw_data_assembly")
    private String rawDataAssembly;
    @Lob
    @Column(name = "vcf_file", nullable = true)
    @Basic(fetch = FetchType.LAZY)
    private byte[] vcfFile;
    @Lob
    @Column(name = "vep_file", nullable = true)
    @Basic(fetch = FetchType.LAZY)
    private byte[] vepFile;
    @Column(name = "completed")
    private Boolean completed;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inheritance_mode_id", nullable = true)
    private HPOTerm inheritanceMode;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "age_of_onset_id", nullable = true)
    private HPOTerm ageOfOnset;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dataset_vw")
    private Set<Variant> variants = new HashSet<Variant>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dataset_vw")
    private Set<VariantStatus> variantStatus = new HashSet<VariantStatus>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(DateTime dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userID) {
        this.user = userID;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    public String getRawDataType() {
        return rawDataType;
    }

    public void setRawDataType(String rawDataType) {
        this.rawDataType = rawDataType;
    }

    public String getRawDataAssembly() {
        return rawDataAssembly;
    }

    public void setRawDataAssembly(String rawDataAssembly) {
        this.rawDataAssembly = rawDataAssembly;
    }

    public byte[] getVcfFile() {
        return vcfFile;
    }

    public void setVcfFile(byte[] vcfFile) {
        this.vcfFile = vcfFile;
    }

//    public PatientSQL getPatient() {
//        return patient;
//    }
//
//    public void setPatient(PatientSQL patient) {
//        this.patient = patient;
//    }
    public Set<Variant> getVariants() {
        return variants;
    }

    public void setVariants(Set<Variant> variants) {
        this.variants = variants;
    }

    public byte[] getVepFile() {
        return vepFile;
    }

    public void setVepFile(byte[] vepFile) {
        this.vepFile = vepFile;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean jobstatus) {
        this.completed = jobstatus;
    }

    public AuthClient getClient() {
        return client;
    }

    public void setClient(AuthClient client) {
        this.client = client;
    }

    public Set<VariantStatus> getVariantStatus() {
        return variantStatus;
    }

    public void setVariantStatus(Set<VariantStatus> variantStatus) {
        this.variantStatus = variantStatus;
    }

    public HPOTerm getInheritanceMode() {
        return inheritanceMode;
    }

    public void setInheritanceMode(HPOTerm inheritanceMode) {
        this.inheritanceMode = inheritanceMode;
    }

    public HPOTerm getAgeOfOnset() {
        return ageOfOnset;
    }

    public void setAgeOfOnset(HPOTerm ageOfOnset) {
        this.ageOfOnset = ageOfOnset;
    }
}
