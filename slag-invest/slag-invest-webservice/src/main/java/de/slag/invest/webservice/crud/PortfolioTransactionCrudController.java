package de.slag.invest.webservice.crud;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.slag.invest.model.Mandant;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.service.PortfolioTransactionService;
import de.slag.invest.webcommon.mapping.ValueMappingRunner;
import de.slag.invest.webcommon.mapping.VersaValueMappingRunner;
import de.slag.invest.webcommon.model.CommonDto;
import de.slag.invest.webcommon.model.DtoType;

@RestController
@RequestMapping("/portfoliotransaction")
public class PortfolioTransactionCrudController extends AbstractIwsCrudController<CommonDto> {

	@Resource
	private PortfolioTransactionService portfolioTransactionService;

	@Override
	protected Long create0(Mandant mandant) {
		final PortfolioTransaction create = portfolioTransactionService.create(mandant);
		portfolioTransactionService.save(create);
		return create.getId();
	}

	@Override
	protected CommonDto load0(long id, Mandant mandant) {
		final PortfolioTransaction t = portfolioTransactionService.loadById(id);
		if (!t.getMandant().equals(mandant)) {
			throw new RuntimeException();
		}
		final CommonDto dto = createDto();
		final ValueMappingRunner valueMappingRunner = new ValueMappingRunner(t, dto);
		valueMappingRunner.prepare();
		valueMappingRunner.run();
		return dto;
	}

	@Override
	protected void save0(CommonDto t, Mandant mandant) {
		final PortfolioTransaction transaction = portfolioTransactionService.loadById(t.getId());
		final VersaValueMappingRunner runner = new VersaValueMappingRunner(t, transaction);
		runner.prepare();
		runner.run();		
		portfolioTransactionService.save(transaction);

	}

	@Override
	protected void delete0(long id, Mandant mandant) {
		portfolioTransactionService.deleteBy(id);
	}

	private CommonDto createDto() {
		final CommonDto dto = new CommonDto();
		dto.setType(DtoType.PORTFOLIO_TRANSACTION);
		return dto;
	}

}
