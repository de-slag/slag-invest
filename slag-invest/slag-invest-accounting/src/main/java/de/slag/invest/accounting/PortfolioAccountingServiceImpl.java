package de.slag.invest.accounting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.slag.invest.model.Portfolio;
import de.slag.invest.model.PortfolioTransaction;

@Service
public class PortfolioAccountingServiceImpl implements PortfolioAccoutingService {

	public void account(Portfolio portfolio) {
		final List<PortfolioTransaction> transactions = new ArrayList<>(portfolio.getTransactions());
		
	}

}
