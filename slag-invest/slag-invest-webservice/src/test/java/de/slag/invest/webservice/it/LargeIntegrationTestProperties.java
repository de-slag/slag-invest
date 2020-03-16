package de.slag.invest.webservice.it;

import java.util.HashMap;
import java.util.Map;

public class LargeIntegrationTestProperties {

	private static final String INTEGRATION_TEST_SUPER_USER = "integration-test-super-user";

	private static final String INTEGRATION_TEST_MANDANT = "integration-test-mandant";

	private static final String ADM_USER = "adm-user";

	private static final String _TOKEN = "_TOKEN";

	private final String runId;

	public LargeIntegrationTestProperties(long runId) {
		this(String.valueOf(runId));
	}

	public LargeIntegrationTestProperties(String runId) {
		super();
		this.runId = runId;
	}

	private Map<String, String> keyValues = new HashMap<>();

	public void putToken(String username, String token) {
		keyValues.put(username + _TOKEN, token);
	}

	public String getToken(String username) {
		return keyValues.get(username + _TOKEN);
	}

	public String getAdmUserName() {
		return ADM_USER;
	}

	public String getMandantName() {
		return withRunId(INTEGRATION_TEST_MANDANT);
	}

	public String getMandantSuperUserName() {
		return withRunId(INTEGRATION_TEST_SUPER_USER);
	}

	private String withRunId(String string) {
		return string + "_" + runId;
	}

	public String getPassword(String user) {
		return user + "_password";
	}

}
