/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.reference_db;

import java.util.List;

/**
 *
 * @author broder
 */
public interface ReferenceDBDao {

    public RefDatabase get(Long id);

    public List<RefDatabase> getActiveDatabases();

    public RefDatabase getVarWatchDatabase();

    public void save(RefDatabase refDbSql);
}
