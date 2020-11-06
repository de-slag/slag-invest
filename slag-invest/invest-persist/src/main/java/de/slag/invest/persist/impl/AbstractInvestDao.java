package de.slag.invest.persist.impl;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.slag.common.context.SubClassesUtils;
import de.slag.common.core.dao.AbstractDao;
import de.slag.common.model.EntityBean;

public abstract class AbstractInvestDao<E extends EntityBean> extends AbstractDao<E> {

	static Function<Class<?>, Class<? extends EntityBean>> CONV = type -> (Class<? extends EntityBean>) type;

	static {
		registeredEntitiesSupplier = () -> {
			Collection<Class<?>> findAllSubclassesOf = SubClassesUtils.findAllSubclassesOf(EntityBean.class);
			return findAllSubclassesOf.stream().map(CONV).collect(Collectors.toList());
		};

	}
}
