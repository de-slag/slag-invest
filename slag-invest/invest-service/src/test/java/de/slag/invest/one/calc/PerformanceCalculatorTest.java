package de.slag.invest.one.calc;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.slag.common.base.BaseException;
import de.slag.common.util.CsvUtils;
import de.slag.common.util.DateUtils;

class PerformanceCalculatorTest {

	static String FILE_DAX_2010_2014;

	@BeforeAll
	static void setUp() throws IOException {
		final InputStream is = PerformanceCalculatorTest.class.getClassLoader()
				.getResourceAsStream("dax-2010-2014.csv");
		final Path tmpFile = Files.createTempFile("dax-2010-2014", ".csv");
		final FileOutputStream fileOutputStream = new FileOutputStream(tmpFile.toFile());
		final byte[] readAllBytes = is.readAllBytes();
		fileOutputStream.write(readAllBytes);

		FILE_DAX_2010_2014 = tmpFile.toString();
	}

	Map<LocalDate, BigDecimal> valuesOf(String filename) throws IOException {
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

	LocalDate localDateOf(String dateString) {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date parse;
		try {
			parse = sdf.parse(dateString);
		} catch (ParseException e) {
			throw new BaseException(e);
		}
		return DateUtils.toLocalDate(parse);
	}

	@Test
	void firstQuarterTest() throws IOException {
		LocalDate forDate = LocalDate.of(2010, 1, 31);
		LocalDate sinceDate = LocalDate.of(2010, 1, 1);
		Map<LocalDate, BigDecimal> performanceValues = valuesOf(FILE_DAX_2010_2014);
		int dateToleranceDays = 5;
		final PerformanceCalculator performanceCalculator = new PerformanceCalculator(forDate, sinceDate,
				performanceValues, dateToleranceDays);
		final BigDecimal calculate = performanceCalculator.calculate();
		assertEquals(BigDecimal.valueOf(-0.0585219), calculate);
	}

	@Test
	void simpleTest() {
		LocalDate forDate = LocalDate.of(2010, 1, 31);
		LocalDate sinceDate = LocalDate.of(2010, 1, 1);

		Map<LocalDate, BigDecimal> performanceValues = new HashMap<>();
		performanceValues.put(LocalDate.of(2009, 12, 30), BigDecimal.valueOf(5957.430));
		performanceValues.put(LocalDate.of(2010, 01, 29), BigDecimal.valueOf(5608.79));

		int dateToleranceDays = 5;
		final PerformanceCalculator performanceCalculator = new PerformanceCalculator(forDate, sinceDate,
				performanceValues, dateToleranceDays);
		final BigDecimal calculate = performanceCalculator.calculate();
		assertEquals(BigDecimal.valueOf(-0.0585219), calculate);
	}

}
