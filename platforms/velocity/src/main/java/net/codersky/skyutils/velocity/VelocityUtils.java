package net.codersky.skyutils.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.codersky.skyutils.MCPlatform;
import net.codersky.skyutils.cmd.GlobalCommand;
import net.codersky.skyutils.crossplatform.player.MCPlayer;
import net.codersky.skyutils.crossplatform.proxy.ProxyUtils;
import net.codersky.skyutils.java.SkyCollections;
import net.codersky.skyutils.velocity.cmd.AdaptedVelocityCommand;
import net.codersky.skyutils.velocity.cmd.VelocityCommand;
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
	 * Gets an {@link MCPlayer} by {@link UUID} from the
	 * {@link #getPlayerProvider() VelocityPlayerProvider}
	 * that this {@link VelocityUtils} is using.
	 *
	 * @param uuid The {@link UUID} of the player to get.
	 *
	 * @return A possibly {@code null} {@link MCPlayer} instance of an online
	 * {@link Player} that matches the provided {@link UUID}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #getPlayerProvider()
	 * @see #getPlayer(Player)
	 */
	@Nullable
	public MCPlayer getPlayer(@NotNull UUID uuid) {
		return getPlayerProvider().getPlayer(uuid);
	}

	/**
	 * Gets an {@link MCPlayer} by {@link Player} from the
	 * {@link #getPlayerProvider() VelocityPlayerProvider}
	 * that this {@link VelocityUtils} is using.
	 *
	 * @param velocity The {@link Player} instance to convert.
	 *
	 * @return A {@link MCPlayer} instance that matches the provided {@link Player}.
	 * This can be {@code null} if you use an instance of a {@link Player} that
	 * is not {@link Player#isActive() online}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #getPlayerProvider()
	 * @see #getPlayer(Player)
	 */
	@Nullable
	public MCPlayer getPlayer(@NotNull Player velocity) {
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

	@Override
	@SuppressWarnings("unchecked")
	public void registerCommands(GlobalCommand<P>... commands) {
		if (commands == null || commands.length == 0)
			return;
		registerCommands(SkyCollections.map(
				commands,
				new AdaptedVelocityCommand[commands.length],
				cmd -> new AdaptedVelocityCommand<>(this, cmd))
		);
	}

	@SuppressWarnings("unchecked")
	public void registerCommands(VelocityCommand<P>... commands) {
		final CommandManager manager = getProxy().getCommandManager();
		for (VelocityCommand<P> command : commands) {
			final CommandMeta meta = manager.metaBuilder(command.getName())
					.plugin(getPlugin())
					.aliases(command.getAliasesArray())
					.build();
			manager.register(meta, command);
		}
	}
}
