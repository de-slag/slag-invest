package de.slag.invest.webservice.crud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.slag.common.base.BaseException;
import de.slag.invest.model.DomainBean;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.service.DomainService;
import de.slag.invest.service.PortfolioTransactionService;
import de.slag.invest.service.StockValueService;
import de.slag.invest.webservice.InvCredentialsComponent;
import de.slag.invest.webservice.WebserviceException;
import de.slag.invest.webservice.response.HtmlDecorator;
import de.slag.invest.webservice.response.SimpleHtmlResponse;
import de.slag.invest.webservice.response.SimpleWebserviceResponse;

@RestController
@RequestMapping("/crud")
public class InvestCrudController {
	
	private static final Log LOG = LogFactory.getLog(InvestCrudController.class);

	@Resource
	private PortfolioTransactionService portfolioTransactionService;

	@Resource
	private StockValueService stockValueService;

	@Resource
	private InvCredentialsComponent invCredentialsComponent;

	@GetMapping
	public String getHelp() {
		final HtmlDecorator decorator = new HtmlDecorator();
		final List<String> asList = Arrays.asList("crud/read", "crud/save", "crud/delete");
		return new SimpleHtmlResponse("CRUD-Interface", "calls:",
				asList.stream().map(e -> decorator.a(e)).collect(Collectors.toList())).toString();
	}

	@GetMapping("/read")
	public Object read(@RequestParam String type, @RequestParam(required = false) Long id, @RequestParam String token) {
		assertValidToken(token);

		final EntityType entityType = EntityType.valueOf(type);
		try {
			return readInternal(type, id, entityType);
		} catch (WebserviceException e) {
			return null;
		}
	}

	private Object readInternal(String type, Long id, final EntityType entityType) throws WebserviceException {
		Collection<DomainBean> responseCollection = determineBeansToResponse(id, entityType);

		switch (entityType) {
		case PORTFOLIO_TRANSACTION:
			return new PortfolioTransactionWebserviceResponse(transform(responseCollection, PortfolioTransaction.class));
		default:
			return new SimpleWebserviceResponse(true, "not supported: " + type);
		}
	}

	private <T> Collection<T> transform(Collection<DomainBean> beans, Class<T> type) throws WebserviceException {
		final long count = beans.stream().filter(d -> type.isAssignableFrom(d.getClass())).count();
		if (count != beans.size()) {
			throw new WebserviceException("not all elements of type: " + type);
		}
		return beans.stream().map(d -> type.cast(d)).collect(Collectors.toList());
	}

	private Collection<DomainBean> determineBeansToResponse(Long id, final EntityType entityType) {
		final DomainService<? extends DomainBean> domainService = determineDomainService(entityType);

		Collection<DomainBean> responseCollection = new ArrayList<>();
		if (id == null) {
			responseCollection.addAll(domainService.findAll());
		} else {
			responseCollection.add(domainService.loadById(id));
		}
		return responseCollection;
	}

	private void assertValidToken(String token) {
		if (!invCredentialsComponent.isValid(token)) {
			throw new BaseException("token not valid");
		}
	}

	@GetMapping("/save")
	public Object save(@RequestParam String type, @RequestParam(required = false) Long id,
			@RequestParam(required = false) String properties) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@GetMapping("/delete")
	public Object delete(@RequestParam String type, @RequestParam Long id) {
		final EntityType entityType = EntityType.valueOf(type);
		determineDomainService(entityType).deleteBy(id);
		return "deleted";
	}

	private DomainService<? extends DomainBean> determineDomainService(EntityType type) {
		switch (type) {
		case PORTFOLIO_TRANSACTION:
			return portfolioTransactionService;
		case STOCK_VALUE:
			return stockValueService;
		default:
			throw new IllegalStateException("not supported:" + type);
		}
	}

}
