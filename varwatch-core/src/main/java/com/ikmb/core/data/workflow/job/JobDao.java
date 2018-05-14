/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.workflow.job;

import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.workflow.analysis.Analysis;
import java.util.List;

/**
 *
 * @author broder
 */
public interface JobDao {

    public void save(AnalysisJob job);

    public List<Long> getIDsOfAvailableJobs();

    public AnalysisJob getJobByID(Long id);

    public void update(AnalysisJob job);

    public List<AnalysisJob> getJobByModuleAndDataset(DatasetVW dataset, Analysis analysisSQL);

    public int getNofAvailableJobs();

    List<AnalysisJob> getJobByModuleAndStatus(DatasetVW dataset, String claimed, Analysis analysisSQL);

    List<AnalysisJob> getJobByStatus(String status);
}
