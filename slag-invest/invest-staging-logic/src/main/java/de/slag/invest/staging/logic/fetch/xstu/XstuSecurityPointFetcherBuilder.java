package de.slag.invest.staging.logic.fetch.xstu;

import org.apache.commons.lang3.builder.Builder;

public class XstuSecurityPointFetcherBuilder implements Builder<XstuSecurityPointFetcher> {

	private String baseUrl = "https://www.boerse-stuttgart.de/api/bsg-feature-navigation/PriceDataComponents/DownloadQuoteArchive";

	private String notationId;

	private String timeFrom;

	private String timeTo;

	private String configId = "649e0402-3e80-426b-b89c-2fcc3f522c2a";

	@Override
	public XstuSecurityPointFetcher build() {

		String fullUrl = "https://www.boerse-stuttgart.de/api/bsg-feature-navigation/PriceDataComponents/DownloadQuoteArchive?notationId=40066463&timeFrom=2021-03-31T04:46:00.000Z&timeTo=2021-03-31T05:16:00.000Z&configId=649e0402-3e80-426b-b89c-2fcc3f522c2a";

		// TODO Auto-generated method stub
		return null;
	}

}
