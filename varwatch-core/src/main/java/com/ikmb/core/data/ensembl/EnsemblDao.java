/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.ensembl;

/**
 *
 * @author broder
 */
public interface EnsemblDao {

    public Ensembl getActiveEnsembl(Boolean needFile);
}
