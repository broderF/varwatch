/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.utils;

import com.ikmb.core.varwatchcommons.entities.RegistrationUser;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author bfredrich
 */
public class PdfCreator {

    public static String createPdfFromContact(RegistrationUser contact) {
        Random rand = new Random();
        String date = new DateTime().toString(DateTimeFormat.forPattern("yyyy_MM_dd"));
        String filename = "/tmp/varwatch_registration_" + contact.getFirstName() + "_" + contact.getLastName() + "_" + date + ".pdf";
        String message = getTextFromContact(contact);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            document.add(new Paragraph(message));
            Image img = Image.getInstance("/data/varwatch/VarWatchService/lib/varwatch_logo.png");
            img.scalePercent(40);
            document.add(img);
        } catch (DocumentException ex) {
            Logger.getLogger(PdfCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PdfCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        document.close();
        return filename;

    }

    public static void main(String args[]) {
        RegistrationUser user = new RegistrationUser();
        user.setFirstName("Broder");
        user.setLastName("Fredrich");
        user.setMail("broderfredrich@gmail.com");
        user.setInstitution("IKMB");
        user.setPhone("015771888686");
        user.setAddress("Methfesselstraße 34");
        user.setCity("Hamburg");
        user.setCountry("Deutschland");
        user.setPostalCode("20257");
        PdfCreator.createPdfFromContact(user);
    }

    private static String getTextFromContact(RegistrationUser contact) {
        String text = "";
        String firstName = "First name = " + contact.getFirstName();
        String lastName = "Last name = " + contact.getLastName();
        String mail = "Mail = " + contact.getMail();
        String institution = "Institution = " + contact.getInstitution();
        String phone = "Phone = " + contact.getPhone();
        String addressString = "Address = " + contact.getAddress();
        String city = "City = " + contact.getPostalCode() + " " + contact.getCity();
        String country = "Country = " + contact.getCountry();
        text += "Dear " + contact.getFirstName() + " " + contact.getLastName() + ",\n\n";
        text += "Welcome to VarWatch. You have registered at VarWatch with the following information\n\n";
        text += firstName + "\n";
        text += lastName + "\n";
        text += mail + "\n";
        text += institution + "\n";
        text += phone + "\n";
        text += addressString + "\n";
        text += city + "\n";
        text += country + "\n\n";
        
        text+= "Please correct if necessary."
                + "\n\nYour account will be activated once you have authenticated yourself. To this end, please sign this form and return to the address given below. You will be notified about the completion of the registration process by e-mail. "
                + "\n\nWith your signature, you also permit VarWatch to store and process your personal data for administrative purposes. Please note that your contact information will be shared with other users of our service in case of a variant match."; 

       
        text += "\n\n\nSignature: ....................................................................................\n";
        text += "Date / Signature"
                + "\n\nPlease return by post to\n\n";
        text += "VarWatch\n"
                + "c/o Institut für Medizinische Informatik und Statistik\n"
                + "Kiel University\n"
                + "Brunswiker Straße 10\n"
                + "24105 Kiel\n"
                + "Germany";
        text += "\n\nor scan and sent by e-mail to info@varwatch.de";
        return text;
    }

}
