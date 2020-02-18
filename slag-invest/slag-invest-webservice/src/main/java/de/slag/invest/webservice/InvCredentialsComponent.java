package de.slag.invest.webservice;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import de.slag.common.base.BaseException;
import de.slag.common.core.SlagDevelopment;
import de.slag.invest.service.UserService;

@Component
public class InvCredentialsComponent {

	private static final long TOKEN_TIMEOUT = 15000;

	@Resource
	private UserService userService;

	private Map<String, String> users = new HashMap<>();

	private Map<String, Long> tokens = new HashMap<>();

	private Map<String, String> userTokens = new HashMap<>();

	@PostConstruct
	public void setUp() {
		users.put("test", "test");
	}

	public void logout(String token) {
		tokens.remove(token);
	}

	public String login(String username, String password) {
		if (!users.containsKey(username)) {
			return null;
		}
		final String token = token();
		userTokens.put(username, token);
		useToken(token);
		return token;
	}

	public void useToken(String token) {
		if (!isValid(token)) {
			throw new BaseException("token invalid: " + token);
		}
		tokens.put(token, System.currentTimeMillis() + TOKEN_TIMEOUT);
	}

	private String token() {
		return String.valueOf(System.nanoTime());
	}

	public boolean isValid(String token) {
		if (token == null) {
			return false;
		}

		// TODO remove block before production
		if (SlagDevelopment.isEnabled()) {
			return true;
		}

		if (!tokens.containsKey(token)) {
			return false;
		}
		final Long validUntil = tokens.get(token);
		if (System.currentTimeMillis() > validUntil) {
			return false;
		}
		return true;
	}
}
