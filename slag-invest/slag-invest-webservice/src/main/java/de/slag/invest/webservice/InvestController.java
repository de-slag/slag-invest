package de.slag.invest.webservice;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.catalina.connector.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.slag.common.base.AdmCache;
import de.slag.common.base.BaseException;
import de.slag.invest.iface.av.api.StockValueDataImportService;
import de.slag.invest.model.Mandant;
import de.slag.invest.model.User;
import de.slag.invest.service.MandantService;
import de.slag.invest.service.PortfolioTransactionService;
import de.slag.invest.service.StockValueService;
import de.slag.invest.service.UserService;
import de.slag.invest.webservice.response.HtmlDecorator;
import de.slag.invest.webservice.response.SimpleHtmlResponse;
import de.slag.invest.webservice.response.WsResponse;

@RestController
public class InvestController extends AbstractInvController {

	private static final Log LOG = LogFactory.getLog(InvestController.class);

	@Resource
	private AdmCache admCache;

	@Resource
	private PortfolioTransactionService portfolioTransactionService;

	@Resource
	private StockValueDataImportService stockValueDataImportService;

	@Resource
	private StockValueService stockValueService;

	@Resource
	private UserService userService;

	@Resource
	private MandantService mandantService;

	@GetMapping("/fetchStockValues")
	public Collection<String> getFetchStockValues() {
		stockValueDataImportService.importData();

		final ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("function: FETCH STOCK VALUES");
		arrayList.add(String.format("result: success, %s", new Date()));
		return arrayList;
	}

	@GetMapping("/status")
	public String getStatus(@RequestParam(required = false) String token) {
		final List<String> asList = new ArrayList<>();

		if (!getCredentialsComponent().isValid(token)) {
			asList.add("running");
		} else {
			asList.add("start time: " + new Date(ManagementFactory.getRuntimeMXBean().getStartTime()));
			asList.add("portfolio transactions: " + portfolioTransactionService.findAllIds().size());
			asList.add("stock values: " + stockValueService.findAllIds().size());
			final Runtime runtime = Runtime.getRuntime();

			asList.add(String.format("memory: %s M (%s M)",
					(runtime.totalMemory() - runtime.freeMemory()) / (1000 * 1000), runtime.totalMemory() / 1000000));

		}

		return new SimpleHtmlResponse("Status", "", asList).toString();
	}

	@GetMapping
	public String get() {
		final HtmlDecorator htmlDecorator = new HtmlDecorator();
		final List<String> responseList = new ArrayList<>();
		responseList.add(htmlDecorator.h1("CALLS"));

		final List<String> calls = Arrays.asList("status", "fetchStockValues", "test", "crud");

		responseList.addAll(calls.stream().map(c -> htmlDecorator.a(c)).collect(Collectors.toList()));

		return htmlDecorator.decorateList(responseList);
	}

	@GetMapping("/test")
	public Object getTest(@RequestParam(required = false) String param) {
		if ("response".equalsIgnoreCase(param)) {
			return Response.SC_OK;
		}
		if ("wsresponse".equalsIgnoreCase(param)) {
			final WsResponse wsResponse = new WsResponse();
			wsResponse.setMessage("param: " + param);
			return wsResponse;
		}
		if ("wsbeanresponse".equals(param)) {
			throw new BaseException("not implemented yet");
		}
		return "test, param: " + param;
	}

	@GetMapping("/login")
	public Object login(@RequestParam String username, @RequestParam String password) {
		return getCredentialsComponent().login(username, password);
	}

	@GetMapping("/adduser")
	public Object addUser(@RequestParam String username, @RequestParam String password,
			@RequestParam(name = "mandant") String mandantName, String token) {
		final Optional<Mandant> loadBy = mandantService.loadBy(mandantName);
		final Mandant mandant = loadBy.get();
		User newUser = new User(mandant);
		newUser.setUsername(username);
		newUser.setPassword(password);

		userService.save(newUser);

		return "OK";
	}

	@GetMapping("/addmandant")
	public Object addMandant(@RequestParam String token, @RequestParam(name = "mandant") String mandantName) {
		final Optional<Mandant> loadBy = mandantService.loadBy(mandantName);
		if (loadBy.isPresent()) {
			return "a mandant whith the given name already exists: " + mandantName;
		}

		final Mandant mandant = new Mandant();

		mandant.setName(mandantName);
		mandantService.save(mandant);
		return "OK";
	}

}
