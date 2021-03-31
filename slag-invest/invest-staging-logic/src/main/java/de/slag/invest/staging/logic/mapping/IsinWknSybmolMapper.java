package de.slag.invest.staging.logic.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class IsinWknSybmolMapper implements Function<IsinWkn, Symbol> {

	private Map<String, String> isinWknSymbols = new HashMap<>();
	
	

	IsinWknSybmolMapper(Map<String, String> isinWknSymbols) {
		super();
		this.isinWknSymbols = isinWknSymbols;
	}



	@Override
	public Symbol apply(IsinWkn t) {
		return Symbol.of(isinWknSymbols.get(t.getValue()));
	}
}
