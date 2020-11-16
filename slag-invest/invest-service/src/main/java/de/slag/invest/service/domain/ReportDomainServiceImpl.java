package de.slag.invest.service.domain;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.slag.common.core.dao.Dao;
import de.slag.invest.domain.model.report.Report;
import de.slag.invest.persist.api.ReportDao;
import de.slag.invest.service.domain.api.ReportDomainService;

@Service
public class ReportDomainServiceImpl extends AbstractDomainService<Report> implements ReportDomainService {

	@Resource
	private ReportDao reportDao;

	@Override
	public Report create() {
		return new Report();
	}

	@Override
	protected Dao<Report> getDao() {
		return reportDao;
	}

}
