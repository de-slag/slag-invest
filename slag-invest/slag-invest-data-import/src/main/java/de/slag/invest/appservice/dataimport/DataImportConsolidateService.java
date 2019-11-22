package de.slag.invest.appservice.dataimport;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class DataImportConsolidateService {

	private static final Log LOG = LogFactory.getLog(DataImportConsolidateService.class);

	private static final Comparator<ImpStockValueDto> TIMESTAMP_COMPARATOR = new Comparator<ImpStockValueDto>() {

		@Override
		public int compare(ImpStockValueDto o1, ImpStockValueDto o2) {
			Objects.requireNonNull(o1, "o1 is null");
			Objects.requireNonNull(o2, "o2 is null");

			Objects.requireNonNull(o1.getTimestamp(), "o1.timestamp is null: " + o1);
			Objects.requireNonNull(o2.getTimestamp(), "o2.timestamp is null: " + o2);

			return o1.getTimestamp().compareTo(o2.getTimestamp());
		}
	};

	Collection<ImpStockValueDto> newestForThisDay(Collection<ImpStockValueDto> all) {
		final Map<String, List<ImpStockValueDto>> map = new HashMap<>();
		for (ImpStockValueDto dto : all) {
			final String identifier = identifier(dto.getIsin(), dto.getDate());
			if (!map.containsKey(identifier)) {
				map.put(identifier, new ArrayList<>());
			}
			map.get(identifier).add(dto);
		}
		map.keySet().forEach(id -> {
			List<ImpStockValueDto> list = map.get(id);
			Collections.sort(list, TIMESTAMP_COMPARATOR.reversed());
		});

		final List<ImpStockValueDto> newes = map.keySet().stream().map(id -> map.get(id).get(0))
				.collect(Collectors.toList());

		LOG.warn(newes.size() + " newest found out of: " + all.size());

		return newes;

	}

	private String identifier(String isin, LocalDate date) {
		return isin + ":" + date;
	}

}
