/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.reference_db;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author Broder
 */
@Entity
@Table(name = "ref_database")
public class RefDatabase implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    @Expose
    private String name;
    @Column(name = "version")
    private String version;
    @Column(name = "path")
    @Expose
    private String path;
    @Expose
    @Column(name = "assembly")
    private String assembly;
    @Expose
    @Column(name = "implementation")
    private String implementation;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "updated")
    private Boolean updated;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "timestamp")
    private DateTime lastUpdate;
    @Expose
    @Lob
    @Column(name = "image", nullable = true, columnDefinition = "blob")
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public DateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(DateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Boolean getUpdated() {
        return updated;
    }

    public void setUpdated(Boolean needUpdate) {
        this.updated = needUpdate;
    }

//    RefDatabase toRefDatabase() {
//        RefDatabase refDatabase = new RefDatabaseImpl();
//        ref
//    }
}
