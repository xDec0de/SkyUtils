package net.codersky.skyutils.spigot.time;

import net.codersky.skyutils.time.TaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * The Spigot platform {@link TaskScheduler}.
 *
 * @since SkyUtils 1.0.0
 */
public class SpigotTaskScheduler implements TaskScheduler {

	private final JavaPlugin plugin;

	public SpigotTaskScheduler(@NotNull JavaPlugin plugin) {
		this.plugin = plugin;
	}

	private long toTicks(@NotNull TimeUnit unit, int amount) {
		return unit.toMillis(amount) * 50;
	}

	@NotNull
	@Override
	public SpigotTask runSync(@NotNull Runnable task) {
		return new SpigotTask(Bukkit.getScheduler().runTask(plugin, task));
	}

	@NotNull
	@Override
	public SpigotTask delaySync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay) {
		return new SpigotTask(Bukkit.getScheduler().runTaskLater(plugin, task, toTicks(unit, delay)));
	}

	@NotNull
	@Override
	public SpigotTask repeatSync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay, int repeat) {
		return new SpigotTask(Bukkit.getScheduler().runTaskTimer(plugin, task, toTicks(unit, delay), toTicks(unit, repeat)));
	}

	@NotNull
	@Override
	public SpigotTask runAsync(@NotNull Runnable task) {
		return new SpigotTask(Bukkit.getScheduler().runTaskAsynchronously(plugin, task));
	}

	@NotNull
	@Override
	public SpigotTask delayAsync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay) {
		return new SpigotTask(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, unit.toMillis(delay) * 50));
	}

	@NotNull
	@Override
	public SpigotTask repeatAsync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay, int repeat) {
		return new SpigotTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, toTicks(unit, delay), toTicks(unit, repeat)));
	}
}
