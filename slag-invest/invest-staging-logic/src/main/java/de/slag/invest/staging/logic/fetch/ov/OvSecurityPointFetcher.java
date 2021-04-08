package de.slag.invest.staging.logic.fetch.ov;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.util.CsvUtils;
import de.slag.invest.staging.logic.fetch.SecurityPointsFetcher;
import de.slag.invest.staging.logic.fetch.model.FetchSecurityPoint;
import de.slag.invest.staging.logic.mapping.IsinWkn;
import de.slag.invest.staging.logic.mapping.IsinWknOvNotationIdMapper;
import de.slag.invest.staging.logic.mapping.OvNotationId;
import de.slag.invest.staging.model.SecurityPointSource;

public class OvSecurityPointFetcher implements SecurityPointsFetcher {

	private static final Log LOG = LogFactory.getLog(OvSecurityPointFetcher.class);
	
	private IsinWknOvNotationIdMapper ovNotationIdMapper;

	private Collection<IsinWkn> isinWkns = new ArrayList<>();

	OvSecurityPointFetcher(IsinWknOvNotationIdMapper ovNotationIdMapper, Collection<IsinWkn> isinWkns) {
		super();
		this.ovNotationIdMapper = ovNotationIdMapper;
		this.isinWkns = isinWkns;
	}

	@Override
	public Collection<FetchSecurityPoint> fetchSecurityPoints() throws Exception {

		Collection<FetchSecurityPoint> points = new ArrayList<>();
		for (IsinWkn isinWkn : isinWkns) {
			OvNotationId notationId = ovNotationIdMapper.apply(isinWkn);
			String urlString = new OvUrlStringBuilder().withFromDate(LocalDate.now())
					.withNotationId(notationId.getValue()).build();
			URL url = new URL(urlString);
			URLConnection openConnection = url.openConnection();
			InputStream inputStream = openConnection.getInputStream();
			String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
			Path tempCsv = Files.createTempFile("fetched-ov-security-points", ".csv");
			Files.write(tempCsv, text.trim().getBytes());
			Collection<CSVRecord> readRecords = CsvUtils.readRecords(tempCsv.toFile().toString());
			for (CSVRecord csvRecord : readRecords) {
				String datum = csvRecord.get("Datum");
				String schluss = csvRecord.get("Schluss");
				FetchSecurityPoint point = new FetchSecurityPoint();

				point.setTimestamp(toDate(datum));
				point.setValue(toValue(schluss));
				point.setSource(SecurityPointSource.ONVISTA);
				point.setIsinWkn(isinWkn.getValue());
				point.setCurrency("EUR");

				points.add(point);

			}
		}

		LOG.info("recieved points: " + points.size());
		return points;
	}

	private BigDecimal toValue(String schluss) {
		String preformatted = schluss.replace(".", "").replace(",", ".");
		Double valueOf = Double.valueOf(preformatted);
		return BigDecimal.valueOf(valueOf);
	}

	private Date toDate(String string) {
		try {
			return new SimpleDateFormat("dd.MM.yyyy").parse(string);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
