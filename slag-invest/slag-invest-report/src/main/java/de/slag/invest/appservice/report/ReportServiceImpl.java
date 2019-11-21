package de.slag.invest.appservice.report;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.invest.service.StockValueService;

@Service
public class ReportServiceImpl implements ReportService {
	
	@Resource
	private StockValueService stockValueService;

	public String testReport() {
		return "Test-Report: " + stockValueService.findAllIds().size() + " datasets found";
	}

}
