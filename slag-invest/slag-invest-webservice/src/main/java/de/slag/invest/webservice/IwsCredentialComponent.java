package de.slag.invest.webservice;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import de.slag.common.base.BaseException;
import de.slag.common.core.SlagDevelopment;
import de.slag.invest.dtomodel.UserDto;
import de.slag.invest.model.Mandant;
import de.slag.invest.model.User;
import de.slag.invest.service.UserService;

@Component
public class IwsCredentialComponent {

	private static final Log LOG = LogFactory.getLog(IwsCredentialComponent.class);

	private static final long TOKEN_TIMEOUT_IN_MILLISECONDS = 15000;

	@Resource
	private UserService userService;

	private Map<CredentialToken, Long> tokens = new HashMap<>();

	private Map<UserDto, CredentialToken> userTokens = new HashMap<>();

	@Deprecated
	public void logout(String tokenString) {
		tokens.remove(CredentialToken.of(tokenString));
	}

	public void logout(CredentialToken token) {
		tokens.remove(token);
	}

	@Deprecated
	public String login(String username, String password) {
		throw new BaseException("deprecated");
	}

	public CredentialToken login(String username, String password, Mandant mandant) {
		final User user = userService.loadBy(username, mandant).orElseThrow(() -> new BaseException(
				String.format("user not found '%s', mandant '%s'", username, mandant.getName())));

		if (!userService.isPasswordCorrect(user, password)) {
			return null;
		}
		
		final UserDto userDto = userService.createDto(user);

		final CredentialToken token = token();
		userTokens.put(userDto, token);
		tokens.put(token, System.currentTimeMillis());
		useToken(token);
		return token;
	}

	@Deprecated
	public void useToken(String tokenString) {
		useToken(CredentialToken.of(tokenString));
	}

	public void useToken(CredentialToken token) {
		if (!isValid(token)) {
			throw new BaseException("token invalid: " + token);
		}
		tokens.put(token, System.currentTimeMillis() + TOKEN_TIMEOUT_IN_MILLISECONDS);
	}

	private CredentialToken token() {
		return CredentialToken.of(String.valueOf(System.nanoTime()));
	}

	public boolean isValid(CredentialToken token) {
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
