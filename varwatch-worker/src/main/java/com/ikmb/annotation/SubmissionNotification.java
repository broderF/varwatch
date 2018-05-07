/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.annotation;

import com.ikmb.varwatchcommons.notification.NotificationSubmitter;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bfredrich
 */
public class SubmissionNotification {

    private final Logger logger = LoggerFactory.getLogger(SubmissionNotification.class);

    public void notifySubmission(DatasetVWSQL dataset) {
        UserSQL user = dataset.getUser();
        String mail = user.getMail();
        if (mail.equals("notifier")) {
            return;
        }
        logger.info("Send submission report to user: " + mail);

        String variants = "";
        for (VariantSQL variant : dataset.getVariants()) {
            variants += variant.toDbString() + "\n";
        }

        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String header = "Dear " + firstName + " " + lastName + ",";
        String title = header + "\n\nthank you for submitting the following variants to VarWatch:\n\n";

        String varheader = "VarWatch Identifier,Chromosome,Position,Reference,Alternate\n";
        String vwTeam = "\n\nWith kind regards,\n"
                + "Your VarWatch Team";
        String mailText = title + varheader + variants + vwTeam;

        NotificationSubmitter.sendMail(mail, mailText, "VarWatch Submission Report");
    }

}
