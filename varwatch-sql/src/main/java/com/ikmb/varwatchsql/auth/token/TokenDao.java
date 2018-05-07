/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.auth.token;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.ikmb.varwatchsql.auth.client.AuthClientSQL;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
@Singleton
public class TokenDao {

    @Inject
//    EntityManager em;
    private Provider<EntityManager> emProvider;
    private static final Logger logger = LoggerFactory.getLogger(TokenDao.class);

    public AuthTokenSQL getValidTokenByUserAndClient(UserSQL user, AuthClientSQL client) {
        TypedQuery<AuthTokenSQL> query = emProvider.get().createQuery("SELECT s FROM AuthTokenSQL s WHERE s.user = :user AND s.client = :client AND s.expires > NOW()", AuthTokenSQL.class);
        AuthTokenSQL tokenSQL = null;
        try {
            tokenSQL = query.setParameter("user", user).setParameter("client", client).getSingleResult();
            emProvider.get().refresh(tokenSQL);
        } catch (NoResultException nre) {
            logger.error("No valid token found");
        }
        return tokenSQL;
    }

    public AuthTokenSQL getToken(String token) {
        TypedQuery<AuthTokenSQL> query = emProvider.get().createQuery("SELECT s FROM AuthTokenSQL s WHERE s.token = :token", AuthTokenSQL.class);
        AuthTokenSQL tokenSQL = null;
        try {
            tokenSQL = query.setParameter("token", token).getSingleResult();
            emProvider.get().refresh(tokenSQL);
        } catch (NoResultException nre) {
        }
        return tokenSQL;
    }

    public void update(AuthTokenSQL tokenSQL) {
        AuthTokenSQL currentToken = emProvider.get().find(AuthTokenSQL.class, tokenSQL.getId());
        currentToken.setExpiresIn(tokenSQL.getExpiresIn());
        emProvider.get().merge(currentToken);
        emProvider.get().refresh(currentToken);
    }

    public void save(AuthTokenSQL tokensql) {
        emProvider.get().persist(tokensql);
        emProvider.get().refresh(tokensql);
    }

    public boolean remove(String accessToken) {
        AuthTokenSQL tokenSQL = getToken(accessToken);
        if (tokenSQL == null) {
            logger.error("token is null {}", accessToken);
        }
        emProvider.get().remove(tokenSQL);
        emProvider.get().refresh(tokenSQL);
        return true;
    }

}
