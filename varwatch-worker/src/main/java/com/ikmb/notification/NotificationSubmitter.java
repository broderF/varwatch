/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.notification;

import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.variant.Variant;


/**
 *
 * @author bfredrich
 */
public class NotificationSubmitter {

    private User user;
    private Variant variantQuery;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Variant getVariantQuery() {
        return variantQuery;
    }

    public String getSubmittedVariant() {
        return variantQuery.getChromosomeName() + "," + variantQuery.getChromosomePos() + "," + variantQuery.getReferenceBase() + "," + variantQuery.getAlternateBase();
    }

    public void setVariantQuery(Variant variantQuery) {
        this.variantQuery = variantQuery;
    }

    /**
     *
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof NotificationSubmitter)) {
            return false;
        }
        NotificationSubmitter otherMyClass = (NotificationSubmitter) other;
        if (otherMyClass.getUser().getMail().equals(user.getMail())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.user != null ? this.user.hashCode() : 0);
        return hash;
    }
}
