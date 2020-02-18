package de.slag.invest.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import de.slag.common.base.AdmCache;
import de.slag.common.base.BaseException;

@Component
public class InvestWsAdmCacheImpl implements AdmCache {

	private static final Log LOG = LogFactory.getLog(InvestWsAdmCacheImpl.class);

	public static final String CONFIG = "de.slag.configfile";

	private Properties properties = new Properties();

	@PostConstruct
	public void setUp() {
		final String configFilePath = System.getProperty(CONFIG);
		if (StringUtils.isBlank(configFilePath)) {
			throw new BaseException("property not setted: 'config'");
		}
		File file = new File(configFilePath);
		if (!file.exists()) {
			throw new BaseException("file not exists:" + configFilePath);
		}
		FileInputStream inStream;
		try {
			inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new BaseException(e);
		}
		try {
			properties.load(inStream);
		} catch (IOException e) {
			throw new BaseException(e);
		}

		final List<String> keyValuesStrings = properties.keySet().stream().map(key -> (String) key)
				.map(key -> key + ": " + properties.getProperty(key)).collect(Collectors.toList());

		LOG.debug(String.format("configuration:\n%s", String.join("\n", keyValuesStrings)));

		LOG.info("setUp successful");

	}

	@Override
	public String get(String key) {		
		return properties.getProperty(key);
	}

}
