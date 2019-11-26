package de.slag.invest.appservice.dataimport;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.common.reflect.engine.SimpleReflectionEngine;
import de.slag.invest.filerepo.PortfolioTransactionDto;
import de.slag.invest.filerepo.PortfolioTransactionFileRepo;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.model.PortfolioTransaction.PortfolioTransactionType;
import de.slag.invest.service.PortfolioService;
import de.slag.invest.service.PortfolioTransactionService;

@Service
public class PortfolioUpdateServiceImpl implements PortfolioUpdateService {
	
	@Resource
	private PortfolioService portfolioService;

	@Resource
	private PortfolioTransactionService portfolioTransactionService;

	@Resource
	private PortfolioTransactionFileRepo portfolioTransactionFileRepo;

	@Override
	public void updatePortfolios() {
		updateTransactions();	}

	private void updateTransactions() {
		final Collection<PortfolioTransactionDto> findAll = portfolioTransactionFileRepo.findAll();
		findAll.forEach(dto -> {
			PortfolioTransaction portfolioTransaction = new PortfolioTransaction();

			portfolioTransaction.setType(PortfolioTransactionType.valueOf(dto.getType().toUpperCase()));
			new SimpleReflectionEngine().mapValues(dto, portfolioTransaction, Arrays.asList("TYPE"));
			portfolioTransactionService.save(portfolioTransaction);
		});
	}

}
