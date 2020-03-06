package de.slag.invest.webcommon;

import javax.ws.rs.core.Response;

public class ResponseSupport {

	private final Response response;

	public ResponseSupport(Response response) {
		super();
		this.response = response;
	}

	public String getStringEntity() {
		return response.readEntity(String.class);
	}

	public Long getLongEntity() {
		return Long.valueOf(response.readEntity(String.class));
	}

}
