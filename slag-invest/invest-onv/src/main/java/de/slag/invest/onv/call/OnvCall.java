package de.slag.invest.onv.call;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import de.slag.common.util.CsvUtils;
import de.slag.common.util.DateUtils;
import de.slag.invest.onv.model.OnvStockData;

public class OnvCall implements Callable<List<OnvStockData>> {

	private Predicate<CSVRecord> FILTER_NO_HEADER = rec -> OnvStockData.CSV_STRUCTURE.stream()
			.anyMatch(head -> !head.equals(rec.get(head)));

	private String callUrl;

	OnvCall(String callUrl) {
		this.callUrl = callUrl;
	}

	@Override
	public List<OnvStockData> call() throws Exception {
		
		Path tmpFile = Files.createTempFile("onv-call-", ".csv");
		callAndSave(tmpFile);
		Path fileWithValidLines = Files.createTempFile("onv-call-valid-lines-", ".csv");
		removeInvalidLines(tmpFile, fileWithValidLines);
		Collection<CSVRecord> recordsWithHeader = CsvUtils.getRecords(fileWithValidLines.toString(),
				OnvStockData.CSV_STRUCTURE);
		return recordsWithHeader.stream().filter(FILTER_NO_HEADER).map(rec -> stockDataOf(rec))
				.collect(Collectors.toList());
	}

	private OnvStockData stockDataOf(CSVRecord rec) {
		LocalDate datum;
		try {
			datum = DateUtils
					.toLocalDate(new SimpleDateFormat(OnvStockData.DATUM_FORMAT).parse(rec.get(OnvStockData.DATUM)));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		Long volumen = Long.valueOf(technicalNumberFormattedOf(rec.get(OnvStockData.VOLUMEN)));
		Double tief = Double.valueOf(technicalNumberFormattedOf(rec.get(OnvStockData.TIEF)));
		Double schluss = Double.valueOf(technicalNumberFormattedOf(rec.get(OnvStockData.SCHLUSS)));
		Double hoch = Double.valueOf(technicalNumberFormattedOf(rec.get(OnvStockData.HOCH)));
		Double eroeffnung = Double.valueOf(technicalNumberFormattedOf(rec.get(OnvStockData.EROEFFUNG)));

		return new OnvStockData() {

			@Override
			public long getVolume() {
				return volumen;
			}

			@Override
			public double getTief() {
				return tief;
			}

			@Override
			public double getSchluss() {
				return schluss;
			}

			@Override
			public double getHoch() {
				return hoch;
			}

			@Override
			public double getEroeffnung() {
				return eroeffnung;
			}

			@Override
			public LocalDate getDatum() {
				return datum;
			}
		};
	}

	private String technicalNumberFormattedOf(String humanReadableFormatted) {
		return humanReadableFormatted.replace(".", "").replace(",", ".");
	}

	private void removeInvalidLines(Path source, Path target) throws IOException {
		final List<String> validLines = Files.lines(source).filter(line -> StringUtils.isNotBlank(line))
				.collect(Collectors.toList());
		Files.write(target, validLines);
	}

	private void callAndSave(Path outputFilePath) throws IOException {
		final URL website = new URL(callUrl);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());

		try (final FileOutputStream fos = new FileOutputStream(outputFilePath.toFile())) {
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		}
	}

}
