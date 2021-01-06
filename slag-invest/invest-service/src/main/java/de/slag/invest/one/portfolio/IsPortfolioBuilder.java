package de.slag.invest.one.portfolio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.money.MonetaryAmount;

import de.slag.common.base.pattern.Builder;
import de.slag.common.util.CurrencyUtils;
import de.slag.invest.one.model.IsPortfolioPoint;
import de.slag.invest.one.model.IsSecurityPoint;
import de.slag.invest.one.model.IsSecurityPointIdentifier;
import de.slag.invest.one.model.IsSecurityPosition;

public class IsPortfolioBuilder implements Builder<IsPortfolioPoint> {

	private final Map<String, Integer> portfolioContent = new HashMap<>();

	private IsSecurityPointProvider securityPointProvider;

	private List<String> errors = new ArrayList<>();

	private final List<IsSecurityPosition> positions = new ArrayList<>();

	private final LocalDate date;

	public IsPortfolioBuilder(IsSecurityPointProvider securityPointProvider, Map<String, Integer> portfolioContent,
			LocalDate date) {
		super();
		this.securityPointProvider = securityPointProvider;
		this.portfolioContent.putAll(portfolioContent);
		this.date = date;
	}

	@Override
	public IsPortfolioPoint build() throws Exception {
		portfolioContent.keySet().forEach(isinWkn -> build(isinWkn));

		if (isErroneous()) {
			throw new Exception("could not build:" + portfolioContent);
		}
		MonetaryAmount portfolioValue = CurrencyUtils.newAmount();
		for (IsSecurityPosition p : positions) {
			portfolioValue = portfolioValue.add(p.getTotalValue());
		}

		return new IsPortfolioPoint(positions, date, portfolioValue);
	}

	private boolean isErroneous() {
		return !errors.isEmpty();
	}

	private void build(String isinWkn) {
		final Optional<IsSecurityPoint> securityOptional = securityPointProvider.apply(new IsSecurityPointIdentifier() {

			@Override
			public String getIsinWkn() {
				return isinWkn;
			}

			@Override
			public LocalDate getDate() {
				return LocalDate.now();
			}
		});
		if (securityOptional.isEmpty()) {
			errors.add("not found: " + isinWkn);
			return;
		}
		final IsSecurityPoint invSecurityPoint = securityOptional.get();
		final Integer count = portfolioContent.get(isinWkn);

		final MonetaryAmount pointAmount = invSecurityPoint.getPointAmount();
		final MonetaryAmount totalValue = CurrencyUtils.mutiply(pointAmount, count);

		positions.add(new IsSecurityPosition(invSecurityPoint, count, totalValue));

	}
}
