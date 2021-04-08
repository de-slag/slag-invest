package de.slag.invest.staging.logic.mapping;

import java.util.HashMap;
import java.util.Map;

public class IsinWkn implements Mappable {


	private static final Map<String, IsinWkn> ALL_INSTANCES = new HashMap<>();

	public static IsinWkn of(String isinWkn) {
		if (!ALL_INSTANCES.containsKey(isinWkn)) {
			ALL_INSTANCES.put(isinWkn, new IsinWkn(isinWkn));
		}
		return ALL_INSTANCES.get(isinWkn);
	}

	private String isinWkn;

	private IsinWkn(String isinWkn) {
		this.isinWkn = isinWkn;
	}

	@Override
	public String toString() {
		return isinWkn;
	}

	public String getValue() {
		return isinWkn;
	}
}
