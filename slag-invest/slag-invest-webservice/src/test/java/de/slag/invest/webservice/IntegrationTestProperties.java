package de.slag.invest.webservice;

import java.util.HashMap;
import java.util.Map;

public class IntegrationTestProperties {

	private static final String _TOKEN = "_TOKEN";

	private Map<String, String> keyValues = new HashMap<>();

	public void putToken(String username, String token) {
		keyValues.put(username + _TOKEN, token);
	}

	public String getToken(String username) {
		return keyValues.get(username + _TOKEN);
	}

}
