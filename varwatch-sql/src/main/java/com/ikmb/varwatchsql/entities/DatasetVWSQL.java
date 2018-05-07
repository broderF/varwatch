/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.entities;

import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.auth.client.AuthClientSQL;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.data.hpo.HPOTermSQL;
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
public class DatasetVWSQL extends DatasetSQL implements Serializable {

    @Column(name = "description")
    private String description;
    @Column(name = "date_submitted")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateSubmitted;
//    @Column(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserSQL user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private AuthClientSQL client;
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
    private HPOTermSQL inheritanceMode;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "age_of_onset_id", nullable = true)
    private HPOTermSQL ageOfOnset;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dataset_vw")
    private Set<VariantSQL> variants = new HashSet<VariantSQL>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dataset_vw")
    private Set<VariantStatusSQL> variantStatus = new HashSet<VariantStatusSQL>();

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

    public UserSQL getUser() {
        return user;
    }

    public void setUser(UserSQL userID) {
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
    public Set<VariantSQL> getVariants() {
        return variants;
    }

    public void setVariants(Set<VariantSQL> variants) {
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

    public AuthClientSQL getClient() {
        return client;
    }

    public void setClient(AuthClientSQL client) {
        this.client = client;
    }

    public Set<VariantStatusSQL> getVariantStatus() {
        return variantStatus;
    }

    public void setVariantStatus(Set<VariantStatusSQL> variantStatus) {
        this.variantStatus = variantStatus;
    }

    public HPOTermSQL getInheritanceMode() {
        return inheritanceMode;
    }

    public void setInheritanceMode(HPOTermSQL inheritanceMode) {
        this.inheritanceMode = inheritanceMode;
    }

    public HPOTermSQL getAgeOfOnset() {
        return ageOfOnset;
    }

    public void setAgeOfOnset(HPOTermSQL ageOfOnset) {
        this.ageOfOnset = ageOfOnset;
    }
}
