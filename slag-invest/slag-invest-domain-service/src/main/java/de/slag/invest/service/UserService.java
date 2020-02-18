package de.slag.invest.service;

import de.slag.invest.model.User;

public interface UserService extends DomainService<User> {

	boolean isValid(String username, String password);

}
