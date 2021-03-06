/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.notification;

import com.google.inject.Inject;
import com.ikmb.core.data.config.ConfigurationManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bfredrich
 */
public class EmailNotifier {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotifier.class);

    public static String host;
    public static String port;
    public static String user;
    public static String pw;

    @Inject
    public EmailNotifier(ConfigurationManager configManager) {
        host = configManager.getConfiguration("email_host");
        port = configManager.getConfiguration("email_port");
        user = configManager.getConfiguration("email_user");
        pw = configManager.getConfiguration("email_pw");
    }

    public void sendMail(String email, String mailtext, String subject) {
        if (host == null || port == null || user == null || pw == null) {
            return;
        }
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.transport.protocol", "smtp");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pw);
            }
        });

        try {

            Transport transport = session.getTransport();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(mailtext);
            transport.connect();
            transport.sendMessage(message, InternetAddress.parse(email));

            System.out.println("Done");

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    public void sendMail(String email, String mailtext, String subject, String filePath) {
        if (host == null || port == null || user == null || pw == null) {
            return;
        }
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.transport.protocol", "smtp");

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

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(mailtext);

            ByteArrayDataSource source = new ByteArrayDataSource(new FileInputStream(filePath), "application/pdf");
//            DataSource source = new FileDataSource(filePath);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
//            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("VarWatch_Registration.pdf");
            Multipart multipart = new MimeMultipart("mixed");
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            System.out.println(e);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(EmailNotifier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMail(Set<String> emails, String mailtext, String subject) {
        sendMail(StringUtils.join(emails.toArray(), ","), mailtext, subject);
//        String host = "smtp.gmail.com";
//        Integer port = 587;
//        final String user = "varwatch.notifier@gmail.com";
//        final String pw = "varwatchreport";

//        String mailtext = parseMailText(submitSQLVariant, similarVariants);
//        String email = submitSQLVariant.getDataset().getUser().getEmail();
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.socketFactory.port", "465");
//        props.put("mail.smtp.socketFactory.class",
//                "javax.net.ssl.SSLSocketFactory");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.port", "465");
//
//        Session session = Session.getInstance(props,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(user, pw);
//                    }
//                });
//
//        try {
//
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(user));
//            String emailList = StringUtils.join(emails.toArray(), ",");
//            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse(emailList));
//            message.setSubject(subject);
//            message.setText(mailtext);
//
//            Transport.send(message);
//
//            System.out.println("Done");
//
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
    }
}
