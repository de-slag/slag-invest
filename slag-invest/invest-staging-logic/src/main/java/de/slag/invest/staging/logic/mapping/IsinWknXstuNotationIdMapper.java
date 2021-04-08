package de.slag.invest.staging.logic.mapping;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class IsinWknXstuNotationIdMapper extends AbstractMapper<IsinWkn, XstuNotationId>
		implements Function<IsinWkn, XstuNotationId> {

	IsinWknXstuNotationIdMapper(Map<String, String> isinWknSymbols) {
		super(isinWknSymbols);
	}

	IsinWknXstuNotationIdMapper(Function<String, Optional<String>> provider) {
		super(provider);
	}

	@Override
	protected XstuNotationId of(String value) {
		return XstuNotationId.of(value);
	}
}
