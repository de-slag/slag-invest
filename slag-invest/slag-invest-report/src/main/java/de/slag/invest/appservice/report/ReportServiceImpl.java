package de.slag.invest.appservice.report;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.invest.service.PortfolioPositionService;
import de.slag.invest.service.StockValueService;

@Service
public class ReportServiceImpl implements ReportService {

	@Resource
	private StockValueService stockValueService;

	@Resource
	private PortfolioPositionService portfolioPositionService;

	public String testReport() {
		List<String> report = new ArrayList<>();
		report.add("Test-Report:");
		report.add(stockValueService.findAllIds().size() + " stock datasets found");
		report.add(portfolioPositionService.findAllIds().size() + " portfolio positions found");

		return String.join("\n", report);
	}

}
