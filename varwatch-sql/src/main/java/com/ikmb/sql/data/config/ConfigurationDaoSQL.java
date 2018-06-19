/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.data.config;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.data.config.ConfigurationDao;
import com.ikmb.core.data.config.FilterConfig;
import com.ikmb.core.data.config.VarWatchConfig;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class ConfigurationDaoSQL implements ConfigurationDao {

    @Inject
    private Provider<EntityManager> emProvider;

    @Override
    public List<VarWatchConfig> getAllConfigurations() {
        TypedQuery<VarWatchConfig> query = emProvider.get().createQuery("SELECT u FROM VarWatchConfig u", VarWatchConfig.class
        );
        return query.getResultList();
    }

    @Override
    public void update(VarWatchConfig config) {
        emProvider.get().merge(config);
    }

    @Override
    public String getConfiguration(String key) {
        TypedQuery<VarWatchConfig> query = emProvider.get().createQuery("SELECT s FROM VarWatchConfig s WHERE s.key = :key", VarWatchConfig.class
        );
        try {
            VarWatchConfig curConfig = query.setParameter("key", key).getSingleResult();
            return curConfig.getValue();
        } catch (NoResultException nre) {
            System.out.println("no client result found");
            return null;
        }
    }

    @Override
    public List<FilterConfig> getFilterConfigurations() {
        TypedQuery<FilterConfig> query = emProvider.get().createQuery("SELECT u FROM FilterConfig u", FilterConfig.class
        );
        return query.getResultList();
    }

    @Override
    public void updateFilterConfig(FilterConfig filterConfig) {
        EntityManager get = emProvider.get();
        TypedQuery<FilterConfig> query = get.createQuery("SELECT s FROM FilterConfig s WHERE s.key = :key", FilterConfig.class);
        try {
            FilterConfig dbConfig = query.setParameter("key", filterConfig.getName()).getSingleResult();
            if (filterConfig.isEnabled() != null) {
                dbConfig.setEnabled(filterConfig.isEnabled());
            }
            if (filterConfig.getFilterType() != null) {
                dbConfig.setFilterType(filterConfig.getFilterType());
            }
            if (filterConfig.getValue() != null) {
                dbConfig.setValue(filterConfig.getValue());
            }
            get.merge(dbConfig);
        } catch (NoResultException nre) {
            System.out.println("no filter config found, insert new filter config");
            get.persist(filterConfig);
        }
    }
}
