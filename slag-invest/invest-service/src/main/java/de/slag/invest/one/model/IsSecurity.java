package de.slag.invest.one.model;

public class IsSecurity {

	private final String wknIsin;

	private final String description;

	public IsSecurity(String wknIsin, String description) {
		super();
		this.wknIsin = wknIsin;
		this.description = description;
	}

	public String getWknIsin() {
		return wknIsin;
	}

	public String getDescription() {
		return description;
	}

}
