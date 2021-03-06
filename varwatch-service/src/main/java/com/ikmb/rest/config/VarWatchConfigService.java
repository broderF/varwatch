/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.config;

import com.google.inject.Inject;
import com.ikmb.core.data.config.ConfigurationManager;
import com.ikmb.core.data.config.FilterConfig;
import com.ikmb.core.data.config.VarWatchConfig;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.reference_db.RefDatabase;
import com.ikmb.core.data.reference_db.ReferenceDBDataManager;
import com.ikmb.core.data.workflow.analysis.AnalysisBuilder;
import com.ikmb.core.data.workflow.job.AnalysisJob;
import com.ikmb.core.data.workflow.job.JobManager;
import com.ikmb.rest.util.AdminRequestFilter.AdminFilter;
import com.ikmb.rest.util.ResponseBuilder;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author broder
 */
@AdminFilter
@Path("config")
public class VarWatchConfigService {

    @Inject
    private ConfigurationManager configManager;
    @Inject
    private ReferenceDBDataManager databaseManager;
    @Inject
    private DatasetManager dsManager;
    @Inject
    private JobManager jobManager;

    @GET
    @Path("show")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfiguration() {
        List<VarWatchConfig> configurations = configManager.getConfigurations();
        return new ResponseBuilder().buildListWithNulls(configurations);
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addConfiguration(@QueryParam("key") String key, @QueryParam("value") String value) {
        configManager.addConfiguration(key, value);
        List<VarWatchConfig> configurations = configManager.getConfigurations();
        return new ResponseBuilder().buildList(configurations);
    }

    @GET
    @Path("filter/show")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilterConfiguration() {
        List<FilterConfig> filterOptions = configManager.getFilterOptions();
        return new ResponseBuilder().buildList(filterOptions);
    }

    @POST
    @Path("filter/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addFilterConfiguration(@QueryParam("key") String key, @QueryParam("value") String value, @QueryParam("type") String type, @QueryParam("enabled") boolean enabled) {
        configManager.addFilterConfiguration(key, value, type, enabled);
        List<FilterConfig> filterOptions = configManager.getFilterOptions();
        return new ResponseBuilder().buildList(filterOptions);
    }

    @GET
    @Path("beacon/show")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBeaconConfiguration() {
        List<RefDatabase> activeBeacons = databaseManager.getActiveBeacons();
        return new ResponseBuilder().buildListWithExpose(activeBeacons);
    }

    @POST
    @Path("beacon/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addBeaconConfiguration(@QueryParam("name") String name, @QueryParam("path") String path, @QueryParam("assembly") String assembly, @QueryParam("image_url") String imageUrl, @QueryParam("enabled") Boolean enabled) {
        String imagePath = "images/clinvar_logo.png";
        try {
            saveImage(imageUrl, imagePath);
        } catch (IOException ex) {
            Logger.getLogger(VarWatchConfigService.class.getName()).log(Level.SEVERE, null, ex);
        }
        RefDatabase referenceDB = databaseManager.saveBeacon(name, path, assembly, imagePath, enabled);
        for (Long id : dsManager.getAllDatasetIds()) {
            jobManager.createJob(id, AnalysisBuilder.ModuleName.SCREENING_BEACON, referenceDB.getId().toString(), AnalysisJob.JobAction.UPDATE.toString());
            jobManager.createJob(id, AnalysisBuilder.ModuleName.SCREEN_BEACON_RESULT_COLLECT, null, AnalysisJob.JobAction.UPDATE.toString());
        }
        List<RefDatabase> activeBeacons = databaseManager.getActiveBeacons();
        return new ResponseBuilder().buildListWithExpose(activeBeacons);
    }

    public static byte[] recoverImageFromUrl(String urlText) {
        URL url;
        try {
            url = new URL(urlText);
        } catch (MalformedURLException ex) {
            Logger.getLogger(VarWatchConfigService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte[] buffer = new byte[1024];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } catch (IOException ex) {
            Logger.getLogger(VarWatchConfigService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return output.toByteArray();
    }

    TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                //No need to implement.
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                //No need to implement.
            }
        }
    };

    public static void main(String[] args) throws Exception {
        VarWatchConfigService cur = new VarWatchConfigService();
        cur.run();
    }

    public void run() throws IOException {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println(e);
        }
        String imageUrl = "https://varwatch.de/assets/images/logo-877471f9f124d5f5d79a250ef2650efa.png";
        String destinationFile = "image.jpg";

        saveImage(imageUrl, destinationFile);
    }

    public void saveImage(String imageUrl, String destinationFile) throws IOException {

        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

}
