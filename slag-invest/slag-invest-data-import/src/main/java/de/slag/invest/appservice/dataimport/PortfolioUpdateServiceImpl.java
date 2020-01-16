package de.slag.invest.appservice.dataimport;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.invest.filerepo.PortfolioTransactionFileRepo;
import de.slag.invest.model.Portfolio;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.service.PortfolioService;
import de.slag.invest.service.PortfolioTransactionService;

@Service
public class PortfolioUpdateServiceImpl implements PortfolioUpdateService {

	private static final Log LOG = LogFactory.getLog(PortfolioUpdateServiceImpl.class);

	@Resource
	private PortfolioService portfolioService;

	@Resource
	private PortfolioTransactionService portfolioTransactionService;

	@Resource
	private PortfolioTransactionFileRepo portfolioTransactionFileRepo;

	@Override
	public void updatePortfolios() {
		updateTransactions();
	}

	private void updateTransactions() {
		final Collection<Long> portfolioTransactionIds = portfolioTransactionService.findAllIds();
		portfolioTransactionIds.forEach(id -> updatePortfolio(id));
	}

	private void updatePortfolio(Long id) {
		final PortfolioTransaction transaction = portfolioTransactionService.loadById(id);
		final String portfolioNumber = transaction.getPortfolioNumber();
		final Optional<Portfolio> loadBy = portfolioService.loadBy(portfolioNumber);
		if (!loadBy.isEmpty()) {
			LOG.info(String.format("portfolio with number '%s' already exists", portfolioNumber));
			return;
		}
		final Portfolio portfolio = new Portfolio();
		portfolio.setPortfolioNumber(portfolioNumber);
		portfolioService.save(portfolio);

	}

}
