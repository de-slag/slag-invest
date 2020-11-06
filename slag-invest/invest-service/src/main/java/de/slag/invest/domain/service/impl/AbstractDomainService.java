package de.slag.invest.domain.service.impl;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.base.BaseException;
import de.slag.common.core.dao.Dao;
import de.slag.common.model.EntityBean;
import de.slag.invest.domain.service.api.DomainService;

public abstract class AbstractDomainService<E extends EntityBean> implements DomainService<E> {

	private static final Log LOG = LogFactory.getLog(AbstractDomainService.class);

	abstract protected Dao<E> getDao();

	public void save(E e) {
		getDao().save(e);
	}

	public Optional<E> load(Long id) {
		return getDao().load(id);
	}

	public void delete(E e) {
		getDao().delete(e);
	}

	@Override
	public Collection<E> findBy(Predicate<E> filter) {
		return getDao().findAllBy(filter);
	}

	@Override
	public Optional<E> loadBy(Predicate<E> filter) {
		final Collection<E> findBy = findBy(filter);
		if (findBy.size() > 1) {
			throw new BaseException("more than one result");
		}
		return findBy.stream().findAny();
	}

}
