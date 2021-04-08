package de.slag.invest.staging.logic.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class IsinWknOvNotationIdMapper implements Function<IsinWkn, OvNotationId> {

	private Map<String, String> isinWknOvNotationIds = new HashMap<>();

	IsinWknOvNotationIdMapper(Map<String, String> isinWknOvNotationIds) {
		super();
		this.isinWknOvNotationIds = isinWknOvNotationIds;
	}

	@Override
	public OvNotationId apply(IsinWkn isinWkn) {
		return OvNotationId.of(isinWknOvNotationIds.get(isinWkn.getValue()));
	}

}
