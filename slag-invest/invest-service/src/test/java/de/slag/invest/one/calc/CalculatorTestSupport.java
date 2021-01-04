package de.slag.invest.one.calc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import de.slag.common.base.BaseException;
import de.slag.common.util.CsvUtils;
import de.slag.common.util.DateUtils;

public class CalculatorTestSupport {

	private Map<String, String> m = new HashMap<>();

	public void loadResourceFileToCache(String resourceFilePath) throws IOException {

		final InputStream is = PerformanceCalculatorTest.class.getClassLoader().getResourceAsStream(resourceFilePath);
		final String identifierOf = identifierOf(resourceFilePath);
		final Path tmpFile = Files.createTempFile(identifierOf, ".csv");
		final FileOutputStream fileOutputStream = new FileOutputStream(tmpFile.toFile());
		final byte[] readAllBytes = is.readAllBytes();
		fileOutputStream.write(readAllBytes);
		m.put(identifierOf, tmpFile.toString());
	}

	public Map<LocalDate, BigDecimal> valuesOf(String resourceFilePath) throws IOException {
		final String identifierOf = identifierOf(resourceFilePath);
		final String filename = m.get(identifierOf);
		
		final Collection<String> header = CsvUtils.getHeader(filename);
		final Collection<CSVRecord> records = CsvUtils.getRecords(filename, header, true);

		Map<LocalDate, BigDecimal> resultMap = new HashMap<>();

		records.forEach(rec -> {
			final String datum = rec.get("Datum");
			final String schlussKurs = rec.get("Schluss");

			final LocalDate localDateOf = localDateOf(datum);
			final Double priceAsDouble = Double.valueOf(schlussKurs.replace(".", "").replace(",", "."));
			resultMap.put(localDateOf, BigDecimal.valueOf(priceAsDouble));

		});
		return resultMap;
	}

	private LocalDate localDateOf(String dateString) {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date parse;
		try {
			parse = sdf.parse(dateString);
		} catch (ParseException e) {
			throw new BaseException(e);
		}
		return DateUtils.toLocalDate(parse);
	}

	private String identifierOf(String resourceFilePath) {
		return resourceFilePath.replace(".", "-");
	}

}
