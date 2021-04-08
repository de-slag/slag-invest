package de.slag.invest.staging.logic.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractMapper<X extends Mappable, Y extends Mappable> implements Function<X, Y> {

	private Map<String, String> map = new HashMap<>();

	private Function<String, Optional<String>> provider;

	public AbstractMapper(Map<String, String> map) {
		this.map.putAll(map);
	}

	public AbstractMapper(Function<String, Optional<String>> provider) {
		this.provider = provider;
	}

	@Override
	public Y apply(X x) {
		String xValue = x.getValue();
		if (!map.containsKey(xValue)) {
			fetchFromProvider(xValue);
		}

		if (!map.containsKey(xValue)) {
			throw new RuntimeException("not found: " + xValue);
		}

		return of(map.get(xValue));
	}

	private void fetchFromProvider(String xValue) {
		if (provider == null) {
			return;
		}

		final Optional<String> yValueOptional = provider.apply(xValue);
		if (yValueOptional.isEmpty()) {
			return;
		}
		map.put(xValue, yValueOptional.get());

	}

	protected abstract Y of(String value);

}
