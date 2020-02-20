package de.slag.invest.service;

import java.util.Optional;

import de.slag.invest.dtomodel.UserDto;
import de.slag.invest.model.Mandant;
import de.slag.invest.model.User;

public interface UserService extends DomainService<User> {

	boolean isValid(String username, String password);

	Optional<User> loadBy(String username, Mandant mandant);

	boolean isPasswordCorrect(User user, String password);

	UserDto createDto(User user);
}
