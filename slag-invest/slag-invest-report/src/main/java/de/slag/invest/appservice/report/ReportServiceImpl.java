package de.slag.invest.appservice.report;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.invest.service.PortfolioTransactionService;
import de.slag.invest.service.StockValueService;

@Service
public class ReportServiceImpl implements ReportService {

	@Resource
	private StockValueService stockValueService;

	@Resource
	private PortfolioTransactionService portfolioTransactionService;

	public String testReport() {
		List<String> report = new ArrayList<>();
		report.add("Test-Report:");
		report.add(stockValueService.findAllIds().size() + " stock datasets found");
		report.add(portfolioTransactionService.findAllIds().size() + " portfolio transactions found");

		return String.join("\n", report);
	}

}
