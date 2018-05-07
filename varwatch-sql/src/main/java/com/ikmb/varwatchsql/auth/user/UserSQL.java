/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.auth.user;

import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import java.io.Serializable;
import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author Broder
 */
@Entity
@Table(name = "auth_user")
public class UserSQL implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "institution")
    private String institution;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "address")
    private String address;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;
    @Column(name = "phone")
    private String phone;
    @Column(name = "status")
    private String status;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<DatasetVWSQL> datasets = new HashSet<DatasetVWSQL>();
    @Column(name = "pw_timestamp")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime pwTimestamp;
    @Column(name = "pw_source", nullable = true)
    private String pwSource;
    @Column(name = "report_schedule", nullable = true)
    private String reportSchedule = "never";
    @Column(name = "last_report")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastReport;
    @Column(name = "active")
    private Boolean active;

    @Column(name = "registration_form")
    @Lob
    private Blob registrationForm;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public DateTime getLastReport() {
        return lastReport;
    }

    public void setLastReport(DateTime lastReport) {
        this.lastReport = lastReport;
    }

    public UserSQL() {
    }

    public UserSQL(String name, String prename, String mail, String password, String phone) {
        this.last_name = name;
        this.first_name = prename;
        this.email = mail;
        this.password = password;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getMail() {
        return email;
    }

    public void setMail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<DatasetVWSQL> getDatasets() {
        return datasets;
    }

    public void setDatasets(Set<DatasetVWSQL> datasets) {
        this.datasets = datasets;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getPwTimestamp() {
        return pwTimestamp;
    }

    public void setPwTimestamp(DateTime pwTimestamp) {
        this.pwTimestamp = pwTimestamp;
    }

    public String getPwSource() {
        return pwSource;
    }

    public void setPwSource(String pwSource) {
        this.pwSource = pwSource;
    }

    public String getReportSchedule() {
        return reportSchedule;
    }

    public void setReportSchedule(String reportSchedule) {
        this.reportSchedule = reportSchedule;
    }

    public Blob getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationForm(Blob registrationForm) {
        this.registrationForm = registrationForm;
    }

}
