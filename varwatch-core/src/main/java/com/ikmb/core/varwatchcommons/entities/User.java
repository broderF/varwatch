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
public interface User {

    public void setFirstName(String firstname);

    public String getFirstName();

    public void setLastName(String lastname);

    public String getLastName();

    public void setMail(String email);

    public String getMail();

    public void setPhone(String phone);

    public String getPhone();

    public void setInstitution(String institution);

    public String getInstitution();

}
