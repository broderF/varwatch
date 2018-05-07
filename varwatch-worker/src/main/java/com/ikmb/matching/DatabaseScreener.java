/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching;

import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.data.reference_db.RefDatabaseSQL;
import com.ikmb.varwatchsql.matching.MatchSQL;
import java.util.List;

/**
 *
 * @author Broder
 */
public interface DatabaseScreener {
    
    public void setVWDatabase(RefDatabaseSQL vwDatabase);

    public void run();

    public List<MatchSQL> getMatches();

    public void initialize(RefDatabaseSQL screenedDatabase, DatasetVWSQL dataset);

    public DatasetVWSQL getDataset();

    public RefDatabaseSQL getDatabase();
}
