package de.slag.invest.staging.logic.mapping;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class IsinWknOvNotationIdMapper extends AbstractMapper<IsinWkn, OvNotationId> implements Function<IsinWkn, OvNotationId> {

	IsinWknOvNotationIdMapper(Map<String, String> isinWknOvNotationIds) {
		super(isinWknOvNotationIds);
	}
	
	IsinWknOvNotationIdMapper(Function<String, Optional<String>> provider) {
		super(provider);
	}

	@Override
	protected OvNotationId of(String value) {
		return OvNotationId.of(value);
	}

}
