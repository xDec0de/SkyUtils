package net.codersky.skyutils.spigot.time;

import net.codersky.skyutils.time.Task;
import org.bukkit.scheduler.BukkitTask;

public class SpigotTask implements Task {

	private final BukkitTask task;

	SpigotTask(BukkitTask task) {
		this.task = task;
	}

	@Override
	public void cancel() {
		task.cancel();
	}
}
