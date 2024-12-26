package net.codersky.skyutils.velocity.time;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.Scheduler;
import net.codersky.skyutils.time.TaskScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class VelocityTaskScheduler implements TaskScheduler {

	private final Scheduler scheduler;
	private final Object plugin;

	public VelocityTaskScheduler(@NotNull ProxyServer server, @NotNull Object plugin) {
		this.scheduler = server.getScheduler();
		this.plugin = plugin;
	}

	@NotNull
	@Override
	public VelocityTask runSync(@NotNull Runnable task) {
		return runAsync(task);
	}

	@NotNull
	@Override
	public VelocityTask delaySync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay) {
		return delayAsync(task, unit, delay);
	}

	@NotNull
	@Override
	public VelocityTask repeatSync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay, int repeat) {
		return repeatAsync(task, unit, delay, repeat);
	}

	@NotNull
	@Override
	public VelocityTask runAsync(@NotNull Runnable task) {
		return new VelocityTask(scheduler.buildTask(plugin, task).schedule());
	}

	@NotNull
	@Override
	public VelocityTask delayAsync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay) {
		return new VelocityTask(scheduler.buildTask(plugin, task).delay(delay, unit).schedule());
	}

	@NotNull
	@Override
	public VelocityTask repeatAsync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay, int repeat) {
		return new VelocityTask(scheduler.buildTask(plugin, task)
				.delay(delay, unit)
				.repeat(repeat, unit)
				.schedule());
	}
}
