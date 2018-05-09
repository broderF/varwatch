/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.auth.user;

import com.google.inject.Inject;
import com.ikmb.core.varwatchcommons.entities.Contact;
import com.ikmb.core.varwatchcommons.entities.DefaultUser;
import com.ikmb.core.varwatchcommons.entities.RegistrationUser;
import com.ikmb.core.varwatchcommons.utils.PasswordValidator;

/**
 *
 * @author broder
 */
public class UserBuilder {

    private String preName;
    private String lastName;
    private String phone;
    private String mail;
    private String password;
    private String institution;
    private Boolean active;

    @Inject
    private PasswordValidator passHandler;
    private String reportSchedule;
    private String address;
    private String city;
    private String country;
    private String postalCode;

    public UserBuilder withContact(Contact contact) {
        preName = contact.getPreName();
        lastName = contact.getName();
        phone = contact.getPhone();
        mail = contact.getHref();
        password = contact.getPass();
        return this;
    }

    public UserBuilder secretPass() {
        if (password != null) {
            password = passHandler.createHash(password);
        }
        return this;
    }

    public User build() {
        User user = new User();
        user.setFirstName(preName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setMail(mail);
        user.setPassword(password);
        user.setInstitution(institution);
        if (address != null && city != null && country != null && postalCode != null) {
            user.setAddress(address.trim());
            user.setCity(city.trim());
            user.setCountry(country.trim());
            user.setPostalCode(postalCode.trim());
        }
        return user;
    }

    public Contact buildVWContact() {
        Contact user = new Contact();
        user.setPreName(preName);
        user.setName(lastName);
        user.setPhone(phone);
        user.setHref(mail);
        user.setPass(password);
        return user;
    }

    public DefaultUser buildVWContactWoPassword() {
        DefaultUser user = new DefaultUser();
        user.setFirstName(preName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setMail(mail);
        user.setInstitution(institution);
        user.setAddress(address);
        user.setCity(city);
        user.setCountry(country);
        user.setPostalCode(postalCode);
        user.setReportSchedule(reportSchedule);
        user.setActive(active);
        return user;
    }

    public Contact buildMatchContact() {
        Contact user = new Contact();
        user.setPreName(preName);
        user.setName(lastName);
        user.setPhone(phone);
        user.setHref(mail);
        return user;
    }

    public String getSecretPW(String pw) {
        String newPw = null;
        if (pw != null) {
            newPw = passHandler.createHash(pw);
        }
        return newPw;
    }

    public UserBuilder withRegistrationUser(RegistrationUser user) {
        preName = user.getFirstName().trim();
        lastName = user.getLastName().trim();
        if (user.getPhone() != null) {
            phone = user.getPhone().trim();
        }
        mail = user.getMail().trim();
        password = user.getPassword().getPassword().trim();
        if (user.getInstitution() != null) {
            institution = user.getInstitution().trim();
        }
        address = user.getAddress();
        city = user.getCity();
        country = user.getCountry();
        postalCode = user.getPostalCode();
        return this;
    }

    public UserBuilder withDefaultUser(DefaultUser user) {
        preName = user.getFirstName().trim();
        lastName = user.getLastName().trim();
        if (user.getPhone() != null) {
            phone = user.getPhone().trim();
        }
        mail = user.getMail().trim();
        if (user.getInstitution() != null) {
            institution = user.getInstitution().trim();
        }
        address = user.getAddress();
        city = user.getCity();
        country = user.getCountry();
        postalCode = user.getPostalCode();
        return this;
    }

    public RegistrationUser buildRegistrationUser() {
        RegistrationUser user = new RegistrationUser();
        user.setFirstName(preName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setMail(mail);
        user.setInstitution(institution);
        user.setAddress(address);
        user.setCity(city);
        user.setCountry(country);
        user.setPostalCode(postalCode);
        user.setReportSchedule(reportSchedule);
        user.setActive(active);
        return user;
    }

    public UserBuilder withUser(User contact) {
        preName = contact.getFirstName();
        lastName = contact.getLastName();
        phone = contact.getPhone();
        mail = contact.getMail();
        password = contact.getPassword();
        institution = contact.getInstitution();
        reportSchedule = contact.getReportSchedule();

        active = contact.getActive();
        address = contact.getAddress();
        city = contact.getCity();
        country = contact.getCountry();
        postalCode = contact.getPostalCode();
        return this;
    }
}
