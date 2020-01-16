package de.slag.invest.facades;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import de.slag.invest.model.Portfolio;
import de.slag.invest.model.PortfolioPosition;
import de.slag.invest.model.PortfolioTransaction;

public class PortfolioFacade {

	private final Portfolio portfolio;

	private final Collection<PortfolioTransaction> transactions = new ArrayList<>();

	private final Collection<PortfolioPosition> positions = new ArrayList<>();

	public PortfolioFacade(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public void evaluate() {
		assert portfolio != null;
		final String portfolioNumber = portfolio.getPortfolioNumber();
		assert portfolioNumber != null;
		transactions.forEach(t -> t.setPortfolioNumber(portfolioNumber));
		positions.forEach(p -> p.setPortfolioNumber(portfolioNumber));
	}

	public void validate() {
		assert portfolio != null;
		final String portfolioNumber = portfolio.getPortfolioNumber();
		transactions.forEach(t -> {
			final String transactionPortfolioNumber = t.getPortfolioNumber();
			if (StringUtils.isEmpty(transactionPortfolioNumber)) {
				return;
			}
			assert portfolioNumber.equals(transactionPortfolioNumber);
		});
		positions.forEach(p -> {
			final String positionPorfolioNumber = p.getPortfolioNumber();
			if (StringUtils.isEmpty(positionPorfolioNumber)) {
				return;
			}
			assert portfolioNumber.equals(positionPorfolioNumber);
		});
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public Collection<PortfolioTransaction> getTransactions() {
		return transactions;
	}

	public Collection<PortfolioPosition> getPositions() {
		return positions;
	}
}
