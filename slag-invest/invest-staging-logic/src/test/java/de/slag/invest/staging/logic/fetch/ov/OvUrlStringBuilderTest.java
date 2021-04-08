package de.slag.invest.staging.logic.fetch.ov;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class OvUrlStringBuilderTest {

	@Test
	void test() {
		String build = new OvUrlStringBuilder().withNotationId("20735").withFromDate(LocalDate.of(2021, 4, 1)).build();
		assertEquals(
				"https://www.onvista.de/onvista/boxes/historicalquote/export.csv?notationId=20735&dateStart=01.04.2020&interval=Y1",
				build);
	}

}
