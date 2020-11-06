package de.slag.staging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import de.slag.staging.model.SecurityCsv;

public final class StagingModelUtils {
	
	public static void writeOut(List<List<String>> csvLines, String outputFolder) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.join(";", SecurityCsv.STRUCTURE));
		sb.append("\n");
		csvLines.forEach(line -> {
			sb.append(String.join(";", line));
			sb.append("\n");
		});

		try {
			Path path = Paths.get(outputFolder + "/securities-" + System.currentTimeMillis() + ".csv");
			byte[] strToBytes = sb.toString().getBytes();

			Files.write(path, strToBytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
