package de.slag.invest.app.cli;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.context.SlagContext;

public class InvestApp {
	
	private static final Log LOG = LogFactory.getLog(InvestApp.class);
	
	public static void main(String[] args) {
		final InvestApp investApp = new InvestApp();
		investApp.setUp();
		investApp.run();
	}
	
	private InvestAppService investAppService;

	private void setUp() {
		investAppService = SlagContext.getBean(InvestAppService.class);
		LOG.info(String.format("initialized: '%s'", investAppService));
	}

	private void run() {
		investAppService.importData();
		investAppService.calc();
	}
}
