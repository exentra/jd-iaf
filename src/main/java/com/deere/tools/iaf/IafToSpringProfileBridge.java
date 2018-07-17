package com.deere.tools.iaf;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;

/**
 * Convert Iaf Config Suffix to corresponding spring profile 
 */
public class IafToSpringProfileBridge implements ServletContextListener {

	public static final String IAF_CONFIG_SUFFIX = "IafConfigSuffix";

	private static final Logger LOG = LoggerFactory.getLogger(IafToSpringProfileBridge.class);

	@Override
	public void contextInitialized(final ServletContextEvent sce) {
		final String iafConfigSuffix = System.getProperty(IAF_CONFIG_SUFFIX, "");
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, iafConfigSuffix);
		LOG.info("IAF Config Suffix / active profile is {}", iafConfigSuffix);
	}

	@Override
	public void contextDestroyed(final ServletContextEvent sce) {
		// implementation is required by interface, but we don't have to do any cleanup
	}

}
