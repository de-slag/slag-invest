package de.slag.invest.one.portfolio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.slag.common.base.pattern.Builder;
import de.slag.invest.one.model.IsPortfolio;
import de.slag.invest.one.model.IsSecurity;
import de.slag.invest.one.model.IsSecurityPosition;

public class IsPortfolioBuilder implements Builder<IsPortfolio> {

	private final Map<String, Integer> portfolioContent = new HashMap<>();

	private IsSecurityProvider securityProvider;

	private List<String> errors = new ArrayList<>();

	private final List<IsSecurityPosition> positions = new ArrayList<>();

	private IsPortfolioBuilder(IsSecurityProvider securityProvider, Map<String, Integer> portfolioContent) {
		super();
		this.securityProvider = securityProvider;
		this.portfolioContent.putAll(portfolioContent);
	}

	@Override
	public IsPortfolio build() throws Exception {
		portfolioContent.keySet().forEach(isinWkn -> build(isinWkn));

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

	private void build(String isinWkn) {
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
