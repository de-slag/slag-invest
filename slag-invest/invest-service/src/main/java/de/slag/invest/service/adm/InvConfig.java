package de.slag.invest.service.adm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.base.BaseException;

public class InvConfig {

	private static final Log LOG = LogFactory.getLog(InvConfig.class);

	public static String getProperty(String key, String defaultValue) {
		return getProperties().getProperty(key, defaultValue);
	}

	static Properties getProperties() {
		final Properties properties = new Properties();
		final String slagConfigFileName = System.getProperty("SlagConfig");
		if (slagConfigFileName == null) {
			LOG.warn("no config file setted, use jvm parameter :'-DSlagConfig=/path/to/file'");
		} else if (!new File(slagConfigFileName).exists()) {
			LOG.warn(String.format("config file does not exists: %s", slagConfigFileName));
		} else {
			LOG.info("use as config file: " + slagConfigFileName);
			try {
				properties.load(new FileInputStream(slagConfigFileName));
			} catch (IOException e) {
				throw new BaseException(e);
			}
		}
		return properties;
	}

}