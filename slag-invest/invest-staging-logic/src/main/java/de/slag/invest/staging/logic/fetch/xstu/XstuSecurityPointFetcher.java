package de.slag.invest.staging.logic.fetch.xstu;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.io.IOUtils;

import de.slag.invest.staging.logic.fetch.SecurityPointsFetcher;
import de.slag.invest.staging.logic.fetch.model.FetchSecurityPoint;
import de.slag.invest.staging.logic.mapping.IsinWknXstuNotationIdMapper;

public class XstuSecurityPointFetcher implements SecurityPointsFetcher {

	private static final LocalTime TARGET_TIME = LocalTime.of(18, 0, 0);
	
	private static final Long TIME_CALL_OFFSET = 15L;

	private static final LocalTime TO_TIME = TARGET_TIME.plusMinutes(TIME_CALL_OFFSET);

	private static final LocalTime FROM_TIME = TARGET_TIME.minusMinutes(TIME_CALL_OFFSET);

	Clock clock = Clock.systemUTC();

	private LocalDateTime defaultStartTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(17, 45, 00));

	private ZonedDateTime zdt = defaultStartTime.atZone(ZoneId.systemDefault());

	private ZonedDateTime utcZdt = zdt.withZoneSameInstant(ZoneId.of("UTC"));

	private LocalDateTime utcDst = utcZdt.toLocalDateTime();

	private Collection<String> isinWkns = Collections.singletonList("IE00B5BMR087");

	private LocalDateTime from = LocalDateTime.of(LocalDate.now(), LocalTime.now().minusHours(2));

	private LocalDateTime to = LocalDateTime.now();

	private String configId = "649e0402-3e80-426b-b89c-2fcc3f522c2a";

	// "https://www.boerse-stuttgart.de/api/bsg-feature-navigation/PriceDataComponents/DownloadQuoteArchive?notationId=40066463&timeFrom=2021-03-31T04:46:00.000Z&timeTo=2021-03-31T05:16:00.000Z&configId=649e0402-3e80-426b-b89c-2fcc3f522c2a"
	private String baseUrl = "https://www.boerse-stuttgart.de/api/bsg-feature-navigation/PriceDataComponents/DownloadQuoteArchive";

	private IsinWknXstuNotationIdMapper mapper;

	@Override
	public Collection<FetchSecurityPoint> fetchSecurityPoints() throws Exception {

		LocalDateTime fromTimeInUtc = getFromTimeInUtc();
		LocalDateTime toTimeInUtc = getToTimeInUtc();

		String urlString = "https://www.boerse-stuttgart.de/api/bsg-feature-navigation/PriceDataComponents/DownloadQuoteArchive?notationId=40066463&timeFrom=2021-03-30T15:45:00.000Z&timeTo=2021-03-30T16:15:00.000Z&configId=649e0402-3e80-426b-b89c-2fcc3f522c2a";

		URL url = new URL(urlString);
		URLConnection openConnection = url.openConnection();
		InputStream inputStream = openConnection.getInputStream();
//		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//		BufferedReader br = new BufferedReader(inputStreamReader);
//
//		String line;
//		while ((line = br.readLine()) != null) {
//			System.out.println(line);
//		}

		String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
		System.out.println(text);
//	    assertThat(text, equalTo(originalString));

//		for (String isinWkn : isinWkns) {
//			XstuNotationId notationId = mapper.apply(IsinWkn.of(isinWkn));
//			String.format(format, args)
//
//		}

		// TODO Auto-generated method stub
		return null;
	}

	private LocalDateTime getFromTimeInUtc() {
		return toUtc(LocalDateTime.of(getCallDate(), FROM_TIME));
	}

	private LocalDateTime getToTimeInUtc() {
		return toUtc(LocalDateTime.of(getCallDate(), TO_TIME));
	}

	private LocalDate getCallDate() {
		if (isCallableFromToday()) {
			return LocalDate.now();
		}
		return LocalDate.now().minusDays(1);
	}

	private boolean isCallableFromToday() {
		return TO_TIME.isBefore(LocalTime.now());
	}

	private LocalDateTime toUtc(LocalDateTime localDateTime) {
		final ZonedDateTime localZonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
		final ZonedDateTime utcZonedDateTime = localZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
		return utcZonedDateTime.toLocalDateTime();
	}

}
