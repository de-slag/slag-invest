package de.slag.invest.service;

import javax.annotation.Resource;

import de.slag.invest.model.DomainBean;
import de.slag.invest.repo.UniversalRepo;

public abstract class AbstractDomainServiceImpl<T extends DomainBean> implements DomainService<T> {

	@Resource
	private UniversalRepo universalRepo;
	
	public void save(T bean) {
		universalRepo.save(bean);		
	}
	
	public void delete(T bean) {
		universalRepo.delete(bean);		
	}
	
	public T loadById(Long id) {
		return universalRepo.loadById(id, getType());
	}

	protected abstract Class<T> getType();

}
