/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.config;

import java.util.List;

/**
 *
 * @author broder
 */
public interface ConfigurationDao {

    public List<VarWatchConfig> getAllConfigurations();

    public String getConfiguration(String key);

    public List<FilterConfig> getFilterConfigurations();

    public void update(VarWatchConfig config);

    public void updateFilterConfig(FilterConfig filterConfig);

}
