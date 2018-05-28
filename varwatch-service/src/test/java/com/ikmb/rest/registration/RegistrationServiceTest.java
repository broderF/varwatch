/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.registration;

import com.ikmb.core.data.auth.RegistrationResponse;
import com.ikmb.core.varwatchcommons.entities.Client;
import com.ikmb.rest.util.HTTPVarWatchInputConverter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
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
    HttpServletRequest httpRequest;
    Response response;
    Client client = new Client();

    @Test
    public void shouldRegisterClient() {
        client.setName("varwatch_client_name");
        client.setPassword("varwatch_client_pw");
        when(registrationManager.saveClient(any())).thenReturn(RegistrationResponse.CLIENT_SAVED.getDescription());
        registrationService.registerClient(httpRequest);
    }
    
    @Before
    public void setup(){
        registrationService.setInputConverter(inputConverter);
        registrationService.setRegistrationManager(registrationManager);
        
    }

}
