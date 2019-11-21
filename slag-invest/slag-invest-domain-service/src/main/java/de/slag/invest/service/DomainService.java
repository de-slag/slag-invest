package de.slag.invest.service;

import de.slag.invest.model.DomainBean;

public interface DomainService<T extends DomainBean> {
	
	void save(T bean);
	
	T loadById(Long id);
	
	void delete(T bean);

}
