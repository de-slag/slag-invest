package de.slag.invest.webservice.crud;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.slag.invest.model.Mandant;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.service.PortfolioTransactionService;
import de.slag.invest.webcommon.model.CommonDto;
import de.slag.invest.webcommon.model.DtoType;

@RestController
@RequestMapping("/portfoliotransaction")
public class PortfolioTransactionCrudController extends AbstractIwsCrudController<CommonDto> {

	private static final Function<PortfolioTransaction, CommonDto> TRANSACTION_TO_DTO = t -> {
		final CommonDto dto = new CommonDto();
		dto.setType(DtoType.PORTFOLIO_TRANSACTION);
		return dto;
	};

	private static final BiConsumer<CommonDto, PortfolioTransaction> DTO_TO_TRANSACTION = (d, p) -> {

	};

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
		return TRANSACTION_TO_DTO.apply(portfolioTransactionService.loadById(id));
	}

	@Override
	protected void save0(CommonDto t, Mandant mandant) {
		final PortfolioTransaction transaction = portfolioTransactionService.loadById(t.getId());
		DTO_TO_TRANSACTION.accept(t, transaction);
		portfolioTransactionService.save(transaction);

	}

	@Override
	protected void delete0(long id, Mandant mandant) {
		portfolioTransactionService.deleteBy(id);
	}

}
