package de.slag.invest.webservice.crud;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.slag.invest.service.DomainService;
import de.slag.invest.service.PortfolioTransactionService;
import de.slag.invest.service.StockValueService;
import de.slag.invest.webservice.response.HtmlDecorator;
import de.slag.invest.webservice.response.SimpleHtmlResponse;

@RestController
@RequestMapping("/crud")
public class InvestCrudController {

	@Resource
	private PortfolioTransactionService portfolioTransactionService;

	@Resource
	private StockValueService stockValueService;

	@GetMapping
	public String getHelp() {
		final HtmlDecorator decorator = new HtmlDecorator();
		final List<String> asList = Arrays.asList("crud/read", "crud/save", "crud/delete");
		return new SimpleHtmlResponse("CRUD-Interface", "calls:",
				asList.stream().map(e -> decorator.a(e)).collect(Collectors.toList())).toString();
	}

	@GetMapping("/read")
	public Object read(@RequestParam String type, @RequestParam(required = false) Long id) {
		final EntityType entityType = EntityType.valueOf(type);
		final DomainService<?> determineDomainService = determineDomainService(entityType);
		return load(determineDomainService, id);
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

	private DomainService<?> determineDomainService(EntityType type) {
		switch (type) {
		case PORTFOLIO_TRANSACTION:
			return portfolioTransactionService;
		case STOCK_VALUE:
			return stockValueService;
		default:
			throw new IllegalStateException("not supported:" + type);
		}
	}

	private Object load(DomainService<?> domainService, Long id) {
		if (id == null) {
			return domainService.findAll();
		}
		return domainService.loadById(id);
	}

}
