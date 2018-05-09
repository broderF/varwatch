/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.ensembl;

//import com.ikmb.varwatchsql.PersistenceManager;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "ensembl_version")
public class Ensembl implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "description")
    private String description;
    @Column(name = "name")
    private String name;
    @Column(name = "version")
    private String version;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "updated")
    private Boolean updated;
    @Lob
    @Column(name = "gen_file")
    @Basic(fetch = FetchType.LAZY)
    private byte[] genFile;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "timestamp")
    private DateTime timestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getUpdated() {
        return updated;
    }

    public void setUpdated(Boolean updated) {
        this.updated = updated;
    }

    public byte[] getGenFile() {
        return genFile;
    }

    public void setGenFile(byte[] genFile) {
        this.genFile = genFile;
    }

//    public void update(EnsemblSQL updatedEnsembl) {
//        EntityManager _em = PersistenceManager.INSTANCE.getEntityManager();
//        EnsemblSQL ensembl = _em.find(EnsemblSQL.class, 1);
//
//        _em.getTransaction().begin();
//        employee.setNickname("Joe the Plumber");
//        _em.getTransaction().commit();
//        _em.close();
//    }
}
