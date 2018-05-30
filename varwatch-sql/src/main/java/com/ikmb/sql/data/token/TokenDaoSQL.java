/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.data.token;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.ikmb.core.data.auth.client.AuthClient;
import com.ikmb.core.data.auth.token.AuthToken;
import com.ikmb.core.data.auth.token.TokenDao;
import com.ikmb.core.data.auth.user.User;
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
public class TokenDaoSQL implements TokenDao {

    @Inject
//    EntityManager em;
    private Provider<EntityManager> emProvider;
    private static final Logger logger = LoggerFactory.getLogger(TokenDao.class);

    public AuthToken getValidTokenByUserAndClient(User user, AuthClient client) {
        TypedQuery<AuthToken> query = emProvider.get().createQuery("SELECT s FROM AuthToken s WHERE s.user = :user AND s.client = :client AND s.expires > NOW()", AuthToken.class);
        AuthToken tokenSQL;
        try {
            tokenSQL = query.setParameter("user", user).setParameter("client", client).getSingleResult();
            emProvider.get().refresh(tokenSQL);
            return tokenSQL;
        } catch (NoResultException nre) {
            logger.error("No valid token found");
        }
        return null;
    }

    public AuthToken getToken(String token) {
        TypedQuery<AuthToken> query = emProvider.get().createQuery("SELECT s FROM AuthToken s WHERE s.token = :token", AuthToken.class);
        AuthToken tokenSQL = null;
        try {
            tokenSQL = query.setParameter("token", token).getSingleResult();
            emProvider.get().refresh(tokenSQL);
        } catch (NoResultException nre) {
        }
        return tokenSQL;
    }

    public void update(AuthToken tokenSQL) {
//        AuthToken currentToken = emProvider.get().find(AuthToken.class, tokenSQL.getId());
//        currentToken.setExpiresIn(tokenSQL.getExpiresIn());
        emProvider.get().merge(tokenSQL);
//        emProvider.get().refresh(currentToken);
    }

    public void save(AuthToken tokensql) {
        emProvider.get().persist(tokensql);
        emProvider.get().refresh(tokensql);
    }

    public boolean remove(String accessToken) {
        AuthToken tokenSQL = getToken(accessToken);
        if (tokenSQL == null) {
            logger.error("token is null {}", accessToken);
        }
        emProvider.get().remove(tokenSQL);
        emProvider.get().refresh(tokenSQL);
        return true;
    }

}
