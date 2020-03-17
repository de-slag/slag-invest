package de.slag.invest.webservice.it;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LitReporter implements AutoCloseable {

	private List<String> messages = new ArrayList<>();

	private Map<String, Long> openEvents = new HashMap<>();

	private long sum = 0;

	public void start(String event) {
		openEvents.put(event, System.currentTimeMillis());
	}

	public void end(String event, String result) {
		if (!openEvents.containsKey(event)) {
			messages.add(String.format("(no valid event: '%s', result: '%s')", event, result));
			return;
		}

		final Long start = openEvents.remove(event);
		final long duration = System.currentTimeMillis() - start;
		sum = sum + duration;
		messages.add(String.format("[%s]: %s (%s ms)", result, event, duration));
	}

	@Override
	public void close() throws Exception {
		final Set<String> keySet = openEvents.keySet();
		final ArrayList<String> all = new ArrayList<>(keySet);
		all.forEach(e -> end(e, "unfinished"));

		messages.add(String.format("sum: %s s", sum / 1000.0));

	}

	public void end(String event) {
		end(event, "OK");
	}

	@Override
	public String toString() {
		return "REPORT:\n" + String.join("\n", messages);
	}
}
