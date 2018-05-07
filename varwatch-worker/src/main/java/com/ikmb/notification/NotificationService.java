/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.notification;

import com.google.common.collect.Multimap;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Broder
 */
public class NotificationService {

//    public static void sendVarWatchFoundMails(Container container, VariantAPI submitSQLVariant, List<VariantAPI> similarVariants) {
//        List<VariantAPI> submitAsList = new ArrayList<VariantAPI>();
//        submitAsList.add(submitSQLVariant);
//        String mailtext = parseVarWatchMailText(submitSQLVariant, similarVariants);
//        sendMail(container.getUser().getMail(), mailtext);
//        for (VariantAPI variant : similarVariants) {
//            VariantSQL varSQL = (VariantSQL) variant;
//            mailtext = parseVarWatchMailText(variant, submitAsList);
//            sendMail(varSQL.getDataset().getUser().getMail(), mailtext);
//        }
//    }
//
//    public static void sendExternalFoundMails(Container container, Multimap<VariantAPI, String> foundVariants) {
//        for (VariantAPI variant : foundVariants.keySet()) {
//            String mailtext = parseExternalMailText(variant, new ArrayList<String>(foundVariants.get(variant)));
//            sendMail(container.getUser().getMail(), mailtext);
//        }
//    }
    public static void sendMail(String email, String mailtext, String subject) {

        String host = "smtp.gmail.com";
        Integer port = 587;
        final String user = "varwatch.notifier@gmail.com";
        final String pw = "varwatchreport";

//        String subject = ;
//        String mailtext = parseMailText(submitSQLVariant, similarVariants);
//        String email = submitSQLVariant.getDataset().getUser().getEmail();
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pw);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(mailtext);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

//    private static String parseVarWatchMailText(VariantAPI submitSQLVariant, List<VariantAPI> similarVariants) {
//        String text = "Found similar Variant in VarWatch";
//        text += "\nSubmitted Variant: \n" + submitSQLVariant.getChromosomeName() + "," + submitSQLVariant.getChromosomePos() + "," + submitSQLVariant.getReferenceBase() + "," + submitSQLVariant.getAlternateBase();
//
//        text += "\nFound variants:";
//        for (VariantAPI varSQL : similarVariants) {
//            text += "\n" + varSQL.getChromosomeName() + "," + varSQL.getChromosomePos() + "," + varSQL.getReferenceBase() + "," + varSQL.getAlternateBase();
//        }
//
//        return text;
//    }
//
//    private static String parseExternalMailText(VariantAPI submitSQLVariant, List<String> externalDatabases) {
//        String text = "Found similar Variant in ExternalDB";
//        text += "\nVariant: " + submitSQLVariant.getChromosomeName() + "," + submitSQLVariant.getChromosomePos() + "," + submitSQLVariant.getReferenceBase() + "," + submitSQLVariant.getAlternateBase() + " in Databases: ";
//        for (String dbName : externalDatabases) {
//            text += dbName + " ";
//        }
//
//        return text;
//    }
    public static void sendEmail(VariantSQL _queryVariant, String email, VariantSQL _targetVariant, String _dbName) {
        String mailText = parseVariantMailText(_queryVariant, _targetVariant, _dbName);
        sendMail(email, mailText, "Found similar Variant in VarWatch");
    }

    private static String parseVariantMailText(VariantSQL _queryVariant, VariantSQL _targetVariant, String _dbName) {
        String text = "Dataset: " + _queryVariant.getDataset().getDescription() + " ; " + _queryVariant.getDataset().getId();
        text += "\nFound similar Variant in " + _dbName;
        text += "\nSubmitted Variant: " + _queryVariant.getChromosomeName() + "," + _queryVariant.getChromosomePos() + "," + _queryVariant.getReferenceBase() + "," + _queryVariant.getAlternateBase();
        if (_targetVariant != null) {
            text += "\nFound Variant: " + _targetVariant.getChromosomeName() + "," + _targetVariant.getChromosomePos() + "," + _targetVariant.getReferenceBase() + "," + _targetVariant.getAlternateBase();
        }
        return text;
    }

    public static void sendEmail(VariantSQL _queryVariant, String mail, String _dbName) {
        sendEmail(_queryVariant, mail, null, _dbName);
    }

    public static void sendEmail(NotificationInfoParser notparser) {
        for (NotificationSubmitter mail : notparser.getNotificationInfos().keySet()) {
            String mailtext = "Submitted: " + mail.getSubmittedVariant() + "\nMatched:\n";
            for (NotificationInfo notinfo : notparser.getNotificationInfos().get(mail)) {
                mailtext += notinfo.getMatch() + "\n";
            }

            sendMail(mail.getUser().getMail(), mailtext, "Found similar Variant in VarWatch");
        }
    }
}
