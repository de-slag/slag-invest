package de.slag.invest.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import de.slag.common.base.BaseException;
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

	public Collection<Long> findAllIds() {
		return universalRepo.findAllIds(getType());
	}

	protected abstract Class<T> getType();

	public Collection<T> findAll() {
		return findAllIds().stream().map(id -> loadById(id)).collect(Collectors.toList());
	}

	protected Collection<T> findBy(Predicate<T> filter) {
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}

	protected Optional<T> loadBy(Predicate<T> filter) {
		final Collection<T> findBy = findBy(filter);
		if (findBy.isEmpty()) {
			return Optional.empty();
		}
		if (findBy.size() > 1) {
			throw new BaseException("more than 1 result: " + findBy);
		}
		return Optional.of(new ArrayList<>(findBy).get(0));
	}
}
