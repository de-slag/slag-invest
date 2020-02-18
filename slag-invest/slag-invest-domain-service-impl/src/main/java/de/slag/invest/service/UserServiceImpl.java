package de.slag.invest.service;

import java.util.Collection;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import de.slag.invest.model.User;

@Service
public class UserServiceImpl extends AbstractDomainServiceImpl<User> implements UserService {

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

	private String hash(String password) {
		return password;
	}

	private boolean isCorrect(String password, String hash) {
		return hash.equals(hash(password));
	}
	
	@Override
	public void save(User bean) {
		if(StringUtils.isNoneEmpty(bean.getPassword())) {
			bean.setPasswordHash(hash(bean.getPassword()));
		}		
		super.save(bean); 
	}

}
