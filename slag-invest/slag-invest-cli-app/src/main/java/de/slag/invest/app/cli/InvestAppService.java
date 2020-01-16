package de.slag.invest.app.cli;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.common.base.AdmCache;
import de.slag.invest.appservice.dataimport.DataImportSerivce;
import de.slag.invest.appservice.dataimport.PortfolioTransactionImportService;
import de.slag.invest.appservice.dataimport.PortfolioUpdateService;
import de.slag.invest.appservice.report.ReportService;

@Service
public class InvestAppService {

	private static final Log LOG = LogFactory.getLog(InvestAppService.class);

	@Resource
	private AdmCache admCache;

	@Resource
	private DataImportSerivce dataImportSerivce;

	@Resource
	private ReportService reportService;

	@Resource
	private PortfolioUpdateService portfolioUpdateService;

	@Resource
	private PortfolioTransactionImportService portfolioTransactionImportService;

	public void importData() {
		dataImportSerivce.importData();
	}

	public void importPortfolioTransactions() {
		portfolioTransactionImportService.importTransactions();
	}

	public void updatePortfolios() {
		portfolioUpdateService.updatePortfolios();
	}

	public void report() {
		System.out.println(reportService.testReport());
	}

}
