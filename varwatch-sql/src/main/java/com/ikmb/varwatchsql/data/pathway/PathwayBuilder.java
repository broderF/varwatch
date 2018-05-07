/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.pathway;

import com.ikmb.varwatchcommons.entities.Pathway;
import com.ikmb.varwatchsql.entities.PathwaySQL;

/**
 *
 * @author broder
 */
public class PathwayBuilder {

    private String identifier;

    public PathwayBuilder withPathwaySql(PathwaySQL pathway) {
        identifier = pathway.getName();
        return this;
    }

    public Pathway build() {
        Pathway pathway = new Pathway();
        pathway.setIdentifier(identifier);
        return pathway;
    }
}
