/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.varwatchcommons.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class EnsemblHttpRequestHandler {

    private final String server = "http://rest.ensembl.org";
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(EnsemblHttpRequestHandler.class);

    public String sendHttpRequest(String parameter) {
        String output = null;
        HttpURLConnection httpConnection = null;

        try {
            String restCall = server;
            if (!parameter.startsWith("/")) {
                restCall += "/";
            }
            URL url = new URL(restCall + parameter);

            URLConnection connection = url.openConnection();
            httpConnection = (HttpURLConnection) connection;

            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("content-type", "application/json");

            InputStream response = httpConnection.getInputStream();
            int responseCode = httpConnection.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("Response code was not 200. Detected response was " + responseCode);
            }

            Reader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
                StringBuilder builder = new StringBuilder();
                char[] buffer = new char[8192];
                int read;
                while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
                    builder.append(buffer, 0, read);
                }
                output = builder.toString();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException logOrIgnore) {
                        logOrIgnore.printStackTrace();
                    }
                }
            }

        } catch (MalformedURLException ex) {
            logger.error("Error while connecting to ensembl endpoint", ex);
        } catch (IOException ex) {
            logger.error("Error while connecting to ensembl endpoint", ex);
        }
        return output;
    }
}
