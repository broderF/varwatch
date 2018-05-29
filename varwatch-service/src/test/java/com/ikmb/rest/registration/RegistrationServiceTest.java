/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.registration;

import com.google.gson.Gson;
import com.ikmb.core.data.auth.RegistrationResponse;
import com.ikmb.core.varwatchcommons.entities.Client;
import com.ikmb.rest.util.HTTPVarWatchInputConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author broder
 */
public class RegistrationServiceTest {

    RegistrationService registrationService = new RegistrationService();
    HTTPVarWatchInputConverter inputConverter = new HTTPVarWatchInputConverter();
    RegistrationManager registrationManager = mock(RegistrationManager.class);
    HttpServletRequest httpRequest = mock(HttpServletRequest.class);
    Response response;
    Client client = new Client();

    @Test
    public void shouldRegisterClient() throws IOException {
        client.setName("varwatch_client_name");
        client.setPassword("varwatch_client_pw");
        String input = new Gson().toJson(client);

        InputStream byteArrayInputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ServletInputStream servletInputStream = new ServletInputStream() {
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isReady() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setReadListener(ReadListener rl) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        when(httpRequest.getInputStream()).thenReturn(servletInputStream);
        when(registrationManager.saveClient(any())).thenReturn(RegistrationResponse.CLIENT_SAVED.getDescription());
        registrationService.registerClient(httpRequest);
    }

    @Before
    public void setup() {
        registrationService.setInputConverter(inputConverter);
        registrationService.setRegistrationManager(registrationManager);

    }

}
