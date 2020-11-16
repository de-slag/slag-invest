package de.slag.invest.persist.impl;

import org.springframework.stereotype.Repository;

import de.slag.common.model.EntityBean;
import de.slag.invest.domain.model.report.Report;
import de.slag.invest.persist.api.ReportDao;

@Repository
public class ReportDaoImpl extends AbstractInvestDao<Report> implements ReportDao {

	@Override
	protected Class<? extends EntityBean> getType() {
		return Report.class;
	}

}
