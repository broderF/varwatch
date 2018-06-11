/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.utils;

import com.google.inject.Inject;
import com.ikmb.core.data.config.ConfigurationManager;
import com.ikmb.core.data.reference_db.RefDatabase;
import com.ikmb.core.data.reference_db.ReferenceDBDataManager;
import com.ikmb.core.varwatchcommons.entities.RegistrationUser;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private Image varwatchImage = null;

    @Inject
    public PdfCreator(ReferenceDBDataManager dbManager) {
        RefDatabase varWatchDatabase = dbManager.getVarWatchDatabase();
        try {
            byte[] byteImage = Files.readAllBytes(Paths.get(varWatchDatabase.getImagePath()));
            varwatchImage = Image.getInstance(byteImage);
        } catch (BadElementException ex) {
            Logger.getLogger(PdfCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String createPdfFromContact(RegistrationUser contact) {
        Random rand = new Random();
        String date = new DateTime().toString(DateTimeFormat.forPattern("yyyy_MM_dd"));
        String filename = "/tmp/varwatch_registration_" + contact.getFirstName() + "_" + contact.getLastName() + "_" + date + ".pdf";
        String message = getTextFromContact(contact);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            document.add(new Paragraph(message));
//            Image img = Image.getInstance("/data/varwatch/VarWatchService/lib/varwatch_logo.png");
            varwatchImage.scalePercent(40);
            document.add(varwatchImage);
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

    public static void main(String args[]) throws BadElementException, IOException {
        Image.getInstance("aVZCT1J3MEtHZ29BQUFBTlNVaEVVZ0FBQUZrQUFBQmNDQVlBQUFBRnhoa2NBQUFBQkhOQ1NWUUlDQWdJZkFoa2lBQUFBQWx3U0ZsekFBQU82d0FBRHVzQmNjMkJsUUFBQUJsMFJWaDBVMjltZEhkaGNtVUFkM2QzTG1sdWEzTmpZWEJsTG05eVo1dnVQQm9BQUJBK1NVUkJWSGljN1oxNVdKVDF2c0EvTSt5YkZobjdjN3FYanBrWFMwSE5yVkoyVU1pVFppWm10d1ZCRXJYTlUybUwxYW5zTWJxVnBjY2VzL3Uwbld2SFJKWktBOU1veEkzRkZTVkVEWndCVFFlQllXYVlZZDc3eHh1RENBeXp2QVA0MU9jdmVaZmY3L3QrZVBudHYxZlp5K3RLaE5jMjdHY3dJNVBCMnVlbmtYSC83UU1kaWkxVU9lM08zN2pLYUJRb0tqMDMwTUgwaUV3Rzd6ODNqY2ZuWHBPQ0FTN0tBVjU1ZkNJckY0NGY2R0M2SVpQQmU4OU92WllGQXlEditNZXJpeWV4SW5Yd2lKYko0TjIvVDJYeEE2TUhPaFM3a1YvNXcydVprM2p1c1hFREZVc1gzbHcyaGN4NTE3NWd1RW95d090TEp2UHNvd01yK28ybGsxbis4TmdCalVGS3Vra0c4U0gvL3NqQVBPUmcrQ1ZMVFkrU1FmeHo3ZSszNlIrRHFMaVNrbDRsUS8rV2k2OWxUdUw1UVZUeFNvbFp5UjAxdktPYlVJT3RaU00xWmlWRFoyZkFVYjJ0VlJtRHM0MHVKWDFLQnNkMWExOWVOSUVYMCsrUU5NM0JpRVdTb1ZOMCtwemJKTWw0K2NOamVXblJCRW5TR3V4WUxCbEUwUit1aUNUdHZsRjJaZnJNZjBldytva3BkcVZ4TFdHVlpCQkZyMXNaeGNMWnRvbCs2cUVJM25yeVRwdnV2VmF4V2pKMGlJNWtRZkpJcSs1N2NrRTRhNTc2WXdrR0d5VUR5T1V5UG40bGhnZVRiclhvK2ljZURPZnRwKyt5TmJ0ckdwc2xBempKWld4Nk5aYjVNOHlMWGpaL0RGblAvREVGZzUyU1FSVDl5V3V4cEV3ZjBlUDVwZlBIOE03eXUrM041cHJHYnNrZ2l2N2ZmOFR4UU9JdFhZNHZuRDJLZDU3NVl3c0dpU1NES1ByVDErT1pteUNLVHAwMWl2VXZSQ0dUU1pYRHRZdE0xYVFWcnZOeGt5eEJ2Y0hJcDdtVlBIcHZtS1NDRzV0MVNCbG5QMUlsajAzTFJ0V2treXhGRjJjNWo4MlNWbkREeFZibUx2OU91Z1Q3R1hsWjVYbVNNbk5vVXJjTmRDdzkwbkN4bFppRld6bHgrdEpBaDJJemNvQzloK3VabmpINFJKKy9wQ0UyTFp2ak5kZXVZTGlpNGlzNXJHVEc0emswRHhMUjV5OXBpRm00bFdPbkxwcTlic1dLRmR4ODg4MnNYYnZXZEt5cHFZa1JJMFl3YXRRb05CcU5vMFB0a3k2dGl6MkhsTXhZUFBDaUw2ZzB4S2IxTFJnZ01qS1NtcG9hTm0zYVpEcVdrNU5EVlZVVllXRmhlSGg0QUtEUmFGQW9GTFMydGpvczd0N28xb1FycmhCRnQ3VHErejBZNkJSOHRMcHZ3UUJSVVZFRUJRVlJVVkhCeVpNbkFkaThlVE1BRHo3NElBQno1c3pCMDlPVDRPQmd2THk4bUR4NU1ncUZBb0RVMUZSOGZYMTU4ODAzR1RObURJc1hMNWI4bVhwc0p3K1VhRldUanVtUDUzRGtGOHNFQXpnNU9URnYzanhBbEh2cDBpVUtDZ29ZTm13WUNRa0pBTVRGeFpHYm0wdFJVUkZwYVdtVWxKVHc0WWNmQXFCV3ExR3BWR1JsWmVIczdNeXdZY01rZnk3bjNrNzhYSzRnS1RPSC9BOW00dTNwSW5uR1Y5UFlyQ04rVVRabGxlZXR2bmZCZ2dWa1pXV3hlZk5tZ29PRGFXdHJZKzdjdWJpNGlIR1BIeitlZGV2V1VWcGFTbDFkSFFEbnozZk5KeWNuaHlsVEhEUEdiYmJIOTFPWmd1UWx1YWcxam4yakc1dDF4S2RuVTNyY2VzRUFvMGVQNXJiYmJ1UDQ4ZU9zWHIwYTZDd3FGQW9GVTZaTUlUYzNseVZMbHJCeTVVcko0cmFVUHJ2VlJhWG5tUFhrTjJoMEJvY0UwTmlzSTJIUk5nN2FLTGlERHFuVjFkVU1IejZjQ1JQRXFhMk95aTQ0T0ppSWlBanE2K3Z0anRsYUxCcTdLTno3Sy9jK2tZOVcxeTVwNXBkYjJrak0yTWFCWXcxMnA1V1Nrb0pjTGo3Ty9QbnprZjNlNVF3UER5YzVPWm15c2pMR2p4OVBSVVdGM1hsWmkwdysrajNCMG90akovMkZiZThtNCs3bVpIZkdsMXZhU0ZpVXpmNmpsZ2tPOGZmbTdJNUhiYzZ2dnI0ZUh4OGZ2THk4YkU3RFJxcXNHb1VyS1BtVldVL1ovMFkzcWNVMzJGTEJVaEFRRURBUWdnRWJoanAzRko5bDFsUDU2TnBzRTYzVzZKbTVOSTk5UjZRcEd6VWFEU3FWcWt2UHJxbXBDWlZLUlh1N0dHTjdlenNxbFlxbXBpWko4dXlncEtTRWp6NzZpTk9uVDV1OXpxYnhaRnRGcXpWNmtqSnpKZDA2OGRsbm4rSHI2OHRERHowRWdGNnZKeVFrQkY5Zlg3S3pzd0d4ZWVicjYwdEtTa3FmNmExYXRZcHg0OFp4OE9EQlBxL2RzbVVMNmVucGxKYVdtcjNPNWtINzdUK2ZaZlpUMzFnc1dxM1JrN3drVC9LOUtURXhNWUQ0VmdIczI3ZVA1dVptQUhidjNnMUFjWEZ4bDJ0VktoVjc5KzZsc0xDUS9mdjNvOWVMVGRTbXBpYU9IRGxDYVdrcFZWVlYxTlRVSUFoaWxhWFJhRGh3NEFCRlJVVmN1SENoV3h5MXRiVVVGUlhSMHRMUzdWeXZuUkZMK083bk04eC9manYvZWlzUkYrZmVmMSt0V2dQSlMvTDQ4V0NkUGRuMVNHaG9LS0dob2RUVTFGQmJXOHZPblRzQmNITnpZOWV1WFVCM3lYZmZmVGRIang0MXBSRVNFa0pSVVJFN2R1eGc2OWF0Z05oQ0FXaHJhMlBuenAwODhzZ2pwdWFmczdNejVlWGxwdnZYckZuRHdZTUhNUnFOaElhR2N1alFJYnk5dlUzbjdaNSt5dDU1aW5uUGZvZmVZT3p4dkNnNDF5R0NPN2p5YlM0c0xNVGYzNS9aczJkVFdWbko2ZE9uS1Nzckl6QXdrTEN3TUFEZWZ2dHRhbXRydVhqeEloa1pHZFRWMWJGNTgyWlNVbEtZTldzV0FGOTg4UVduVHAxQ3JWWXpiOTQ4R2hzYitmYmJiNm10cmVXbGwxN0N5YW16aFRWeTVFank4L01KQ3d1anBxYUdvcUtpTHZGSk1zZVh2Zk1VODUvZmpxRzlxK2hXcllGN2x1YXkrNERqQkVPbjVJS0NBdmJ0MjBkVVZCVHg4ZkVJZ3NDYU5XdlE2L1ZFUjBlYjJzNTFkWFhNbkRrVGYzOS8xcTlmRDRoRnhaQWhRL0R4OFFFZ01EQ1EwTkJReXN2TGFXeHNKREl5a3NURVJFSkNRbmp4eFJjWk9iSnpZVTlTVWhLSmlZbEVSMGNEME5qWTJDVSt5U1pTdnk2b0p1VzVUdEVhbllHWnkvTFl0ZCt4Z2tFY2laUEw1WHorK2VmbzlYcGlZbUtJaVlsQkpwUHh5U2VmQUoyL2lEMTc5cENhbW9wY0xxZTB0TlIwL21xTVJ2RTUvUHo4Z0s3aURBWURPbDMzS1R0WFY5Y2UwNUpNTW9paTV6KzNnK2JXTm1ZdXplT0hmYlZTSnQ4ck45eHdBK0hoNFdpMVdnQ2lvNk1KQ2dwaTFLaFJwbU1ka2p0K05oZ01IRHQyaksrLy9ycExXamZlZUNNQTY5ZXZKeXNyaStIRGh4TVJFVUZKU1FscGFXbTgvLzc3VEowNmxTTkhqbGdjbi9QM0crNjEreUd2NW95aWlXY2ZIU2ZwQmh0WEYvTzl6STdtV1VCQUFEZmRkQk1nam1kODlkVlhCQWNIRXh3Y0RJaUQvQmtaR1d6WnNvWFZxMWNURnhlSFVxa2tLQ2dJZ016TVRDb3FLaWd1THFheXNwSmx5NWFSbDVmSEN5KzhRR0ZoSWR1MmJTTXNMSXlBZ0FCQ1FrSVlPM1lzMTE5L1BRREJ3Y0dNSFRzV1gxL2ZMckhKaEk0MnlwODRDdXU2MVg5aUczOUs3Z2YrbE53UE9HdExEaUdvQjM3YXZDL2t2a054aStoc20ycUxLekFvN0J2bzd3L2tIbTZlenJRYnFiLzNTWXd0L1Q5VmJpbnk2NGNRVkxDaHl6RW5mMThhNWk3SGNHN3dpcFlQOVNZd2I2MVI3bjVuT0lIZmZvamN5Mk9nWStvUitYVStCSDMvVDl6Ry9sZVg0eTUvL1F0QnV6N0dPZGh2Z0NJemozeW9ONEU3L29uN1hSRmFPWUQ3WFJFRWJIc1htY2ZnV2pVcHY4NkhvSUlOdUkwTDYvRzh5L0RmUlFmZDJNK1JtVWMreEl2QTdldHhueUJ1eHpOVmZCNHhFd25NZVErWisrQVFMUi9xVGREM3ZRdnVZTENKbG50NUVKQzNGdmVKblJ0THU3UXVQR0luRVRBSVJKc0VqemN2dUFPWFcyNGk2SWVOT0FWS3Z6REZHdVJlSGdSODh3RWVkM2Y5dWtLM0pweG4zQ1N4NkJnZzBSMWxtZHNkMXUwVGRCbnhId1R2K2hpbmdJRVJMZk4wSnlEL0F6eW1kaDlLNkxHZDdCay9tWURzLytsMzBmSWhYbUpsTWNHMnJjV2k2STM5TGxybTZVNWcvZ2Q0VE90NXJLYlh6b2hud2hRQ3RyNkR6SzNuNFR1cE1aVmxOZ3J1d09YVy95Um94M3FjaGwwblVXVG1rWG02RTVpM0ZvL0kzcjkwWUxiSDU1bDRwL2hHTzFoMGIyV1pyYmplZmd0QmhSODVYTFRNdzQzQTNQZnhpREwvcFlNK3U5V2VpWGM2OUkwMlY1YlpnK3ZvRWFMb0d4d2oyaVE0dXU4dkhWZzBkdUU1L1M3OHYxeU56TVd1ZWRkdTlGV1cyWXZyNkJFRU9rQzB6TTJWZ0g5bjRSRXowYUxyTFI0Zzhwb1ZqZisvM3BKTXRFbXdtYkpNQ3R6R2pDQ3dZQU55MzZHU3BDZHpkU0ZnU3hhZU15emZ4bXpWS0p6WDdCajh2bHlOek5tK3RYQ1dWQlpTNGhaK0swR0ZIOWt0V3VicWd2K1dMRHlUck50bGEvVlFwL2Q5c1hhSnRyU3lrQnEzOEZzSkt0aUEvUG9oTnQwdmMzWEIvOTl2NDVVODFlcDdiUnBQOXA0VGg5OFgxb3VXdWJrU3NDWExvc3JDRWJoRmpCUUh3NFpZdC9EUUpQaWVhVGJsYS9PZ3ZmZjljZmg5L3FiRm9tV3VMZ1I4L1E2ZTB3ZjJrd3p1RTI4bmNQdDY1RDZXaVphNU9PUC8xUnFiQllPZE15UGVjK1B4Kyt3TmNES2ZqRW13RlpXRkkzR2ZOSnJBN2V2NkZHMFNQRFBTcnZ6c25uN3lmaUFCdjQyclFONXpVclpXRm83R2ZmSVlBcjh6STlwSmp0K25yK1AxdHlpNzg1Smtqcy9uNFpuNGJYeTVtMmg3S292K3dIM0tHTEdNOXZic2VzSkpqdjluYitEOVFJSTBHUWtTMHZSeHRsQXRIeU5VYzd0d3luV3MwTEx0QnltVGR4aWFuOHFFR3UrSlFqVzNDOVZPWTRUbUw3NlJNdm1Ua2tvV0JFRzR2SEdyY01wdG5OQ1N2VlBxcEIyS3BxaFVxQmt5U1dqK1BGL3FwRStpM1g5VTZrUUYvUm1GNUdsZUhXZnJyZ09TNTJHNG9KSThUZlYzeGJYVURKMHNhUGNka1R4eEtWSHYyQ09jL1d0U2wyTm5ib29YbXIvOGRvQWlzZ3pOeitWQ2plY2Q3WExqNVJZVThZdlFIVGdtVFNFdk1acUNFdXIvdGd4QmU5VlMxWFlqRFF0VzBQSi8yd2Ntc0Q3UUZsZWdUSHdjWTZ0V2JBNFlHNXRSeEtXak96aTRSR3VLU3FtLzkwa0VUUytmNzJrM2N2NmhsYWkzL2RDL2dmV0Jkcy92Z3B2VndCVk5PR05qTTRyWWRIU2x4d2NzdUN2Ui9sUkcvWXhNakgyc2JoTDBCaHJ1WDQ0NloxYy9SV1llYmNraGxBbWRndUdxZHJKSmRGbGx2d2QzSmRxZnkxRk9YMnp4cWlhVDZOemRqZzJzRDdSN0Q2Tk15T2dpR0hyb2pCaFZUU2dUTW1nNzhrdS9CWGNscHJMTXltVmpRcHVlaGpuUG9NNzcwVUdSbVVkWFZpbStHRTNxYnVkNjdQRzFYMUNoaUY1STI5RnFod2QzSmFheXpNWjFlVUtibm9iN251NTMwYnJ5RXloaTB6R3FldDd4Mm11MzJpVDYyQ21IQlhjbDJqMFYzY295VytnUTNacGYxUGZGRXFBclA0RWlKZzNqcGN1OVhtTjI3S0w5L0NVVVVhbTBIYStSUExncjBaWWM2bEliMjR2UXBxZit2cWRwL2VZblNkTHJEVjNGU1pTeDZXWUZnd1VEUkk0V3JTczlqbkpHWm85bG1UMEl1amJxNXp5TnBuQ3ZwT2wyMEhib0pNcVlOTm92TnZaNXJVV2pjTzBORjBYUmxkS0sxcFZWbWkzTDdFWFE2RkRlc3hUTkQ5TCtwNDV0aDZ0UVdDZ1lyQmpxRkVVdlJIL0MvR2NITEtXdnlrSXFCSTBPWmZJU3lVUzNIYTVDRWIyUTl0OHNFd3hXamllMzEvK0dJallkL1NuN05rSHFLazcyV1ZsSWlkQ3FGVVh2T21CWE92b1RwMUhFWjFnbEdHd1l0RGZVTmFDSVRMVlp0SzVDTE12NlMzQUhRcXNXWlZJbW10MTlmOGVpSi9RbnozQXVNcFgyK3Qrc3Z0ZW1tUkZEYmIwb3VzYTZmZFBXVkJhT1FHalZVcCtVaWVaSDYwU0xnaCt6U1REWU1mMWtFbjNhc28rRVdGdFpPQXFqV2tQOWpFdzBSZWEvdHRLQnZ1b3NpcWhVMnBXMkNRWTc1L2dNdnlwUlRIdXNUOUg2RTZkUnhDMnl1aXh6RkVhMWh2cmtKV2ozSGpaN25mNlhYMUZFUG9aQjBmMUxMZFpnOTBTcTRWZWxHTWdaUlkvbjlTZE9pMlZaZytYZjMrd1BqRTFxbEFrWmFQZjF2TnRmS3NFZzBXeTE0YXlTYzlNZXhYQlcyZVc0L3VRWnprWFpWbG4wQjhiTExTampGNkhiZjdUTGNYMzE3NElsMmlNbzJiWmZ3MWtsaXRnMFUyQ215c0tPc3F3L01GNXVFU2NzZnA4WkVwOGpYZEpObUpJdU9PNzRFL1BiOUNvTmM1YzdWTEI4cURkeUNiY3JOOHg3RnI5TnIzQmg2VnNZTDdmWXZEQ3hHKzNHOXY4SDZiVEFUSTNjNENnQUFBQUFTVVZPUks1Q1lJST0=");
//        RegistrationUser user = new RegistrationUser();
//        user.setFirstName("Broder");
//        user.setLastName("Fredrich");
//        user.setMail("broderfredrich@gmail.com");
//        user.setInstitution("IKMB");
//        user.setPhone("015771888686");
//        user.setAddress("Methfesselstraße 34");
//        user.setCity("Hamburg");
//        user.setCountry("Deutschland");
//        user.setPostalCode("20257");
//        PdfCreator.createPdfFromContact(user);
    }

    private String getTextFromContact(RegistrationUser contact) {
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

        text += "Please correct if necessary."
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
