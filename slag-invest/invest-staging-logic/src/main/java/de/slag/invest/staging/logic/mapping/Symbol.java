package de.slag.invest.staging.logic.mapping;

public class Symbol {
	
	public static Symbol of(String sybmol) {
		return new Symbol(sybmol);
	}

	private String sybmol;

	private Symbol(String sybmol) {
		this.sybmol = sybmol;
	}

	@Override
	public String toString() {
		return sybmol;
	}

	public String getValue() {
		return sybmol;
	}
	
	

}
