package net.codersky.skyutils.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.codersky.skyutils.MCPlatform;
import net.codersky.skyutils.cmd.GlobalCommand;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.codersky.skyutils.crossplatform.proxy.ProxyUtils;
import net.codersky.skyutils.java.SkyCollections;
import net.codersky.skyutils.velocity.cmd.AdaptedVelocityCommand;
import net.codersky.skyutils.velocity.cmd.CustomVelocityCommand;
import net.codersky.skyutils.velocity.cmd.VelocityCommandSender;
import net.codersky.skyutils.velocity.player.VelocityPlayerProvider;
import net.codersky.skyutils.velocity.player.VelocityPlayerQuitListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

public class VelocityUtils<P> extends ProxyUtils<P> {

	private final ProxyServer proxy;
	private VelocityPlayerProvider playerProvider;
	private final Path dataDirectory;
	private boolean isPlayerListenerOn = false;

	public VelocityUtils(@NotNull P plugin, @NotNull ProxyServer proxy, @NotNull Path dataDirectory) {
		super(plugin);
		this.proxy = Objects.requireNonNull(proxy);
		this.playerProvider = new VelocityPlayerProvider(proxy);
		this.dataDirectory = dataDirectory;
	}

	@NotNull
	public final ProxyServer getProxy() {
		return this.proxy;
	}

	@NotNull
	public VelocityUtils<P> setPlayerProvider(@NotNull VelocityPlayerProvider playerProvider) {
		this.playerProvider = Objects.requireNonNull(playerProvider, "Player provider cannot be null");
		return this;
	}

	/**
	 * Gets the {@link VelocityPlayerProvider} being used by this {@link VelocityUtils} instance.
	 *
	 * @return The {@link VelocityPlayerProvider} being used by this {@link VelocityUtils} instance.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #setPlayerProvider(VelocityPlayerProvider)
	 * @see #getPlayer(UUID)
	 * @see #getPlayer(Player)
	 */
	@NotNull
	public VelocityPlayerProvider getPlayerProvider() {
		if (!isPlayerListenerOn) {
			proxy.getEventManager().register(getPlugin(), new VelocityPlayerQuitListener(this));
			isPlayerListenerOn = true;
		}
		return playerProvider;
	}

	/**
	 * Gets an {@link SkyPlayer} by {@link UUID} from the
	 * {@link #getPlayerProvider() VelocityPlayerProvider}
	 * that this {@link VelocityUtils} is using.
	 *
	 * @param uuid The {@link UUID} of the player to get.
	 *
	 * @return A possibly {@code null} {@link SkyPlayer} instance of an online
	 * {@link Player} that matches the provided {@link UUID}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #getPlayerProvider()
	 * @see #getPlayer(Player)
	 */
	@Nullable
	public SkyPlayer getPlayer(@NotNull UUID uuid) {
		return getPlayerProvider().getPlayer(uuid);
	}

	/**
	 * Gets an {@link SkyPlayer} by {@link Player} from the
	 * {@link #getPlayerProvider() VelocityPlayerProvider}
	 * that this {@link VelocityUtils} is using.
	 *
	 * @param velocity The {@link Player} instance to convert.
	 *
	 * @return A {@link SkyPlayer} instance that matches the provided {@link Player}.
	 * This can be {@code null} if you use an instance of a {@link Player} that
	 * is not {@link Player#isActive() online}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #getPlayerProvider()
	 * @see #getPlayer(Player)
	 */
	@Nullable
	public SkyPlayer getPlayer(@NotNull Player velocity) {
		return getPlayerProvider().getPlayer(velocity);
	}

	@NotNull
	@Override
	public File getDataFolder() {
		return this.dataDirectory.toFile();
	}

	@NotNull
	@Override
	protected VelocityConsole getConsoleInstance() {
		return new VelocityConsole(getProxy().getConsoleCommandSource());
	}

	@NotNull
	@Override
	public VelocityConsole getConsole() {
		return (VelocityConsole) console;
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
		return registerCommands(SkyCollections.map(
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
}
