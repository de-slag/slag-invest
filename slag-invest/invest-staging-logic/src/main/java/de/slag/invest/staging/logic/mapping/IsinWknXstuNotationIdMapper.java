package de.slag.invest.staging.logic.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class IsinWknXstuNotationIdMapper implements Function<IsinWkn, XstuNotationId> {

	private Map<String, String> isinWknSymbols = new HashMap<>();
	
	

	IsinWknXstuNotationIdMapper(Map<String, String> isinWknSymbols) {
		super();
		this.isinWknSymbols = isinWknSymbols;
	}

	@Override
	public XstuNotationId apply(IsinWkn t) {
		return XstuNotationId.of(isinWknSymbols.get(t.getValue()));
	}
}
