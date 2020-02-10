package de.slag.invest.webservice;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.ws.rs.BadRequestException;

import org.junit.jupiter.api.Test;

public class CrudReadWsIntegrationTest extends AbstractWsIntegrationTest {

	@Override
	String getUri() {
		return super.getUri() + "/crud/read";
	}

	@Test
	public void failNoParameter() {
		assertThrows(BadRequestException.class, () -> getResponse(String.class));
	}

}
