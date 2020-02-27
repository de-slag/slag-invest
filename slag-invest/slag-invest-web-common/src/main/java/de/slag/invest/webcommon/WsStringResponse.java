package de.slag.invest.webcommon;

public class WsStringResponse extends WsResponse<String> {

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
