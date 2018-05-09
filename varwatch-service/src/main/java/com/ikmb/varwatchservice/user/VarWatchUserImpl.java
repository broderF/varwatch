package com.ikmb.varwatchservice.user;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.inject.Inject;
import com.ikmb.core.auth.user.User;
import com.ikmb.core.auth.user.UserManager;
import com.ikmb.core.varwatchcommons.entities.DefaultUser;
import com.ikmb.core.varwatchcommons.entities.VWResponse;
import com.ikmb.varwatchservice.HTTPTokenValidator;
import com.ikmb.varwatchservice.ResponseBuilder;
import com.ikmb.varwatchservice.VarWatchInputConverter;
import com.ikmb.varwatchservice.registration.RegistrationManager;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bfredrich
 */
@Path("user")
public class VarWatchUserImpl implements VarWatchUser {

    private Integer expiresIn = 3600;

    private static final Logger logger = LoggerFactory.getLogger(VarWatchUserImpl.class);

    @Inject
    private VarWatchInputConverter inputConverter;
    @Inject
    private RegistrationManager registrationManager;
    @Inject
    private HTTPTokenValidator tokenValidator;
    @Inject
    private UserManager userManager;

    @Override
    public Response setUserReportSchedule(String header, String reportSchedule) {

        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.isTokenValid();
        if (!tokenValid) {
            VWResponse response = new VWResponse();
            response.setMessage("Error");
            response.setDescription("Token is not valid");
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
        }

        User user = tokenValidator.getUser();
        try {
            ReportSchedule.valueOf(reportSchedule);
        } catch (IllegalArgumentException ex) {
            VWResponse response = new VWResponse();
            response.setMessage("Error");
            response.setDescription("Report Schedule (" + reportSchedule + ") is not valid");
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();

        }
        DateTime startDate = initReportDate(reportSchedule);
        user.setLastReport(startDate);
        user.setReportSchedule(reportSchedule);
        userManager.update(user);
        return new ResponseBuilder().withVwSuccessful().withVwMessage("Report Schedule changed").build();
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

    @Override
    public Response updateUser(String header, HttpServletRequest request) throws OAuthSystemException {
        tokenValidator.setHeader(header);
        Boolean tokenValid = tokenValidator.isTokenValid();
        if (!tokenValid) {
            VWResponse response = new VWResponse();
            response.setMessage("Error");
            response.setDescription("Token is not valid");
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
        }

        User user = tokenValidator.getUser();
        DefaultUser contact;
        try {
            inputConverter.setHTTPRequest(request, DefaultUser.class);
            contact = inputConverter.getDefaultUser();
        } catch (IOException ex) {
            VWResponse response = new VWResponse();
            response.setMessage("Error");
            response.setMessage(VarWatchInputConverter.HTTPParsingResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription());
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
        }

        Response response = registrationManager.updateUser(contact,user);
        return response;
    }

    public enum ReportSchedule {

        never, daily, weekly, monthly, yearly;
    }
}
