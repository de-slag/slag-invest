package de.slag.invest.app.cli;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.common.base.AdmService;
import de.slag.invest.appservice.dataimport.DataImportSerivce;
import de.slag.invest.appservice.report.ReportService;

@Service
public class InvestAppService {

	private static final Log LOG = LogFactory.getLog(InvestAppService.class);
	
	@Resource
	private AdmService admService;
	
	@Resource
	private DataImportSerivce dataImportSerivce;
	
	@Resource
	private ReportService reportService;


	public void importData() {
		dataImportSerivce.importData();
	}
	
	public void report() {
		System.out.println(reportService.testReport());
	}

}
