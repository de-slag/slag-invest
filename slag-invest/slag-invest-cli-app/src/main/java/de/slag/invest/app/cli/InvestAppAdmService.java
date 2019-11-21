package de.slag.invest.app.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.common.base.AdmService;
import de.slag.common.base.BaseException;

@Service
public class InvestAppAdmService implements AdmService {

	private static final Log LOG = LogFactory.getLog(InvestAppAdmService.class);

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

	public Optional<String> getValue(String key) {
		final String property = properties.getProperty(key);
		if (property == null) {
			return Optional.empty();
		}
		return Optional.of(property);
	}

}
