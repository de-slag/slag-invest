package de.slag.invest.staging.logic.mapping;

public class XstuNotationId {

	public static XstuNotationId of(String xstuNotationId) {
		return new XstuNotationId(xstuNotationId);
	}

	private String xstuNotationId;

	private XstuNotationId(String xstuNotationId) {
		this.xstuNotationId = xstuNotationId;
	}

	@Override
	public String toString() {
		return xstuNotationId;
	}

	public String getValue() {
		return xstuNotationId;
	}
}
