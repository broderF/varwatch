/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.auth.token;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.ikmb.varwatchsql.auth.client.AuthClientSQL;
import com.ikmb.varwatchsql.auth.client.ClientDao;
import com.ikmb.varwatchsql.auth.user.UserDao;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class TokenManager {

//    @Inject
//    EntityManager em;
    @Inject
    private UserDao userDao;

    @Inject
    private ClientDao clientDao;

    @Inject
    private TokenDao tokenDao;

    private Integer expiresIn = 3600;
    private static final Logger logger = LoggerFactory.getLogger(TokenManager.class);

    @Transactional
    public String getValidToken(String userMail, String clientName) {
        String token = null;
        UserSQL userByName = userDao.getUserByName(userMail);
        logger.info("current userMail is {} with id {}", userMail, userByName.getId());
        AuthClientSQL clientByName = clientDao.getClientByName(clientName);
        logger.info("current client is {} with id {}", clientName, clientByName.getId());
        AuthTokenSQL validTokenByUserAndClient = tokenDao.getValidTokenByUserAndClient(userByName, clientByName);
        if (validTokenByUserAndClient != null) {
            token = validTokenByUserAndClient.getToken();
        }
        return token;
    }

    @Transactional
    public void refreshToken(String currentToken, String userMail, String clientName, Integer expiresIn) {
        AuthTokenSQL tokenSQL = tokenDao.getToken(currentToken);
        DateTime expiresInDate = tokenSQL.getExpiresIn();
        DateTime newDate = expiresInDate.plusSeconds(expiresIn);
        tokenSQL.setExpiresIn(newDate);
        tokenDao.update(tokenSQL);
    }

    @Transactional
    public void createNewToken(String token, String userName, String clientName, Integer expiresIn) {
        AuthTokenSQL tokensql = new AuthTokenSQL();
        System.out.println(clientName);
        AuthClientSQL clientByName = clientDao.getClientByName(clientName);
        System.out.println(clientByName.getName());
        tokensql.setClient(clientByName);
        DateTime newTime = new DateTime().plusSeconds(expiresIn);
        logger.info("New datetime is {}", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(newTime));
        tokensql.setExpiresIn(newTime);
        tokensql.setToken(token);
        UserSQL userByName = userDao.getUserByName(userName);
        System.out.println(userByName.getMail());
        tokensql.setUser(userByName);
        tokenDao.save(tokensql);
    }

//    @Transactional
//    public boolean isTokenValid(String expToken, String href, String clientName) {
//        UserSQL userByName = userDao.getUserByName(href);
//        AuthClientSQL clientByName = clientDao.getClientByName(clientName);
//        AuthTokenSQL validTokenByUserAndClient = tokenDao.getValidTokenByUserAndClient(userByName, clientByName);
//        String token = null;
//        if (validTokenByUserAndClient != null) {
//            token = validTokenByUserAndClient.getToken();
//        }
//        if (token != null && expToken.equals(token)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    @Transactional
    public boolean isTokenValid(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            return false;
        }

        AuthTokenSQL token = tokenDao.getToken(accessToken);
        DateTime expiresInDate = new DateTime();
        DateTime newDate = expiresInDate.plusSeconds(expiresIn);

        if (token != null && token.getExpiresIn().isAfter(new DateTime())) {

            token.setExpiresIn(newDate);
            tokenDao.update(token);
            return true;
        } else {
            if (token != null) {
                logger.error("current expiresIn {} and in millis {}", token.getExpiresIn().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), token.getExpiresIn().getMillis());
            }else{
                logger.error("Token is null!");
            }
            logger.error("current datetime {} and in millis {}", new DateTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), new DateTime().getMillis());
            logger.error("new expires in  datetime {} and in millis {}", newDate.toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), newDate.getMillis());

            return false;
        }
    }

    @Transactional
    public UserSQL getUserByToken(String accessToken) {
        AuthTokenSQL token = tokenDao.getToken(accessToken);
        UserSQL user = token.getUser();
        return userDao.getUserByID(user.getId());
    }

    @Transactional
    public AuthClientSQL getClientByToken(String accessToken) {
        AuthTokenSQL token = tokenDao.getToken(accessToken);
        return token.getClient();
    }

    @Transactional
    public boolean deleteToken(String accessToken) {
        return tokenDao.remove(accessToken);
    }

}
