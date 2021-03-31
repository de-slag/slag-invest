package de.slag.common.data;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import de.slag.common.model.EntityBean;

public abstract class AbstractPersistService<E extends EntityBean> implements PersistService<E> {

	@Resource
	private EntityManager entityManager;

	protected EntityManager getEntityManager() {
		return entityManager;
	}

	private Field validUntilPersistEntityField;

	@PostConstruct
	public void init() {
		try {
			validUntilPersistEntityField = EntityBean.class.getDeclaredField("validUntil");
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional
	public void save(E e) {
		entityManager.persist(e);
	}

	@Override
	@Transactional
	public void delete(E e) {
		validUntilPersistEntityField.setAccessible(true);
		try {
			validUntilPersistEntityField.set(e, new Date());
		} catch (IllegalArgumentException | IllegalAccessException e1) {
			throw new RuntimeException(e1);
		}
		validUntilPersistEntityField.setAccessible(false);
		save(e);
	}

	@Override
	@Transactional
	public Optional<E> loadById(Long id) {
		final Class<E> type = getType();
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(type);
		final Root<E> root = criteriaQuery.from(type);

		final ParameterExpression<Long> parameter = criteriaBuilder.parameter(Long.class);
		criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), parameter));

		final TypedQuery<E> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setParameter(parameter, id);
		final List<E> resultList = typedQuery.getResultList();

		if (resultList.isEmpty()) {
			return Optional.empty();
		}
		if (resultList.size() > 1) {
			throw new RuntimeException("more than one entry found for: " + id);
		}

		return Optional.of(resultList.get(0));
	}

	@Override
	@Transactional
	public Collection<Long> findAllIds() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	protected abstract Class<E> getType();

}
