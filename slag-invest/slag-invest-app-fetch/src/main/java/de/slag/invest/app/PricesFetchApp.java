package de.slag.invest.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.base.BaseException;
import de.slag.common.utils.CliOptionsUtils;

public class PricesFetchApp {

	private static final Log LOG = LogFactory.getLog(PricesFetchApp.class);

	private static final String CONFIG = "config";

	public static void main(String[] args) throws ParseException, FileNotFoundException, IOException {
		setUp(args);
		new PricesFetchAppRunner().run();
	}

	private static void setUp(String[] args) throws ParseException, IOException, FileNotFoundException {
		final Options options = CliOptionsUtils.createOptions();
		options.addOption(CONFIG, true, "config properties file");
		final CommandLine cliOptions = CliOptionsUtils.parse(options, args);
		final String configFilePath = cliOptions.getOptionValue(CONFIG);
		if (configFilePath == null) {
			throw new BaseException("cli arg not setted: '%s'", CONFIG);
		}
		LOG.info(String.format("using as config: '%s'", configFilePath));

		final File configFile = new File(configFilePath);
		if (!configFile.exists()) {
			throw new BaseException("config file does not exists '%s'", configFilePath);
		}

		final Properties applicationProperties = new Properties();
		applicationProperties.load(new FileInputStream(configFile));

		LOG.debug("configuration:\n" + String.join("\n", applicationProperties.stringPropertyNames().stream()
				.map(key -> key + "=" + applicationProperties.getProperty(key)).collect(Collectors.toList())));
		AdmSupport.getInstance().init(applicationProperties);
	}
}
