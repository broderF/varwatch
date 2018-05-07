/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.report;

import com.google.inject.Inject;
import com.ikmb.WorkFlowManager;
import com.ikmb.WorkFlowManager.JobProcessStatus;
import com.ikmb.utils.WorkerInputHandler;
import com.ikmb.varwatchcommons.entities.ExternalMatchInformation;
import com.ikmb.varwatchcommons.entities.InternalMatchInformation;
import com.ikmb.varwatchcommons.entities.MatchInformation;
import com.ikmb.varwatchcommons.notification.NotificationSubmitter;
import com.ikmb.varwatchcommons.utils.VariantHash;
import com.ikmb.varwatchsql.auth.user.UserManager;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.entities.AnalysisSQL;
import com.ikmb.varwatchsql.entities.AnalysisWorkerSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetManager;
import com.ikmb.varwatchsql.variant_data.variant.VariantDataManager;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.matching.MatchVariantDataManager;
import com.ikmb.varwatchsql.workflow.job.AnalysisJobSQL;
import com.ikmb.varwatchworker.Worker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bfredrich
 */
public class ReportWorker implements Worker {

    private final Logger logger = LoggerFactory.getLogger(ReportWorker.class);
    protected AnalysisWorkerSQL _workerSQL;
    protected AnalysisSQL _analysisSQL;
    protected AnalysisJobSQL _analysisJobSQL;

    private WorkFlowManager.JobProcessStatus jobProcessStatus = WorkFlowManager.JobProcessStatus.FAILED;

    @Inject
    private WorkerInputHandler workerInputHandler;
    @Inject
    private UserManager userManager;
    @Inject
    private MatchVariantDataManager matchDataManager;
    @Inject
    private VariantDataManager variantDm;
    @Inject
    private DatasetManager dsDm;

    @Override
    public void setWorkerSQL(AnalysisWorkerSQL workerSQL) {
        _workerSQL = workerSQL;
    }

    @Override
    public void setAnalysisSQL(AnalysisSQL analysisSQL) {
        _analysisSQL = analysisSQL;
    }

    @Override
    public void setAnalysisJobSQL(AnalysisJobSQL analysisJobSQL) {
        _analysisJobSQL = analysisJobSQL;
    }

    @Override
    public void runJob() {

        workerInputHandler.setJob(_analysisJobSQL);
        workerInputHandler.setAnalysis(_analysisSQL);
        workerInputHandler.parseInputData();

        Integer userId = workerInputHandler.getInteger("user_id");
        UserSQL user = userManager.getUserById(userId);
        logger.info("run report job for user {}" + user.getMail());
        String schedule = user.getReportSchedule();

        //check if schedule is !=never
        if (schedule == null || schedule.equals("never")) {
            logger.info("schedule is never, skip report creation");
            jobProcessStatus = JobProcessStatus.NO_EXECUTION_NECESSARY;
            return;
        }

        DateTime lastReport = user.getLastReport();

        if (lastReport == null) {
            lastReport = initReportDate(schedule);
            user.setLastReport(lastReport);
            userManager.update(user);
        }

        DateTime nextMinCreationDate = getNextMinReportDate(lastReport, schedule);
        if (nextMinCreationDate.isAfter(new DateTime())) {
            logger.info("creation date not reached, skip report creation");
            jobProcessStatus = JobProcessStatus.NO_EXECUTION_NECESSARY;
            return;
        }

        DateTime nextCreationDate = getNextReportDate(schedule);

        List<MatchInformation> matches = matchDataManager.getMatchesByInterval(user, lastReport, nextCreationDate);
        if (matches.isEmpty()) {
            user.setLastReport(nextCreationDate.plusMillis(1));
            userManager.update(user);
            jobProcessStatus = JobProcessStatus.NO_EXECUTION_NECESSARY;
            logger.info("no matches found from {} to {}", lastReport.toString(DateTimeFormat.fullDateTime()), nextCreationDate.toString(DateTimeFormat.fullDateTime()));
            return;
        }
        logger.info("{} new matches found", matches.size());
        user.setLastReport(nextCreationDate.plusMillis(1));
        userManager.update(user);
        String mailText = getMailTextNew(matches);

        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String title = "Dear " + firstName + " " + lastName + ",";
        String header = title + "\n\nVarWatch has found some matches in the last report period.\n\n";
        NotificationSubmitter.sendMail(user.getMail(), header + mailText, "VarWatch: Periodical Report");

        jobProcessStatus = JobProcessStatus.SUCCESSFUL;
    }

    @Override
    public void undo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public WorkFlowManager.JobProcessStatus getJobProcessStatus() {
        return jobProcessStatus;
    }

    private DateTime initReportDate(String schedule) {
        DateTime currentDate = new DateTime();
        DateTime reportTime = currentDate;
        if (schedule.equals("daily")) {
            reportTime = currentDate.withMillisOfDay(0);
        } else if (schedule.equals("weekly")) {
            reportTime = currentDate.withDayOfWeek(1).withMillisOfDay(0);
        } else if (schedule.equals("monthly")) {
            reportTime = currentDate.withDayOfMonth(1).withMillisOfDay(0);
        }
        return reportTime;
    }

