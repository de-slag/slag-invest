package de.slag.invest.av.calls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;

import de.slag.common.base.BaseException;

public class AvTestAdmSupport {

	private Properties properties;

	public AvTestAdmSupport() {
		final String userDirPath = SystemUtils.USER_HOME;
		final String fileName = "slag.properties";
		final File file = new File(userDirPath + "/" + fileName);
		if (!file.exists()) {
			throw new BaseException("File does not exists:" + file);
		}
		FileInputStream inStream;
		try {
			inStream = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			throw new BaseException("error creating input stream for file: " + file);
		}
		properties = new Properties();
		try {
			properties.load(inStream);
		} catch (IOException e) {
			throw new BaseException("error reading properties from file: " + fileName, e);
		}
	}

	public Optional<String> getProperty(String key) {
		final String property = properties.getProperty(key);
		if (property == null) {
			return Optional.empty();
		}
		return Optional.of(property);
	}
}
