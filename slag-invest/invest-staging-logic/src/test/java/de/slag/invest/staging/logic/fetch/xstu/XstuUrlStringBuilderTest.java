package de.slag.invest.staging.logic.fetch.xstu;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class XstuUrlStringBuilderTest {

	private LocalDateTime timeFrom = LocalDateTime.of(2021, 3, 31, 4, 46);

	private LocalDateTime timeTo = LocalDateTime.of(2021, 3, 31, 5, 16);

	@Test
	void test() {
		String build = new XstuUrlStringBuilder().withTimeFrom(timeFrom).withTimeTo(timeTo).withNotationId("40066463")
				.build();
		String fullUrl = "https://www.boerse-stuttgart.de/api/bsg-feature-navigation/PriceDataComponents/DownloadQuoteArchive?notationId=40066463&timeFrom=2021-03-31T04:46:00.000Z&timeTo=2021-03-31T05:16:00.000Z&configId=649e0402-3e80-426b-b89c-2fcc3f522c2a";
		assertEquals(fullUrl, build);
	}

}
