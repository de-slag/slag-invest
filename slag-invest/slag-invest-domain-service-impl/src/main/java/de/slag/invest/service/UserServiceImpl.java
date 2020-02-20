package de.slag.invest.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.common.base.BaseException;
import de.slag.invest.dtomodel.UserDto;
import de.slag.invest.model.Mandant;
import de.slag.invest.model.PasswordHash;
import de.slag.invest.model.User;

@Service
public class UserServiceImpl extends AbstractDomainServiceImpl<User> implements UserService {

	private static final Log LOG = LogFactory.getLog(UserServiceImpl.class);

	@PostConstruct
	public void setUpSuperUser() {
		if (loadBy("sysadm", null).isPresent()) {
			LOG.info("user 'sysadm' already present");
			return;
		}
		final User user = new User(null);
		user.setUsername("sysadm");
		final PasswordHash hash = hash("adm_User");
		user.setPasswordHash(hash);
		save(user);
		LOG.info("user 'sysadm' created");

	}

	@Override
	protected Class<User> getType() {
		return User.class;
	}

	@Override
	public boolean isValid(String username, String password) {
		final Collection<User> allUsers = findAll();
		final Optional<User> findAny = allUsers.stream().filter(user -> user.getUsername().equals(username)).findAny();
		if (findAny.isEmpty()) {
			return false;
		}
		final User user = findAny.get();
		if (!isCorrect(password, user.getPasswordHash())) {
			return false;
		}
		return true;
	}

	private PasswordHash hash(String password) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new BaseException(e);
		}
		byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

		BigInteger number = new BigInteger(1, encodedhash);

		// Convert message digest into hex value
		StringBuilder hexString = new StringBuilder(number.toString(16));

		final String hash = new String(hexString);
		return new PasswordHash(hash);
	}

	private boolean isCorrect(String password, PasswordHash hash) {
		final PasswordHash hash2 = hash(password);
		return hash.equals(hash2);
	}

	@Override
	public void save(User bean) {
		if (StringUtils.isNoneEmpty(bean.getPassword())) {
			bean.setPasswordHash(hash(bean.getPassword()));
		}
		super.save(bean);
	}

	@Override
	public Optional<User> loadBy(String username, Mandant mandant) {
		return loadBy(u -> {
			if (!username.equals(u.getUsername())) {
				return false;
			}
			if (mandant == null) {
				return true;
			}
			final boolean equals = mandant.equals(u.getMandant());
			return equals;
		});
	}

	@Override
	public boolean isPasswordCorrect(User user, String password) {
		return isCorrect(password, user.getPasswordHash());
	}

	@Override
	public UserDto createDto(User user) {
		final UserDto dto = new UserDto();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		return dto;
	}
}
