package net.codersky.skyutils.spigot;

import net.codersky.skyutils.spigot.player.SpigotPlayerProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The SkyUtils Spigot plugin class. This class holds the
 * {@link SpigotPlayerProvider} for all plugins. Its only purpose
 * is to register said provider so plugins can get players, and also
 * so SkyUtils can be used as a Spigot plugin.
 *
 * @since SkyUtils 1.0.0
 */
public class SkyUtilsSpigot extends JavaPlugin {

	static SkyUtilsSpigot instance;
	final SpigotPlayerProvider playerProvider = new SpigotPlayerProvider();

	public SkyUtilsSpigot() {
		instance = this;
	}

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(playerProvider, this);
	}
}
