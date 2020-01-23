package de.slag.invest.imp.filecache;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.common.base.AdmCache;
import de.slag.common.base.BaseException;
import de.slag.common.utils.CsvUtils;
import de.slag.common.utils.DateUtils;

@Service
public class ImportFileCacheServiceImpl implements ImportCacheService {

	private static final Log LOG = LogFactory.getLog(ImportFileCacheServiceImpl.class);

	private static final String DEFAULT_SDF_FORMAT_DATE = "yyyy-MM-dd";

	private static final String DEFAULT_SDF_FORMAT_DATETIME = "yyyy-MM-dd_HH:mm:ss";

	private static final String TIMESTAMP = "TIMESTAMP";

	private static final String VOLUME = "VOLUME";

	private static final String CLOSE = "CLOSE";

	private static final String LOW = "LOW";

	private static final String HIGH = "HIGH";

	private static final String OPEN = "OPEN";

	private static final String DATE = "DATE";

	private static final String ISIN = "ISIN";

	private static final String CACHE_PATH = "de.slag.invest.imp.filecache.path";

	@Resource
	private AdmCache admCache;

	@PostConstruct
	public void init() {

		// to assert configuration

		final String cachePathName = getCachePathName();
		if (!new File(cachePathName).exists()) {
			throw new BaseException("cache path does not exists: '%s'", cachePathName);
		}
	}

	public Collection<ImportCacheStockValueDto> fetchData() {

		String cachePathName = getCachePathName();
		File cachePath = new File(cachePathName);
		List<File> files = Arrays.asList(cachePath.listFiles());
		List<File> csvFiles = files.stream().filter(file -> !file.isDirectory())
				.filter(file -> file.getName().endsWith(".csv")).collect(Collectors.toList());

		final List<Collection<CSVRecord>> allRecordsFromCache = csvFiles.stream().map(csvfile -> getRecords(csvfile))
				.collect(Collectors.toList());
		final List<CSVRecord> flatedAllRecordsFromCache = allRecordsFromCache.stream()
				.flatMap(recList -> recList.stream()).collect(Collectors.toList());

		final List<ImportCacheStockValueDto> collect = flatedAllRecordsFromCache.stream().map(rec -> toDto(rec))
				.collect(Collectors.toList());

		LOG.warn(collect.size() + " datasets from file cache restored");

		return collect;
	}

	private ImportCacheStockValueDto toDto(CSVRecord record) {
		LocalDate date;
		try {
			date = DateUtils.toLocalDate(new SimpleDateFormat(DEFAULT_SDF_FORMAT_DATE).parse(record.get(DATE)));
		} catch (ParseException e) {
			throw new BaseException("error parsing date", e);
		}

		LocalDateTime timestamp;
		try {
			timestamp = DateUtils
					.toLocalDateTime(new SimpleDateFormat(DEFAULT_SDF_FORMAT_DATETIME).parse(record.get(TIMESTAMP)));
		} catch (ParseException e) {
			throw new BaseException("error parsing timestamp", e);
		}

		final ImportCacheStockValueDto dto = new ImportCacheStockValueDto();
		dto.setIsin(record.get(ISIN));
		dto.setDate(date);
		dto.setOpen(BigDecimal.valueOf(Double.valueOf(record.get(OPEN))));
		dto.setHigh(BigDecimal.valueOf(Double.valueOf(record.get(HIGH))));
		dto.setLow(BigDecimal.valueOf(Double.valueOf(record.get(LOW))));
		dto.setClose(BigDecimal.valueOf(Double.valueOf(record.get(CLOSE))));
		dto.setVolume(Long.valueOf(record.get(VOLUME)));
		dto.setTimestamp(timestamp);

		return dto;
	}

	private Collection<CSVRecord> getRecords(File csvfile) {
		Collection<CSVRecord> records = CsvUtils.getRecords(csvfile.getAbsolutePath());
		return records.stream().filter(rec -> rec.getRecordNumber() != 0).collect(Collectors.toList());
	}

	public void storeData(Collection<ImportCacheStockValueDto> data) {
		final String cachePathName = getCachePathName();

		final String filePathName = cachePathName + "/" + System.currentTimeMillis() + ".csv";

		Collection<Collection<String>> collect = data.stream().map(dto -> toList(dto)).collect(Collectors.toList());

		try {
			CsvUtils.write(filePathName, Arrays.asList(ISIN, DATE, OPEN, HIGH, LOW, CLOSE, VOLUME, TIMESTAMP), collect);
		} catch (IOException e) {
			throw new BaseException("error store cache", e);
		}

		LOG.warn(collect.size() + " datasets stored to: " + filePathName);
	}

	private List<String> toList(ImportCacheStockValueDto dto) {
		String date = new SimpleDateFormat(DEFAULT_SDF_FORMAT_DATE).format(DateUtils.toDate(dto.getDate()));
		String timestamp = new SimpleDateFormat(DEFAULT_SDF_FORMAT_DATETIME)
				.format(DateUtils.toDate(dto.getTimestamp()));

		String volume = dto.getVolume() != null ? dto.getVolume().toString() : null;

		final List<String> list = new ArrayList<String>();
		list.add(dto.getIsin());
		list.add(date);
		list.add(dto.getOpen().toString());
		list.add(dto.getHigh().toString());
		list.add(dto.getLow().toString());
		list.add(dto.getClose().toString());
		list.add(volume);
		list.add(timestamp);

		return list;
	}

	private String getCachePathName() {
		return admCache.getValue(CACHE_PATH).orElseThrow(() -> new BaseException("not configured: '%s'", CACHE_PATH));
	}

}