    private DateTime getNextMinReportDate(DateTime lastReport, String schedule) {
        DateTime nextCreationDate = lastReport;
        if (schedule.equals("daily")) {
            nextCreationDate = nextCreationDate.plusDays(1);
        } else if (schedule.equals("weekly")) {
            nextCreationDate = nextCreationDate.plusWeeks(1);
        } else if (schedule.equals("monthly")) {
            nextCreationDate = nextCreationDate.plusMonths(1);
        }
        return nextCreationDate.minusMillis(1);
    }

    private DateTime getNextReportDate(String schedule) {
        DateTime currentDate = new DateTime();
        DateTime reportTime = currentDate;
        if (schedule.equals("daily")) {
            reportTime = currentDate.withMillisOfDay(0);
        } else if (schedule.equals("weekly")) {
            reportTime = currentDate.withDayOfWeek(1);
        } else if (schedule.equals("monthly")) {
            reportTime = currentDate.withDayOfMonth(1);
        }
        return reportTime.minusMillis(1);
    }

    private String getMailText(List<MatchInformation> matches) {
        String mailText = "Matched to VarWatch:\n";

        //internal matches
        Set<String> notifiedVariants = new HashSet<>();
        for (MatchInformation matchInfo : matches) {
            if (matchInfo instanceof InternalMatchInformation) {
                InternalMatchInformation internalInfo = (InternalMatchInformation) matchInfo;
                VariantSQL variant = variantDm.get(internalInfo.getQueryVariantId());
                String variantHash = VariantHash.getVariantHash(variant.getChromosomeName(), variant.getChromosomePos(), variant.getReferenceBase(), variant.getAlternateBase());
                if (notifiedVariants.contains(variantHash)) {
                    continue;
                } else {
                    String variantString = StringUtils.join(new Object[]{variant.getChromosomeName(), variant.getChromosomePos(), variant.getReferenceBase(), variant.getAlternateBase()}, "\t");
                    mailText += variantString + "\n";
                    notifiedVariants.add(variantHash);
                }
            }
        }
        mailText += "\n";
        mailText += "\n";
        //external matches
        mailText += "Matched to HGMD:\n";
        notifiedVariants = new HashSet<>();
        for (MatchInformation matchInfo : matches) {
            if (matchInfo instanceof ExternalMatchInformation) {
                ExternalMatchInformation internalInfo = (ExternalMatchInformation) matchInfo;
                VariantSQL variant = variantDm.get(internalInfo.getQueryVariantId());
                String variantHash = VariantHash.getVariantHash(variant.getChromosomeName(), variant.getChromosomePos(), variant.getReferenceBase(), variant.getAlternateBase());
                if (notifiedVariants.contains(variantHash)) {
                    continue;
                } else {
                    String variantString = StringUtils.join(new Object[]{variant.getChromosomeName(), variant.getChromosomePos(), variant.getReferenceBase(), variant.getAlternateBase()}, "\t");
                    mailText += variantString + "\n";
                    notifiedVariants.add(variantHash);
                }
            }
        }

        return mailText;
    }

    private String getMailTextNew(List<MatchInformation> matches) {
        //get all informations
        Map<Long, Set<Long>> datasetToVariants = new HashMap<>();
        Map<Long, List<MatchInformation>> variantToMatchInfo = new HashMap<>();
        for (MatchInformation matchInfo : matches) {

            VariantSQL variant = null;
            if (matchInfo instanceof InternalMatchInformation) { //vw match
                InternalMatchInformation internalInfo = (InternalMatchInformation) matchInfo;
                variant = variantDm.get(internalInfo.getQueryVariantId());
            } else if (matchInfo instanceof ExternalMatchInformation) { //hgmd match
                ExternalMatchInformation internalInfo = (ExternalMatchInformation) matchInfo;
                variant = variantDm.get(internalInfo.getQueryVariantId());
            }

            if (variant == null) {
                logger.error("Variant for matchInfo with Id {} is empty", matchInfo.getId());
                continue;
            }
            Long dsId = variant.getDataset().getId();
            Long variantId = variant.getId();

            if (datasetToVariants.containsKey(dsId)) {
                datasetToVariants.get(dsId).add(variantId);
            } else {
                datasetToVariants.put(dsId, new HashSet<Long>());
                datasetToVariants.get(dsId).add(variantId);
            }

            if (variantToMatchInfo.containsKey(variantId)) {
                variantToMatchInfo.get(variantId).add(matchInfo);
            } else {
                variantToMatchInfo.put(variantId, new ArrayList<MatchInformation>());
                variantToMatchInfo.get(variantId).add(matchInfo);
            }
        }

        String mailText = "";

        for (Long dsId : datasetToVariants.keySet()) {
            DatasetVWSQL dataset = dsDm.getDatasetByID(dsId);
            String link = "Link to dataset: https://varwatch.de/datasets/" + dataset.getId() + "\n";
            String description = "Description: " + dataset.getDescription() + "\n";

            int nrMatches = 0;
            for (Long variantId : datasetToVariants.get(dsId)) {
                nrMatches += variantToMatchInfo.get(variantId).size();
            }
            String nrMatchesString = "Nr of matches: " + nrMatches + "\n";
            mailText += link + description + nrMatchesString;
        }
        String linkDoesNotWork = "\n\nIf the links don't work in your mail client, please copy/paste it into your browser.";

        String vwTeam = "\n\nWith kind regards,\n"
                + "Your VarWatch Team";
        return mailText + linkDoesNotWork + vwTeam;
    }

}
