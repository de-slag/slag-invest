package de.slag.invest.app.cli;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.common.base.AdmCache;
import de.slag.invest.appservice.dataimport.PortfolioTransactionImportService;
import de.slag.invest.appservice.report.ReportService;
//import de.slag.invest.iface.av.api.StockValueDataImportService;

@Service
public class InvestAppService {

	private static final Log LOG = LogFactory.getLog(InvestAppService.class);

	@Resource
	private AdmCache admCache;

	@Resource
//	private StockValueDataImportService stockValueDataImportService;
	private Object stockValueDataImportService;

	@Resource
	private ReportService reportService;

//	@Resource
//	private PortfolioUpdateService portfolioUpdateService;

	@Resource
	private PortfolioTransactionImportService portfolioTransactionImportService;
	
	@Resource
	private InvestAppStatisticService investAppStatisticService;

	public void importStockData() {
//		stockValueDataImportService.importData();
	}

	public void importPortfolioTransactions() {
		portfolioTransactionImportService.importTransactions();
	}

	public void updatePortfolios() {
//		portfolioUpdateService.updatePortfolios();
	}
	
	public void printStatistics() {
		LOG.info(investAppStatisticService.printStatistics());
	}

	public void report() {
		System.out.println(reportService.testReport());
	}

}
