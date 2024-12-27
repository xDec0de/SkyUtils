package net.codersky.skyutils.spigot;

import net.codersky.skyutils.spigot.player.SpigotPlayerProvider;
import net.codersky.skyutils.spigot.time.SpigotTaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * The SkyUtils Spigot plugin class. This class holds the
 * {@link SpigotPlayerProvider} for all plugins. Its only purpose
 * is to register said provider so plugins can get players, and also
 * so SkyUtils can be used as a Spigot plugin.
 *
 * @since SkyUtils 1.0.0
 */
public class SkyUtilsSpigot extends JavaPlugin {

	private static SkyUtilsSpigot instance;
	private final SpigotPlayerProvider playerProvider;

	public SkyUtilsSpigot() {
		instance = this;
		playerProvider = new SpigotPlayerProvider(new SpigotTaskScheduler(this));
	}

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(playerProvider, this);
	}

	@NotNull
	public static SkyUtilsSpigot getInstance() {
		return instance;
	}

	@NotNull
	public SpigotPlayerProvider getPlayerProvider() {
		return playerProvider;
	}
}
