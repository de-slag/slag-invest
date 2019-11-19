package de.slag.invest.repo.db;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import de.slag.common.base.AdmService;
import de.slag.common.base.BaseException;
import de.slag.common.db.HibernateSupport;
import de.slag.common.db.HibernateSupportBuilder;
import de.slag.common.model.EntityBeanUtils;
import de.slag.invest.model.DomainBean;
import de.slag.invest.repo.UniversalRepo;

@Repository
public class UniversalRepoImpl implements UniversalRepo {

	@Resource
	private AdmService admService;

	private HibernateSupport hibernateSupport;

	@PostConstruct
	public void init() {

		// TODO HibernateSupportBuilder ...
		throw new BaseException("not implemented yet");
	}

	public void save(DomainBean bean) {
		hibernateSupport.save(bean);
	}

	public void delete(DomainBean bean) {
		EntityBeanUtils.setDelete(bean);
		save(bean);
	}

	public <D extends DomainBean> D loadById(Long id, Class<D> type) {
		return type.cast(hibernateSupport.loadById(id, type));
	}

}
