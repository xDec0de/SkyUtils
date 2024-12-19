package net.codersky.skyutils.paper;

import net.codersky.skyutils.MCPlatform;
import net.codersky.skyutils.SkyUtils;
import net.codersky.skyutils.crossplatform.server.ServerUtils;
import net.codersky.skyutils.spigot.SpigotUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Paper platform extension of the {@link SpigotUtils} class,
 * which at the same time extends the {@link ServerUtils} and
 * {@link SkyUtils} classes. It offers access to the SkyUtils
 * API for Paper servers.
 * <p>
 * The use of the Spigot platform is recommended unless you
 * are making a plugin for Paper only, incompatible with Spigot.
 *
 * @param <P> The {@link JavaPlugin} that owns this utils.
 *
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 */
public class PaperUtils<P extends JavaPlugin> extends SpigotUtils<P> {

	public PaperUtils(@NotNull P plugin) {
		super(plugin);
	}

	@NotNull
	@Override
	public MCPlatform getPlatform() {
		return MCPlatform.PAPER;
	}

	/**
	 * Gets the {@link SimpleCommandMap} instance stored on the {@link Bukkit#getServer() server}.
	 * On paper, the {@link Server#getCommandMap()} method is exposed, so no reflection is required,
	 * however, the method does return a {@link CommandMap} instance instead of a {@link SimpleCommandMap}
	 * instance. This method only casts it safely, returning {@code null} if it isn't able to cast it.
	 *
	 * @return The {@link SimpleCommandMap} instance stored on the {@link Bukkit#getServer() server},
	 * {@code null} if any error occurs.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Override
	public SimpleCommandMap getCommandMap() {
		return Bukkit.getServer().getCommandMap() instanceof SimpleCommandMap map ? map : null;
	}
}
