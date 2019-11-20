package de.slag.invest.app.cli;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import de.slag.common.base.AdmService;
import de.slag.common.base.BaseException;

@Service
public class InvestAppAdmService implements AdmService {

	private Properties properties = new Properties();

	@PostConstruct
	public void setUp() {
		final String configFilePath = System.getProperty("config");
		if (StringUtils.isBlank(configFilePath)) {
			throw new BaseException("property not setted: 'config'");
		}
		
	}

	public String getValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
