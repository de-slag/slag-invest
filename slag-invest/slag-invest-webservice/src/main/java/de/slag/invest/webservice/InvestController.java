package de.slag.invest.webservice;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.slag.common.base.AdmCache;
import de.slag.invest.iface.av.api.StockValueDataImportService;
import de.slag.invest.service.PortfolioTransactionService;
import de.slag.invest.service.StockValueService;

@RestController
public class InvestController {

	private static final Log LOG = LogFactory.getLog(InvestController.class);

	@Resource
	private AdmCache admCache;

	@Resource
	private PortfolioTransactionService portfolioTransactionService;

	@Resource
	private StockValueDataImportService stockValueDataImportService;

	@Resource
	private StockValueService stockValueService;

	@GetMapping("/fetchStockValues")
	public Collection<String> getFetchStockValues() {		
		stockValueDataImportService.importData();

		final ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("function: FETCH STOCK VALUES");
		arrayList.add(String.format("result: success, %s", new Date()));
		return arrayList;
	}

	@GetMapping("/status")
	public String getStatus() {
	
		
		
		final List<String> asList = new ArrayList<>();
		asList.add("start time: " + new Date(ManagementFactory.getRuntimeMXBean().getStartTime()));
		asList.add("portfolio transactions: " + portfolioTransactionService.findAllIds().size());
		asList.add("stock values: " + stockValueService.findAllIds().size());
		final Runtime runtime = Runtime.getRuntime();

		asList.add(String.format("mem: %s M (%s M)", (runtime.totalMemory() - runtime.freeMemory()) / (1000 * 1000),
				runtime.totalMemory() / 1000000));
		
		return new SimpleHtmlResponse("Status", "", asList).toString();
	}

	@GetMapping
	public String get() {
		final HtmlDecorator htmlDecorator = new HtmlDecorator();
		final List<String> responseList = new ArrayList<>();
		responseList.add(htmlDecorator.h1("CALLS"));

		final List<String> calls = Arrays.asList("status", "fetchStockValues", "test","crud");

		responseList.addAll(calls.stream().map(c -> htmlDecorator.a(c)).collect(Collectors.toList()));

		return htmlDecorator.decorateList(responseList);
	}

	@GetMapping("/test")
	public String getTest(@RequestParam(required = false) String param) {
		return "test, param: " + param;
	}

}
