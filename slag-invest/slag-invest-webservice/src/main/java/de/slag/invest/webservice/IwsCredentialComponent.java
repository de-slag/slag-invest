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
public class IwsCredentialComponent {

	private static final long TOKEN_TIMEOUT_IN_MILLISECONDS = 15000;

	@Resource
	private UserService userService;

	private Map<String, String> users = new HashMap<>();

	private Map<IwsCredentialToken, Long> tokens = new HashMap<>();

	private Map<String, IwsCredentialToken> userTokens = new HashMap<>();

	@PostConstruct
	public void setUp() {
		users.put("test", "test");
	}

	@Deprecated
	public void logout(String tokenString) {
		tokens.remove(IwsCredentialToken.of(tokenString));
	}
	
	public void logout(IwsCredentialToken token) {
		tokens.remove(token);
	}

	public String login(String username, String password) {
		if (!users.containsKey(username)) {
			return null;
		}
		final IwsCredentialToken token = token();
		userTokens.put(username, token);
		useToken(token);
		return token.getTokenString();
	}
	
	@Deprecated
	public void useToken(String tokenString) {
		useToken(IwsCredentialToken.of(tokenString));
	}

	public void useToken(IwsCredentialToken token) {
		if (!isValid(token)) {
			throw new BaseException("token invalid: " + token);
		}
		tokens.put(token, System.currentTimeMillis() + TOKEN_TIMEOUT_IN_MILLISECONDS);
	}

	private IwsCredentialToken token() {
		return IwsCredentialToken.of(String.valueOf(System.nanoTime()));
	}

	public boolean isValid(IwsCredentialToken token) {
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
