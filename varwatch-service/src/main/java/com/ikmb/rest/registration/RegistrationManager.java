/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.registration;

import com.google.inject.Inject;
import com.ikmb.core.varwatchcommons.entities.VWResponse;
import com.ikmb.core.varwatchcommons.entities.RegistrationUser;
import com.ikmb.core.varwatchcommons.entities.DefaultUser;
import com.ikmb.rest.util.ResponseBuilder;
import static com.ikmb.rest.registration.RegistrationManager.RegistrationResponse.REGISTRATION_ERROR_DOUBLE_ENTITY;
import static com.ikmb.rest.registration.RegistrationManager.RegistrationResponse.REGISTRATION_ERROR_INFORMATIONS_MISSING;
import static com.ikmb.rest.registration.RegistrationManager.RegistrationResponse.REGISTRATION_SUCCESFULL;
import static com.ikmb.rest.registration.RegistrationManager.RegistrationResponse.UPDATE_SUCCESFULL;
import com.ikmb.core.data.auth.client.ClientManager;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.auth.user.UserBuilder;
import com.ikmb.core.data.auth.user.UserManager;
import com.ikmb.core.varwatchcommons.entities.Client;
import java.util.List;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reponsible for the user registration/authentification. Abstract Layer above
 * the direct Connection to the database.
 *
 * @author broder
 */
public class RegistrationManager {

    @Inject
    private ClientManager clientManager;
    @Inject
    private UserManager userManager;
    @Inject
    private UserBuilder userBuilder;

    private static final Logger logger = LoggerFactory.getLogger(RegistrationManager.class);

    /**
     * Saves a client in the database
     *
     * @param client
     * @return
     */
    public String saveClient(Client client) {
        return clientManager.save(client);
    }

    /**
     * Saves a user in the database
     *
     * @param user
     * @return
     */
    public Response saveUser(RegistrationUser user) {
        VWResponse response = new VWResponse();
        User userSQL = userBuilder.withRegistrationUser(user).secretPass().build();
        if (!validateRegistrationUser(user)) {
            response.setMessage(REGISTRATION_ERROR_INFORMATIONS_MISSING.message);
            response.setDescription(REGISTRATION_ERROR_INFORMATIONS_MISSING.description);
            Response httpResponse = Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
            return httpResponse;
        }

        List<User> allUser = userManager.getAllUser();

        if (allUser.isEmpty()) {
            userSQL.setIsAdmin(Boolean.TRUE);
        }

        boolean isSuccesFull = userManager.save(userSQL);
        if (isSuccesFull) {
            response.setMessage(REGISTRATION_SUCCESFULL.message);
            response.setDescription(REGISTRATION_SUCCESFULL.description);
            Response httpResponse = Response.status(Response.Status.OK).entity(response).build();
            return httpResponse;
        } else {
            response.setMessage(REGISTRATION_ERROR_DOUBLE_ENTITY.message);
            response.setDescription(REGISTRATION_ERROR_DOUBLE_ENTITY.description);
            Response httpResponse = Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
            return httpResponse;
        }

    }

    private boolean validateRegistrationUser(RegistrationUser user) {
        if (user == null) {
            return false;
        }

        if (user.getMail() == null || user.getMail().isEmpty()) {
            return false;
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return false;
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            return false;
        }
        if (user.getInstitution() == null || user.getInstitution().isEmpty()) {
            return false;
        }
        if (user.getPassword() == null || user.getPassword().getPassword() == null || user.getPassword().getPassword().isEmpty()) {
            return false;
        }
        return true;
    }

    public Response updateUser(DefaultUser contact, User userSql) {
        if (contact.getFirstName() != null) {
            userSql.setFirstName(contact.getFirstName());
        }
        if (contact.getInstitution() != null) {
            userSql.setInstitution(contact.getInstitution());
        }
        if (contact.getLastName() != null) {
            userSql.setLastName(contact.getLastName());
        }
        if (contact.getPhone() != null) {
            userSql.setPhone(contact.getPhone());
        }
        if (contact.getAddress() != null) {
            userSql.setAddress(contact.getAddress());
        }
        if (contact.getCity() != null) {
            userSql.setCity(contact.getCity());
        }
        if (contact.getCountry() != null) {
            userSql.setCountry(contact.getCountry());
        }
        if (contact.getPostalCode() != null) {
            userSql.setPostalCode(contact.getPostalCode());
        }
        userManager.update(userSql);
        return new ResponseBuilder().withVwSuccessful().withVwMessage(UPDATE_SUCCESFULL.getDescription()).withStatusType(Response.Status.OK).build();
    }

    public enum RegistrationResponse {

        REGISTRATION_SUCCESFULL("Successful", "User successfully registered in database"),
        REGISTRATION_ERROR_DOUBLE_ENTITY("Error", "User exists already in database"),
        REGISTRATION_ERROR_INFORMATIONS_MISSING("Error", "User information missing"),
        UPDATE_SUCCESFULL("Successful", "User successfully updated in database");

        private String message;
        private String description;

        RegistrationResponse(String message, String description) {
            this.message = message;
            this.description = description;
        }

        public String getMessage() {
            return message;
        }

        public String getDescription() {
            return description;
        }

    }
}
