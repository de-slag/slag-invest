package de.slag.invest.webservice;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class TestWebTargetUtils {

	public static WebTarget create(String url) {
		return ClientBuilder.newClient().target(url);
	}

}
