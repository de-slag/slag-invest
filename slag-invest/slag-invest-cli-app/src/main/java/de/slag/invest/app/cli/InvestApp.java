package de.slag.invest.app.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.base.BaseException;
import de.slag.common.context.SlagContext;
import de.slag.common.utils.CliOptionsUtils;

public class InvestApp {

	private static final Log LOG = LogFactory.getLog(InvestApp.class);

	public static void main(String[] args) {
		final InvestApp investApp = new InvestApp();
		investApp.setUp(args);
		try {
			investApp.run();
		} catch (Throwable t) {
			LOG.error("error run application", t);
			System.exit(1);
		}

		LOG.info("all done, Exit!");
		System.exit(0);
	}

	private InvestAppService investAppService;

	private void setUp(String[] args) {
		final Options options = CliOptionsUtils.createOptions();
		options.addOption("config", true, "configuration properties file");
		CommandLine cliArgs;
		try {
			cliArgs = CliOptionsUtils.parse(options, args);
		} catch (ParseException e) {
			throw new BaseException(e);
		}
		final String configFilePath = cliArgs.getOptionValue("config");
		if (StringUtils.isBlank(configFilePath)) {
			throw new BaseException("argument not setted: '%s'", "config");
		}
		LOG.warn("config: " + configFilePath);
		System.setProperty(InvestAppAdmCacheImpl.CONFIG, configFilePath);

		investAppService = SlagContext.getBean(InvestAppService.class);
		LOG.info(String.format("initialized: '%s'", investAppService));
	}

	private void run() {
		investAppService.importData();
		investAppService.report();
	}
}
