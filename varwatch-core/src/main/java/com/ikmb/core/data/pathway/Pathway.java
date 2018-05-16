/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.pathway;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author bfredrich
 */
@Entity
@Table(name = "pathway")
public class Pathway implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "name")
    @Expose
    private String identifier;
    @Column(name = "description")
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return identifier;
    }

    public void setName(String name) {
        this.identifier = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
