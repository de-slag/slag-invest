package de.slag.invest.service;

import java.lang.reflect.InvocationTargetException;

import de.slag.common.base.BaseException;
import de.slag.invest.model.Mandant;
import de.slag.invest.model.MandantBean;

public abstract class AbstractMandantBeanServiceImpl<T extends MandantBean> extends AbstractDomainServiceImpl<T>
		implements MandantBeanService<T> {

	@Override
	public T create(Mandant mandant) {

		final Class<T> type = getType();
		try {
			return type.getConstructor(Mandant.class).newInstance(mandant);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new BaseException(e);
		}
	}

}
