/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.config;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.reference_db.RefDatabase;
import java.util.List;

/**
 *
 * @author broder
 */
public class ConfigurationManager {

    @Inject
    private ConfigurationDao configDao;

    @Transactional
    public List<VarWatchConfig> getConfigurations() {
        return configDao.getAllConfigurations();
    }

    @Transactional
    public void addConfiguration(String key, String value) {
        VarWatchConfig config = new VarWatchConfig(key, value);
        configDao.update(config);
    }

    @Transactional
    public String getConfiguration(String key) {
        return configDao.getConfiguration(key);
    }

    @Transactional
    public List<FilterConfig> getFilterOptions() {
        return configDao.getFilterConfigurations();
    }

    @Transactional
    public void addFilterConfiguration(String key, String value, String type, boolean enabled) {
        FilterConfig filterConfig = new FilterConfig(key, value, type, enabled);
        configDao.updateFilterConfig(filterConfig);
    }

    public List<RefDatabase> getActiveBeacons() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
