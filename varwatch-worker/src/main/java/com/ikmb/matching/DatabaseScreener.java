/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching;

import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.matching.Match;
import com.ikmb.core.data.reference_db.RefDatabase;
import java.util.List;

/**
 *
 * @author Broder
 */
public interface DatabaseScreener {
    
    public void setVWDatabase(RefDatabase vwDatabase);

    public void run();

    public List<Match> getMatches();

    public void initialize(RefDatabase screenedDatabase, DatasetVW dataset);

    public DatasetVW getDataset();

    public RefDatabase getDatabase();
}
