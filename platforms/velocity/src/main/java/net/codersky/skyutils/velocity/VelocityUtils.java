package net.codersky.skyutils.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.codersky.jsky.collections.JCollections;
import net.codersky.skyutils.MCPlatform;
import net.codersky.skyutils.cmd.GlobalCommand;
import net.codersky.skyutils.crossplatform.proxy.ProxyUtils;
import net.codersky.skyutils.velocity.cmd.AdaptedVelocityCommand;
import net.codersky.skyutils.velocity.cmd.CustomVelocityCommand;
import net.codersky.skyutils.velocity.cmd.VelocityCommandSender;
import net.codersky.skyutils.velocity.console.VelocityConsole;
import net.codersky.skyutils.velocity.console.VelocityConsoleProvider;
import net.codersky.skyutils.velocity.player.VelocityPlayer;
import net.codersky.skyutils.velocity.player.VelocityPlayerProvider;
import net.codersky.skyutils.velocity.time.VelocityTaskScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class VelocityUtils<P> extends ProxyUtils<P> {

	private final ProxyServer proxy;
	private final Path dataDirectory;
	private final VelocityTaskScheduler scheduler;

	public VelocityUtils(@NotNull P plugin, @NotNull ProxyServer proxy, @NotNull Path dataDirectory) {
		super(plugin);
		this.proxy = Objects.requireNonNull(proxy);
		this.dataDirectory = dataDirectory;
		this.scheduler = new VelocityTaskScheduler(proxy, plugin);
	}

	@NotNull
	public final ProxyServer getProxy() {
		return this.proxy;
	}

	/*
	 - Player provider
	 */

	@NotNull
	public VelocityPlayerProvider getPlayerProvider() {
		return SkyUtilsVelocity.getInstance().getPlayerProvider();
	}

	@NotNull
	@Override
	public Collection<? extends VelocityPlayer> getOnlinePlayers() {
		return getPlayerProvider().getOnlineCache();
	}

	@Nullable
	@Override
	public VelocityPlayer getPlayer(@NotNull UUID uuid) {
		return getPlayerProvider().getOnline(uuid);
	}

	@Nullable
	public VelocityPlayer getPlayer(@NotNull Player handle) {
		return getPlayer(handle.getUniqueId());
	}

	@Nullable
	@Override
	public VelocityPlayer getPlayer(@NotNull String name) {
		final Player on = getProxy().getPlayer(name).orElse(null);
		return on == null ? null : getPlayer(on);
	}

	@NotNull
	@Override
	public File getDataFolder() {
		return this.dataDirectory.toFile();
	}

	@NotNull
	@Override
	public VelocityConsole getConsole() {
		return VelocityConsoleProvider.getConsole(proxy);
	}

	@Override
	public @NotNull MCPlatform getPlatform() {
		return MCPlatform.VELOCITY;
	}

	/*
	 - Command registration
	 */

	/**
	 * Registers all provided {@code commands} to the {@link #getProxy() proxy}.
	 * On the Velocity platform command registration is pretty straightforward
	 * and should just work without any issues.
	 *
	 * @param commands The {@link CustomVelocityCommand commands} to register.
	 *
	 * @return Always {@code true} as all commands are expected to register
	 * successfully on Velocity.
	 */
	@SafeVarargs
	public final boolean registerCommands(CustomVelocityCommand<P, ? extends VelocityCommandSender>... commands) {
		final CommandManager manager = getProxy().getCommandManager();
		for (CustomVelocityCommand<P, ? extends VelocityCommandSender> command : commands) {
			final CommandMeta meta = manager.metaBuilder(command.getName())
					.plugin(getPlugin())
					.aliases(command.getAliasesArray())
					.build();
			manager.register(meta, command);
		}
		return true;
	}

	/**
	 * Adapts all {@link GlobalCommand commands} to {@link AdaptedVelocityCommand},
	 * then registers all of them with the {@link #registerCommands(CustomVelocityCommand[])}
	 * method. Details about how command registration works on Velocity are provided on
	 * said method.
	 *
	 * @param commands The {@link GlobalCommand commands} to register.
	 *
	 * @return {@code true} if all commands were registered successfully,
	 * {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #registerCommands(CustomVelocityCommand[])
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean registerCommands(@NotNull GlobalCommand<P>... commands) {
		return registerCommands(JCollections.map(
				commands,
				new AdaptedVelocityCommand[commands.length],
				cmd -> new AdaptedVelocityCommand<>(this, cmd))
		);
	}

	@Override
	public boolean unregisterCommand(@NotNull String name) {
		final CommandManager manager = getProxy().getCommandManager();
		if (!manager.hasCommand(name))
			return false;
		manager.unregister(name);
		return true;
	}

	@NotNull
	@Override
	public VelocityTaskScheduler getScheduler() {
		return scheduler;
	}
}
