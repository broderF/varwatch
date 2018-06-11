/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.varwatchcommons.entities;

/**
 *
 * @author broder
 */
public class DefaultUser implements User {

    private String firstName;
    private String lastName;
    private String mail;
    private String phone;
    private String institution;
//    private Address address;
    private String reportSchedule;

    private String address;
    private String postalCode;
    private String city;
    private String country;
    private Boolean active;
    private Boolean isAdmin;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public String getReportSchedule() {
        return reportSchedule;
    }

    public void setReportSchedule(String reportSchedule) {
        this.reportSchedule = reportSchedule;
    }

    @Override
    public void setFirstName(String firstname) {
        this.firstName = firstname;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setMail(String email) {
        this.mail = email;
    }

    @Override
    public String getMail() {
        return mail;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    @Override
    public String getInstitution() {
        return institution;
    }

//    @Override
//    public void setAddress(Address address) {
//        this.address = address;
//    }
//
//    @Override
//    public Address getAddress() {
//        return address;
//    }
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }
}
