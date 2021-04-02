package de.slag.invest.staging.logic.fetch.xstu;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;

import de.slag.common.util.CsvUtils;
import de.slag.invest.staging.logic.fetch.SecurityPointsFetcher;
import de.slag.invest.staging.logic.fetch.model.FetchSecurityPoint;
import de.slag.invest.staging.logic.mapping.IsinWkn;
import de.slag.invest.staging.logic.mapping.IsinWknXstuNotationIdMapper;
import de.slag.invest.staging.logic.mapping.XstuNotationId;
import de.slag.invest.staging.model.SecurityPointSource;

public class XstuSecurityPointFetcher implements SecurityPointsFetcher {

	private static final LocalTime TARGET_TIME = LocalTime.of(18, 0, 0);

	private static final Long TIME_CALL_OFFSET = 15L;

	private static final LocalTime TO_TIME = TARGET_TIME.plusMinutes(TIME_CALL_OFFSET);

	private static final LocalTime FROM_TIME = TARGET_TIME.minusMinutes(TIME_CALL_OFFSET);

	private Collection<IsinWkn> isinWkns = new ArrayList<>();

	private IsinWknXstuNotationIdMapper mapper;

	XstuSecurityPointFetcher(Collection<IsinWkn> isinWkns, IsinWknXstuNotationIdMapper mapper) {
		super();
		this.isinWkns.addAll(isinWkns);
		this.mapper = mapper;
	}

	@Override
	public Collection<FetchSecurityPoint> fetchSecurityPoints() throws Exception {

		LocalDateTime timeFrom = getFromTimeInUtc();
		LocalDateTime timeTo = getToTimeInUtc();

		Collection<FetchSecurityPoint> points =new ArrayList<>();
		for (IsinWkn isinWkn : isinWkns) {
			XstuNotationId notationId = mapper.apply(isinWkn);
			String urlString = new XstuUrlStringBuilder().withTimeFrom(timeFrom).withTimeTo(timeTo)
					.withNotationId(notationId.getValue()).build();

			URL url = new URL(urlString);
			URLConnection openConnection = url.openConnection();
			InputStream inputStream = openConnection.getInputStream();
			String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
			Path tempCsv = Files.createTempFile("fetched-xstu-security-points", ".csv");
			Files.write(tempCsv, text.getBytes());
			Collection<CSVRecord> readRecords = CsvUtils.readRecords(tempCsv.toFile().toString());
			for (CSVRecord csvRecord : readRecords) {
				String datum = csvRecord.get("Datum");
				String uhrzeit = csvRecord.get("Uhrzeit");
				String brief = csvRecord.get("Brief");
				String geld = csvRecord.get("Geld");

				BigDecimal briefValue = toBigDecimal(brief);
				BigDecimal geldValue = toBigDecimal(geld);
				
				BigDecimal value = geldValue.add(briefValue).divide(BigDecimal.valueOf(2));

				String datumAndUhrzeit = String.format("%s %s", datum, uhrzeit.split("\\.")[0]);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
				Date timestamp = simpleDateFormat.parse(datumAndUhrzeit);
				
				FetchSecurityPoint fetchSecurityPoint = new FetchSecurityPoint();
				fetchSecurityPoint.setCurrency("EUR");
				fetchSecurityPoint.setSource(SecurityPointSource.BOERSE_STUTTGART);
				fetchSecurityPoint.setIsinWkn(isinWkn.getValue());
				fetchSecurityPoint.setTimestamp(timestamp);
				fetchSecurityPoint.setValue(value);
				
				points.add(fetchSecurityPoint);		

			}
		}
		return points;
	}

	private BigDecimal toBigDecimal(String value) {
		String valuePreformatted = value.replace(",", ".").replace("\"", "");
		Double doubleValue = Double.valueOf(valuePreformatted);
		return BigDecimal.valueOf(doubleValue);
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
