package de.slag.invest;

import de.slag.invest.model.DomainBean;

public interface Dto<T extends DomainBean> {
	
	Long getId();

}
