package de.slag.invest.one.portfolio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.slag.invest.one.model.IsPortfolio;
import de.slag.invest.one.model.IsSecurity;
import de.slag.invest.one.model.IsSecurityPosition;

public class IsPortfolioResolver implements IsResolver<IsPortfolio> {

	private final Map<String, Integer> portfolioContent = new HashMap<>();

	private IsSecurityProvider securityProvider;

	private List<String> errors = new ArrayList<>();

	private final List<IsSecurityPosition> positions = new ArrayList<>();

	private IsPortfolioResolver(IsSecurityProvider securityProvider, Map<String, Integer> portfolioContent) {
		super();
		this.securityProvider = securityProvider;
		this.portfolioContent.putAll(portfolioContent);
	}

	public IsPortfolio resolve() {
		portfolioContent.keySet().forEach(isinWkn -> resolve(isinWkn));

		if (isErroneous()) {
			throw new RuntimeException();
		}
		final IsPortfolio portfolio = new IsPortfolio();
		portfolio.getSecurityPositions().addAll(positions);
		return portfolio;
	}

	private boolean isErroneous() {
		return !errors.isEmpty();
	}

	private void resolve(String isinWkn) {
		final Optional<IsSecurity> securityOptional = securityProvider.apply(isinWkn);
		if (securityOptional.isEmpty()) {
			errors.add("not found: " + isinWkn);
			return;
		}
		final IsSecurity invSecurity = securityOptional.get();
		final Integer count = portfolioContent.get(isinWkn);
		final BigDecimal totalValue = invSecurity.getPrice().multiply(BigDecimal.valueOf(count));

		positions.add(new IsSecurityPosition(invSecurity, count, totalValue));

	}

}
