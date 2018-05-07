/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.family;

import com.ikmb.varwatchcommons.entities.Family;
import com.ikmb.varwatchsql.entities.FamilySQL;

/**
 *
 * @author broder
 */
public class FamilyBuilder {

    private String identifier;
    private String description;
    
    public FamilyBuilder withFamilySql(FamilySQL family) {
        identifier = family.getName();
        this.description = family.getDescription();
        return this;
    }

    public Family build() {
        Family family = new Family();
        family.setIdentifier(identifier);
        family.setDescription(description);
        return family;
    }
}
