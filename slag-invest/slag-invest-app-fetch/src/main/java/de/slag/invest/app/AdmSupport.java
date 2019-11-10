package de.slag.invest.app;

import java.util.Optional;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.base.BaseException;

public class AdmSupport {

	private static final Log LOG = LogFactory.getLog(AdmSupport.class);

	private static AdmSupport instance;

	private final Properties properties;

	public static AdmSupport getInstance() {
		if (instance == null) {
			instance = new AdmSupport();
		}
		return instance;
	}

	// singleton
	AdmSupport() {
		properties = new Properties();
	}

	public void init(Properties properties) {
		if (!this.properties.isEmpty()) {
			throw new BaseException("already initialized");
		}
		this.properties.putAll(properties);
		LOG.info("initialized.");
	}

	public Optional<String> getProperty(String key) {
		final String property = properties.getProperty(key);
		if (property == null) {
			return Optional.empty();
		}
		return Optional.of(property);
	}

}
