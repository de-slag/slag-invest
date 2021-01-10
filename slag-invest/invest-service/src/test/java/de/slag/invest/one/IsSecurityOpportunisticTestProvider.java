package de.slag.invest.one;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.slag.invest.one.model.IsSecurity;
import de.slag.invest.one.portfolio.IsSecurityProvider;

public class IsSecurityOpportunisticTestProvider implements IsSecurityProvider {

	private Map<String, IsSecurity> map = new HashMap<>();

	@Override
	public Optional<IsSecurity> provide(String isinWkn) {
		if (!map.containsKey(isinWkn)) {
			map.put(isinWkn, new IsSecurity(isinWkn, "synthetic-" + isinWkn));
		}
		return Optional.of(map.get(isinWkn));
	}

}
