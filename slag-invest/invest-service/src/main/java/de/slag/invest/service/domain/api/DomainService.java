package de.slag.invest.service.domain.api;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import de.slag.common.model.EntityBean;

public interface DomainService<E extends EntityBean> {
	
	void save(E e);
	
	public void saveBean(Object o);
	
	Optional<E> load(Long id);
	
	void delete(E e);

	Collection<E> findBy(Predicate<E> filter);
	
	Optional<E> loadBy(Predicate<E> filter);
	
	E create();
	
}
