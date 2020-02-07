package de.slag.invest.iface.av;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import de.slag.common.base.AdmCache;
import de.slag.common.base.BaseException;
import de.slag.common.utils.CsvUtils;
import de.slag.common.utils.SleepUtils;
import de.slag.invest.iface.av.api.AvStockValueDto;
import de.slag.invest.iface.av.stock.AvStock;
import de.slag.invest.iface.av.stock.AvStockCall;
import de.slag.invest.iface.av.stock.AvStockCallBuilder;
import de.slag.invest.iface.av.stock.AvStockResponse;

@Service
class DataImportFetchService {

	private static final Log LOG = LogFactory.getLog(DataImportFetchService.class);

	private static final Predicate<? super CSVRecord> CSV_RECORD_FILTER_NO_HEADER_LINE = rec -> rec
			.getRecordNumber() != 0;

	private static final int DEFAULT_CALLS_PER_MINUTE = 5;

	@Resource
	private AdmCache admCache;

	List<AvStockValueDto> fetchData() {
		String fetchFile = getAdmValue(AvDataImportProperties.FETCH_FILE);
		if (!new File(fetchFile).exists()) {
			throw new BaseException("fetch file does not exists: " + fetchFile);
		}

		final Collection<CSVRecord> records = getRecords(fetchFile);

		final Map<String, String> symbolIsinMap = mapOf(records);

		final Set<String> symbols = symbolIsinMap.keySet();

		final List<AvStockCall> calls = symbols.stream().map(symbol -> callOf(symbol)).collect(Collectors.toList());

		final double callInterval = 60.0 / DEFAULT_CALLS_PER_MINUTE;
		final Collection<AvStockResponse> responses = new ArrayList<>();
		for (AvStockCall avStockCall : calls) {

			try {
				responses.add(avStockCall.call());
			} catch (Exception e) {
				throw new BaseException("error calling av call", e);
			}

			if (calls.size() == responses.size()) {
				break;
			}

			LOG.warn("wait for " + callInterval + " seconds...");
			SleepUtils.sleepFor((int) (callInterval * 1000));
		}

		final List<AvStockValueDto> allStockValueDtos = responses.stream().map(resp -> {
			return resp.getStocks().stream().map(stock -> stockValueDtoOf(stock, symbolIsinMap.get(stock.getSymbol())))
					.collect(Collectors.toList());
		}).flatMap(list -> list.stream()).collect(Collectors.toList());
		return allStockValueDtos;
	}

	private AvStockValueDto stockValueDtoOf(AvStock stock, String isin) {
		final AvStockValueDto stockValueDto = new AvStockValueDto();
		stockValueDto.setIsin(isin);

		stockValueDto.setDate(stock.getDate());
		stockValueDto.setOpen(stock.getOpen());
		stockValueDto.setHigh(stock.getHigh());
		stockValueDto.setLow(stock.getLow());
		stockValueDto.setClose(stock.getClose());
		stockValueDto.setVolume(stock.getVolume());
		stockValueDto.setTimestamp(LocalDateTime.now());
		return stockValueDto;
	}

	private Map<String, String> mapOf(Collection<CSVRecord> records) {
		Map<String, String> map = new HashMap<>();
		records.forEach(rec -> map.put(rec.get(FetchFile.SYMBOL), rec.get(FetchFile.ISIN)));
		return map;
	}

	private AvStockCall callOf(String symbol) {
		final String apiKey = getAdmValue(AvProperties.API_KEY);

		AvStockCallBuilder avStockCallBuilder = new AvStockCallBuilder();
		avStockCallBuilder.apiKey(apiKey);
		avStockCallBuilder.symbol(symbol);

		final Optional<String> outputSizeFullOptional = admCache.getValue(AvProperties.OUTPUT_SIZE_FULL);
		if (outputSizeFullOptional.isPresent()) {
			Boolean outputSizeFull = Boolean.valueOf(outputSizeFullOptional.get());
			avStockCallBuilder.outputSizeFull(BooleanUtils.isTrue(outputSizeFull));
		}

		return avStockCallBuilder.build();

	}

	private Collection<CSVRecord> getRecords(String fetchFile) {
		final Collection<CSVRecord> records = CsvUtils.getRecords(fetchFile);
		return records.stream().filter(CSV_RECORD_FILTER_NO_HEADER_LINE).collect(Collectors.toList());

	}

	private String getAdmValue(String key) {
		final Optional<String> value = admCache.getValue(key);
		return value.orElseThrow(() -> new BaseException("not configured: " + key));
	}

}
