package de.slag.invest.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.common.utils.SleepUtils;

public class TimedExecutor {
	
	private static final Log LOG = LogFactory.getLog(TimedExecutor.class);

	private Collection<Runnable> tasksToRun = new ArrayList<>();

	private double tasksPerMinute;

	public TimedExecutor(double tasksPerMinute, Collection<? extends Runnable> tasks) {
		this.tasksPerMinute = tasksPerMinute;
		this.tasksToRun.addAll(tasks);
	}

	public void execute() {
		final int gapInMs = (int) (60000 / tasksPerMinute);
		final List<Runnable> tasksRunned = new ArrayList<Runnable>();

		for (Runnable runnable : tasksToRun) {
			runnable.run();
			tasksRunned.add(runnable);
			LOG.debug(String.format("tasks to run: %s, tasks runned: %s", tasksToRun.size(), tasksRunned.size()));
			if (tasksRunned.size() == tasksToRun.size()) {
				break;
			}
			LOG.info(String.format("wait %s ms", gapInMs));
			SleepUtils.sleepFor(gapInMs);
		}

	}

}
