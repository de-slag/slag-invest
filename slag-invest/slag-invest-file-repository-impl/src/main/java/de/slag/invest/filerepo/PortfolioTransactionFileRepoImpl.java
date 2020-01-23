package de.slag.invest.filerepo;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import de.slag.common.base.AdmCache;
import de.slag.common.base.BaseException;
import de.slag.common.utils.CsvUtils;
import de.slag.common.utils.DateUtils;

@Repository
public class PortfolioTransactionFileRepoImpl implements PortfolioTransactionFileRepo {

	private static final Log LOG = LogFactory.getLog(PortfolioTransactionFileRepoImpl.class);

	private static final String PORTFOLIO = "PORTFOLIO_";
	@Resource
	private AdmCache admCache;

	@Override
	public Collection<PortfolioTransactionDto> findAll() {
		final String fileRepoPathName = admCache.getValue(FileRepoProperties.DE_SLAG_INVEST_FILEREPO)
				.orElseThrow(() -> new BaseException("not configured: " + FileRepoProperties.DE_SLAG_INVEST_FILEREPO));

		LOG.info("File-Repo Path-Name: " + fileRepoPathName);

		final File fileRepoPath = new File(fileRepoPathName);
		final List<File> portfolioFiles = Arrays.asList(fileRepoPath.listFiles()).stream()
				.filter(file -> file.getName().startsWith(PORTFOLIO)).filter(file -> file.getName().endsWith(".csv"))
				.collect(Collectors.toList());

		return portfolioFiles.stream().flatMap(file -> toTransactionDtos(file).stream()).collect(Collectors.toList());

	}

	private Collection<PortfolioTransactionDto> toTransactionDtos(File file) {
		final String portfolioNumber0 = file.getName().substring(PORTFOLIO.length());
		final String portfolioNumber = portfolioNumber0.substring(0, portfolioNumber0.length() - 4);

		Collection<CSVRecord> records = CsvUtils.getRecords(file.getAbsolutePath());

		Collection<Map<String, String>> recordsAsValueMaps = records.stream().map(rec -> rec.toMap())
				.collect(Collectors.toList());

		final PortfolioTransactionDataValidator portfolioTransactionDataValidator = new PortfolioTransactionDataValidator();
		if (!portfolioTransactionDataValidator.isValid(recordsAsValueMaps)) {
			LOG.error(String.format("* * *\ncontent not valid: %s , file: %s",
					portfolioTransactionDataValidator.getValidateIssues(), file));
			return Collections.emptyList();
		}

		return records.stream().map(rec -> {
			PortfolioTransactionDto dto = new PortfolioTransactionDto();
			dto.setPortfolioNumber(portfolioNumber);

			Date date;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(rec.get("DATE"));
			} catch (ParseException e) {
				throw new BaseException(e);
			}
			dto.setIsin(rec.get("ISIN"));
			dto.setCount(Integer.valueOf(rec.get("COUNT")));
			dto.setDate(DateUtils.toLocalDate(date));
			dto.setTotalPrice(BigDecimal.valueOf(Double.valueOf(rec.get("TOTAL_PRICE"))));
			dto.setType(rec.get("TYPE"));
			return dto;
		}).collect(Collectors.toList());
	}

}
