package de.slag.invest.service;

import de.slag.invest.model.Mandant;
import de.slag.invest.model.MandantBean;

public interface MandantBeanService<T extends MandantBean> extends DomainService<T> {
	
	T create(Mandant mandant);

}
