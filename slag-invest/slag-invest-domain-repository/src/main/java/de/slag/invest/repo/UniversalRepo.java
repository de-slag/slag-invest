package de.slag.invest.repo;

import de.slag.invest.model.DomainBean;

public interface UniversalRepo {

	void save(DomainBean bean);

	void delete(DomainBean bean);

	<D extends DomainBean> D loadById(Long id, Class<D> type);

}
