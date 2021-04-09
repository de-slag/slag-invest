package de.slag.invest.staging.logic.mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.builder.Builder;

import de.slag.common.util.CsvUtils;

public class IsinWknOvNotationIdMapperBuilder implements Builder<IsinWknOvNotationIdMapper> {

	private String sourceFileName;

	private Function<String, Optional<String>> provider;

	public IsinWknOvNotationIdMapperBuilder withProvider(Function<String, Optional<String>> provider) {
		this.provider = provider;
		return this;
	}

	public IsinWknOvNotationIdMapperBuilder withSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
		return this;
	}

	@Override
	public IsinWknOvNotationIdMapper build() {
		if (provider != null) {
			return new IsinWknOvNotationIdMapper(provider);
		}

		Collection<CSVRecord> records = CsvUtils.readRecords(sourceFileName);
		Map<String, String> map = new HashMap<>();
		records.forEach(rec -> {
			String isinWkn = rec.get("ISIN_WKN");
			String symbol = rec.get("OV_NOTATION_ID");
			map.put(isinWkn, symbol);
		});
		return new IsinWknOvNotationIdMapper(map);
	}

}