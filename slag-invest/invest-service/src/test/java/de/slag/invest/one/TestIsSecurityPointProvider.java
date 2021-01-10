package de.slag.invest.one;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.money.MonetaryAmount;

import org.apache.commons.csv.CSVRecord;

import de.slag.common.base.BaseException;
import de.slag.common.util.CsvUtils;
import de.slag.common.util.CurrencyUtils;
import de.slag.common.util.DateUtils;
import de.slag.common.util.ResourceUtils;
import de.slag.invest.one.model.IsSecurity;
import de.slag.invest.one.model.IsSecurityPoint;
import de.slag.invest.one.model.IsSecurityPointIdentifier;
import de.slag.invest.one.portfolio.IsSecurityPointProvider;
import de.slag.invest.one.portfolio.IsSecurityProvider;

public class TestIsSecurityPointProvider implements IsSecurityPointProvider {

	private List<IsSecurityPoint> points = new ArrayList<>();

	public static TestIsSecurityPointProvider buildWith(IsSecurityProvider securityProvider) throws Exception {
		final File securitiesFolder = ResourceUtils.getFileFromResources("securities");
		final List<File> csvFiles = Arrays.asList(securitiesFolder.listFiles())
				.stream()
				.filter(f -> f.isFile())
				.filter(f -> f.getName().endsWith(".csv"))
				.collect(Collectors.toList());

		final List<IsSecurityPoint> pointsFromCsv = new ArrayList<>();
		csvFiles.forEach(file -> {
			String isinWkn = file.getName().split("-")[0];
			final Collection<CSVRecord> records = CsvUtils.readRecords(file.getAbsolutePath());
			records.forEach(rec -> {
				final String datum = rec.get("Datum");
				final String schlussKurs = rec.get("Schluss").replace(".", "").replace(",", ".");

				final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				Date parse;
				try {
					parse = sdf.parse(datum);
				} catch (ParseException e) {
					throw new BaseException(e);
				}
				final LocalDate pointDate = DateUtils.toLocalDate(parse);
				final IsSecurity security = securityProvider.provide(isinWkn).get();
				final MonetaryAmount pointAmount = CurrencyUtils.newAmount(Double.valueOf(schlussKurs));
				pointsFromCsv.add(new IsSecurityPoint(security, pointAmount, pointDate));

			});
		});

		return new TestIsSecurityPointProvider(pointsFromCsv);
	}

	private TestIsSecurityPointProvider(List<IsSecurityPoint> points) {
		super();
		this.points.addAll(points);
	}

	@Override
	public Optional<IsSecurityPoint> provide(IsSecurityPointIdentifier identifier) {
		return points.stream()
				.filter(p -> identifier.getDate().equals(p.getPointDate()))
				.filter(p -> identifier.getIsinWkn().equals(p.getSecurity().getWknIsin()))
				.findFirst();
	}

}
