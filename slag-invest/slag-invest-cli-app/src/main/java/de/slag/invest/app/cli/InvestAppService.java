package de.slag.invest.app.cli;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.common.base.AdmService;

@Service
public class InvestAppService {

	private static final Log LOG = LogFactory.getLog(InvestAppService.class);
	
	@Resource
	private AdmService admService;

	public void importData() {
		LOG.error("not implemented yet");
	}
	
	public void calc() {
		LOG.error("not implemented yet");
	}

}
