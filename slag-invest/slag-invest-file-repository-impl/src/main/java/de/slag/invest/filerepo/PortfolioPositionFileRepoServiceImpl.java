package de.slag.invest.filerepo;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import de.slag.common.base.AdmCache;
import de.slag.common.base.BaseException;
import de.slag.common.utils.CsvUtils;

@Service
public class PortfolioPositionFileRepoServiceImpl implements PortfolioPositionFileRepoService {

	private static final String PORTFOLIO = "PORTFOLIO";

	private static final String DE_SLAG_INVEST_FILEREPO = "de.slag.invest.filerepo";

	@Resource
	private AdmCache admCache;

	@PostConstruct
	public void setUp() {
		final String fileRepoPathName = admCache.getValue(DE_SLAG_INVEST_FILEREPO)
				.orElseThrow(() -> new BaseException("not configured: '%s'", DE_SLAG_INVEST_FILEREPO));
		if (!new File(fileRepoPathName).exists()) {
			throw new BaseException("path to file repo does not exists: '%s'", fileRepoPathName);
		}
	}

	public Collection<PortfolioPositionDto> findAll() {
		final File fileRepoFolder = new File(admCache.getValue(DE_SLAG_INVEST_FILEREPO).get());
		final List<File> portfolioFiles = Arrays.asList(fileRepoFolder.listFiles()).stream()
				.filter(file -> file.getName().startsWith(PORTFOLIO)).collect(Collectors.toList());
		
		return portfolioFiles.stream()
				.map(file -> toPortfolioDto(file))
				.flatMap(portfolioPostionDtos -> portfolioPostionDtos.stream())
				.collect(Collectors.toList());
	}

	private Collection<PortfolioPositionDto> toPortfolioDto(File portfolioFile) {
		String portfolioNumber = portfolioFile.getName().substring(PORTFOLIO.length() + 1);

		Collection<CSVRecord> records = CsvUtils.getRecords(portfolioFile.getAbsolutePath());
		return records.stream().map(rec -> {
			PortfolioPositionDto portfolioPositionDto = new PortfolioPositionDto();
			portfolioPositionDto.setPortfolioNumber(portfolioNumber);
			portfolioPositionDto.setIsin(rec.get("ISIN"));
			portfolioPositionDto.setCount(Integer.valueOf(rec.get("COUNT")));
			return portfolioPositionDto;
		}).collect(Collectors.toList());
	}

}
