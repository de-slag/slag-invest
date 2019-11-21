package de.slag.invest.repo.db;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import de.slag.common.base.AdmService;
import de.slag.common.context.SubClassesUtils;
import de.slag.common.db.HibernateSupport;
import de.slag.common.db.HibernateSupportBuilder;
import de.slag.common.db.h2.InMemoryProperties;
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
		SubClassesUtils.findAllSubclassesOf(DomainBean.class);
		hibernateSupport = new HibernateSupportBuilder()
				.registerClasses(SubClassesUtils.findAllSubclassesOf(DomainBean.class))
				.hibernateDialect(InMemoryProperties.DIALECT).url(InMemoryProperties.URL).user(InMemoryProperties.USER)
				.driver(InMemoryProperties.DRIVER).password(InMemoryProperties.PASSWORD).build();
	}

	public void save(DomainBean bean) {
		hibernateSupport.save(bean);
	}

	public void delete(DomainBean bean) {
		EntityBeanUtils.setDelete(bean);
		save(bean);
	}

	public <D extends DomainBean> D loadById(Long id, Class<D> type) {
		Optional<D> loadById = hibernateSupport.loadById(id, type);
		if (!loadById.isPresent()) {
			return null;
		}
		return type.cast(loadById.get());
	}

	public <D extends DomainBean> Collection<Long> findAllIds(Class<D> type) {
		return hibernateSupport.findAllIds(type);
	}

}
