package de.slag.invest.staging.logic.mapping;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class IsinWknSybmolMapper extends AbstractMapper<IsinWkn, Symbol> implements Function<IsinWkn, Symbol> {

	IsinWknSybmolMapper(Map<String, String> map) {
		super(map);
	}	

	IsinWknSybmolMapper(Function<String, Optional<String>> provider) {
		super(provider);	
	}

	@Override
	protected Symbol of(String value) {
		return Symbol.of(value);
	}
}
