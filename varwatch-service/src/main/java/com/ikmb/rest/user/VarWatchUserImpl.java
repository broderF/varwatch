package com.ikmb.rest.user;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.auth.user.UserManager;
import com.ikmb.core.data.wipe.WipeDataManager;
import com.ikmb.core.varwatchcommons.entities.DefaultUser;
import com.ikmb.core.varwatchcommons.entities.VWResponse;
import com.ikmb.core.utils.VarWatchException;
import com.ikmb.rest.HTTPVarWatchResponse;
import com.ikmb.rest.util.ResponseBuilder;
import com.ikmb.rest.util.HTTPVarWatchInputConverter;
import com.ikmb.rest.registration.RegistrationManager;
import com.ikmb.rest.util.AdminRequestFilter.AdminFilter;
import com.ikmb.rest.util.TokenRequestFilter.TokenFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bfredrich
 */
@Path("user")
@TokenFilter
public class VarWatchUserImpl {

    private Integer expiresIn = 3600;

    private static final Logger logger = LoggerFactory.getLogger(VarWatchUserImpl.class);

    @Inject
    private HTTPVarWatchInputConverter inputConverter;
    @Inject
    private RegistrationManager registrationManager;
//    @Inject
//    private HTTPTokenConverter tokenConverter;
    @Inject
    private UserManager userManager;
    @Inject
    private WipeDataManager wipeDb;

    @POST
    @Path("report")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setUserReportSchedule(@HeaderParam("Authorization") String header, @QueryParam("schedule") String reportSchedule) {

        User user = inputConverter.getUserFromHeader(header);
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

    @PUT
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@HeaderParam("Authorization") String header, @Context HttpServletRequest request) {

        User user = inputConverter.getUserFromHeader(header);
        System.out.println(user.getIsAdmin());
        DefaultUser contact;
        try {
            inputConverter.setHTTPRequest(request, DefaultUser.class);
            contact = inputConverter.getDefaultUser();
        } catch (VarWatchException ex) {
            VWResponse response = new VWResponse();
            response.setMessage("Error");
            response.setMessage(HTTPVarWatchResponse.HTTP_REQUEST_NOT_PARSABLE.getDescription());
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
        }

        Response response = registrationManager.updateUser(contact, user);
        return response;
    }

    @POST
    @Path("delete")
    public Response deleteUser(@HeaderParam("Authorization") String header) {

        User user = inputConverter.getUserFromHeader(header);
        String response = wipeDb.wipeDataByUser(user.getMail());
        userManager.delete(user);
        return Response.status(Response.Status.OK).entity(new Gson().toJson(response)).build();
    }

    @POST
    @AdminFilter
    @Path("activate")
    public Response activateUser(@HeaderParam("Authorization") String header, @QueryParam("mail") String userMail) {

        User user = userManager.getUser(userMail);
        if (user != null) {
            user.setActive(Boolean.TRUE);
            userManager.update(user);
            return Response.status(Response.Status.OK).entity("User acticated").build();
        } else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Cant find user with the given email").build();
        }
    }

    @GET
    @AdminFilter
    @Path("inactive")
    public Response getInactiveUsers() {

        List<User> user = userManager.getAllUser();
        List<User> inactiveUsers = user.stream().filter((u) -> (!u.getActive())).collect(Collectors.toList());
        return new ResponseBuilder().buildListWithExpose(inactiveUsers);
    }

    public enum ReportSchedule {

        never, daily, weekly, monthly, yearly;
    }
}
