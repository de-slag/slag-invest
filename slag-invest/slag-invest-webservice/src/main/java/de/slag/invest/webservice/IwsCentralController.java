package de.slag.invest.webservice;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.catalina.connector.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.slag.common.base.AdmCache;
import de.slag.common.base.BaseException;
//import de.slag.invest.iface.av.api.StockValueDataImportService;
import de.slag.invest.model.Mandant;
import de.slag.invest.model.User;
import de.slag.invest.service.MandantService;
import de.slag.invest.service.PortfolioTransactionService;
import de.slag.invest.service.StockValueService;
import de.slag.invest.service.UserService;
import de.slag.invest.webcommon.WsResponse;
import de.slag.invest.webcommon.WsResponseBuilder;
import de.slag.invest.webcommon.WsStringResponse;
import de.slag.invest.webservice.response.StringWebserviceResponse2;
import de.slag.invest.webservice.response.WebserviceResponse2;

@RestController
public class IwsCentralController extends AbstractIwsController {

	private static final Log LOG = LogFactory.getLog(IwsCentralController.class);

	@Resource
	private AdmCache admCache;

	@Resource
	private PortfolioTransactionService portfolioTransactionService;

//	@Resource
//	private StockValueDataImportService stockValueDataImportService;

	@Resource
	private StockValueService stockValueService;

	@Resource
	private UserService userService;

	@Resource
	private MandantService mandantService;

	@GetMapping("/fetchStockValues")
	public Collection<String> getFetchStockValues() {
//		stockValueDataImportService.importData();

		final ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("function: FETCH STOCK VALUES");
		arrayList.add(String.format("result: success, %s", new Date()));
		return arrayList;
	}

	@GetMapping("/status")
	public WsStringResponse getStatus(@RequestParam String token) {
		if (!getCredentialsComponent().isValid(CredentialToken.of(token))) {
			return null;
		}

		final List<String> asList = new ArrayList<>();
		final long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
		final long now = System.currentTimeMillis();
		final Date uptime = new Date(now - startTime);
		asList.add("uptime: " + uptime);

		asList.add("portfolio transactions: " + portfolioTransactionService.findAllIds().size());
		asList.add("stock values: " + stockValueService.findAllIds().size());
		final Runtime runtime = Runtime.getRuntime();

		asList.add(String.format("mem: %s M (%s M)", (runtime.totalMemory() - runtime.freeMemory()) / (1000 * 1000),
				runtime.totalMemory() / 1000000));

		asList.add(String.format("current time: %s", LocalDateTime.now()));

		return (WsStringResponse) new WsResponseBuilder().withMessage("OK").withSuccess(true)
				.withValue(String.join("\n", asList)).build();
	}

	@GetMapping
	public String get() {
		return "slag-invest-webservice";
	}

	@GetMapping("/test")
	public Object getTest(@RequestParam(required = false) String param) {
		if ("response".equalsIgnoreCase(param)) {
			return Response.SC_OK;
		}
		if ("wsresponse".equalsIgnoreCase(param)) {
			final WebserviceResponse2 response = new WebserviceResponse2();
			response.setSuccessful(true);
			response.setMessage("param: " + param);
			return response;
		}
		return "test, param: " + param;
	}

	@GetMapping("/login")
	public WsStringResponse login(@RequestParam String username, @RequestParam String password,
			@RequestParam(name = "mandant", required = false) String mandantName) {

		final Mandant mandant;
		if (mandantName == null) {
			mandant = null;
		} else {
			final Optional<Mandant> loadBy = mandantService.loadBy(mandantName);
			if (loadBy.isEmpty()) {
				return null;
//				return new WsResponseBuilder().withSuccess(false).build();
			}
			mandant = loadBy.get();
		}
		final CredentialToken login = getCredentialsComponent().login(username, password, mandant);
		if (login == null) {
			return null;
//			return new WsResponseBuilder().withSuccess(false).build();
		}
		final WsStringResponse build = (WsStringResponse) new WsResponseBuilder().withValue(login.getTokenString())
				.withSuccess(true).withMessage("token created").build();
		LOG.info("value:" + build.getValue());
		return (WsStringResponse) build;
	}

	@GetMapping("/adduser")
	public Object addUser(@RequestParam String username, @RequestParam String password,
			@RequestParam(name = "mandant") String mandantName, String token) {

		final Optional<Mandant> loadBy = mandantService.loadBy(mandantName);
		final Mandant mandant = loadBy
				.orElseThrow(() -> new BaseException("no mandant found with name: " + mandantName));
		User newUser = new User(mandant);
		newUser.setUsername(username);
		newUser.setPassword(password);

		userService.save(newUser);

		assertNewUserOk(newUser.getId());

		final WebserviceResponse2 response = new WebserviceResponse2();
		response.setSuccessful(true);
		response.setMessage("adduser succsessful: " + username + ", mandant: " + mandantName);
		return response;
	}

	private void assertNewUserOk(Long id) {
		User loadById = userService.loadById(id);
		Objects.requireNonNull(loadById.getUsername(), "username not setted");
		Objects.requireNonNull(loadById.getMandant(), "mandant not setted");

	}

	@GetMapping("/addmandant")
	public WebserviceResponse2 addMandant(@RequestParam String token,
			@RequestParam(name = "mandant") String mandantName) {
		final Optional<Mandant> loadBy = mandantService.loadBy(mandantName);
		if (loadBy.isPresent()) {
			final WebserviceResponse2 response = new WebserviceResponse2();
			response.setSuccessful(false);
			response.setMessage("a mandant whith the given name already exists: " + mandantName);
			return response;
		}

		final Mandant mandant = new Mandant();

		mandant.setName(mandantName);
		mandantService.save(mandant);
		final WebserviceResponse2 response = new WebserviceResponse2();
		response.setSuccessful(true);
		response.setMessage(String.format("Mandant '%s' succesful added.", mandantName));
		return response;
	}

}
