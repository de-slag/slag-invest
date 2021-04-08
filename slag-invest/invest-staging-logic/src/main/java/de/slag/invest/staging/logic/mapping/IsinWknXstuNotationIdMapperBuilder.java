package de.slag.invest.staging.logic.mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.builder.Builder;

import de.slag.common.util.CsvUtils;

public class IsinWknXstuNotationIdMapperBuilder implements Builder<IsinWknXstuNotationIdMapper> {

	private String sourceFileName;

	private Function<String, Optional<String>> provider;

	public IsinWknXstuNotationIdMapperBuilder withProvider(Function<String, Optional<String>> provider) {
		this.provider = provider;
		return this;
	}

	public IsinWknXstuNotationIdMapperBuilder withSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
		return this;
	}

	@Override
	public IsinWknXstuNotationIdMapper build() {
		if(provider != null) {
			return new IsinWknXstuNotationIdMapper(provider);
		}
		
		Collection<CSVRecord> records = CsvUtils.readRecords(sourceFileName);
		Map<String, String> map = new HashMap<>();
		records.forEach(rec -> {
			String isinWkn = rec.get("ISIN_WKN");
			String notation = rec.get("XSTU_NOTATION_ID");
			map.put(isinWkn, notation);
		});
		return new IsinWknXstuNotationIdMapper(map);
	}

}
