/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.auth.token;

import com.ikmb.core.data.auth.client.AuthClient;
import com.ikmb.core.data.auth.user.User;

/**
 *
 * @author broder
 */
public interface TokenDao {

    public AuthToken getValidTokenByUserAndClient(User user, AuthClient client);

    public AuthToken getToken(String token);

    public void update(AuthToken tokenSQL);

    public void save(AuthToken tokensql);

    public boolean remove(String accessToken);

}
