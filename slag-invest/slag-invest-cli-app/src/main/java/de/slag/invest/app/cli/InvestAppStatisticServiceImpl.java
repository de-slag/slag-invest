package de.slag.invest.app.cli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import de.slag.invest.model.StockValue;
import de.slag.invest.service.DomainService;
import de.slag.invest.service.StockValueService;

@Service
public class InvestAppStatisticServiceImpl implements InvestAppStatisticService {

	@Resource
	private StockValueService stockValueService;

	@Override
	public String printStatistics() {
		final List<String> messages = new ArrayList<>();
		messages.add("STATISTICS");
		messages.add(count(stockValueService, "StockValues"));

		messages.add(stockValues());

		return String.join("\n", messages);
	}

	private String stockValues() {
		final Collection<StockValue> all = stockValueService.findAll();
		Map<String, List<StockValue>> isinWknMapped = new HashMap<>();
		all.forEach(value -> {
			final String key = String.format("%s (%s)", getIsinWkn(value), value.getName());
			if (!isinWknMapped.containsKey(key)) {
				isinWknMapped.put(key, new ArrayList<>());
			}
			isinWknMapped.get(key).add(value);
		});

		List<String> strings = new ArrayList<String>();
		isinWknMapped.keySet().forEach(key -> {
			strings.add(key + ": " + isinWknMapped.get(key).size());
		});

		// TODO Auto-generated method stub
		return String.join("\n", strings);
	}

	private String getIsinWkn(StockValue sv) {
		final String isin = sv.getIsin();
		if (StringUtils.isNoneEmpty(isin)) {
			return isin;
		}
		return sv.getWkn();
	}

	private String count(DomainService<?> domainService, String beansName) {
		return beansName + ": " + domainService.findAllIds().size();
	}

}
