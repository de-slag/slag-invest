package de.slag.invest.accounting;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import de.slag.common.testsupport.AbstractHibernateSupportedTest;
import de.slag.invest.model.DomainBean;
import de.slag.invest.model.Portfolio;
import de.slag.invest.model.PortfolioPosition;
import de.slag.invest.model.PortfolioTransaction;
import de.slag.invest.repo.UniversalRepo;
import de.slag.invest.service.PortfolioTransactionService;
import de.slag.invest.service.PortfolioTransactionServiceImpl;

public class AccountingIntegrationTest extends AbstractHibernateSupportedTest {

	@InjectMocks
	PortfolioAccountingServiceImpl portfolioAccountingServiceImpl;

	@InjectMocks
	PortfolioTransactionService portfolioTransactionService = new PortfolioTransactionServiceImpl();

	@Mock
	UniversalRepo universalRepo;

	@Override
	protected Collection<Class<?>> getRegisterClasses() {
		return Arrays.asList(Portfolio.class, PortfolioPosition.class, PortfolioTransaction.class);
	}

	@Before
	public void setUp() {
		DomainBean o;
	}

	@Test
	public void test() {
		Optional<Portfolio> loadById = hibernateSupport.loadById(1l, Portfolio.class);
		Portfolio portfolio = loadById.get();
		Collection<PortfolioTransaction> findBy = portfolioTransactionService.findBy(portfolio);
	}

}
