package de.slag.invest.service.adm;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.invest.service.adm.api.InvAdmService;

@Service
public class InvAdmServiceImpl implements InvAdmService {

	private static final Log LOG = LogFactory.getLog(InvAdmServiceImpl.class);

	private Properties properties;

	@PostConstruct
	public void init() {
		properties = new Properties();
		properties.putAll(InvConfig.getProperties());
		LOG.info("initialized");
	}

	@Override
	public String getValue(String key) {
		return properties.getProperty(key);
	}
}
