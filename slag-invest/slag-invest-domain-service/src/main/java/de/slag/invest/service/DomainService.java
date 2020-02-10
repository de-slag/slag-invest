package de.slag.invest.service;

import java.util.Collection;
import java.util.stream.Collectors;

import de.slag.invest.model.DomainBean;

public interface DomainService<T extends DomainBean> {
	
	void save(T bean);
	
	T loadById(Long id);
	
	void delete(T bean);
	
	default void deleteBy(Long id) {
		delete(loadById(id));
	}
	
	Collection<Long> findAllIds();
	
	default Collection<T> findAll() {
		return findAllIds().stream().map(id -> loadById(id)).collect(Collectors.toList());
	}

}
