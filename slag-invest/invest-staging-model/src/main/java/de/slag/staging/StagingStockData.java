package de.slag.staging;

import java.time.LocalDateTime;

public interface StagingStockData {

	LocalDateTime getDateTime();

	double getOpen();

	double getClose();

	double getHigh();

	double getLow();

	long getVolume();

}
