package de.slag.invest.staging.logic.mapping;

public class IsinWkn implements Mappable {

	public static IsinWkn of(String isinWkn) {
		return new IsinWkn(isinWkn);
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
