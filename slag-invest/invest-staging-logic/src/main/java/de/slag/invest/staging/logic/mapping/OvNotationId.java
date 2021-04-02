package de.slag.invest.staging.logic.mapping;

public class OvNotationId {

	public static OvNotationId of(String xstuNotationId) {
		return new OvNotationId(xstuNotationId);
	}

	private String xstuNotationId;

	private OvNotationId(String xstuNotationId) {
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
