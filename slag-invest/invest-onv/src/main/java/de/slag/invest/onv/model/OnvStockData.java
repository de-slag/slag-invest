package de.slag.invest.onv.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public interface OnvStockData {

	String DATUM = "Datum";
	String DATUM_FORMAT = "dd.mm.yyyy";

	String EROEFFUNG = "Eroeffnung";
	String HOCH = "Hoch";
	String TIEF = "Tief";
	String SCHLUSS = "Schluss";
	String VOLUMEN = "Volumen";

	List<String> CSV_STRUCTURE = Arrays.asList(DATUM, EROEFFUNG, HOCH, TIEF, SCHLUSS, VOLUMEN);

	LocalDate getDatum();

	double getEroeffnung();

	double getHoch();

	double getTief();

	double getSchluss();
	
	long getVolume();

}
