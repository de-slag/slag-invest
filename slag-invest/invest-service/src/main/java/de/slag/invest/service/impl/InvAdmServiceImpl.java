package de.slag.invest.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import de.slag.common.base.BaseException;
import de.slag.invest.service.api.InvAdmService;

@Service
public class InvAdmServiceImpl implements InvAdmService {

	@PostConstruct
	public void init() {
		final String slagConfigFileName = System.getProperty("SlagConfig");
		final Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(slagConfigFileName));
		} catch (IOException e) {
			throw new BaseException(e);
		}
	}

	@Override
	public String getValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}
}
