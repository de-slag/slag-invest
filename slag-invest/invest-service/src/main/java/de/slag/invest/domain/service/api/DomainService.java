package de.slag.invest.domain.service.api;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import de.slag.common.model.EntityBean;

public interface DomainService<E extends EntityBean> {
	
	void save(E e);
	
	Optional<E> load(Long id);
	
	void delete(E e);

	Collection<E> findBy(Predicate<E> filter);
	
	Optional<E> loadBy(Predicate<E> filter);

}
