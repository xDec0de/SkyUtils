package net.codersky.mcutils.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.codersky.mcutils.MCPlatform;
import net.codersky.mcutils.cmd.GlobalCommand;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.codersky.mcutils.crossplatform.player.PlayerProvider;
import net.codersky.mcutils.crossplatform.proxy.ProxyUtils;
import net.codersky.mcutils.java.MCCollections;
import net.codersky.mcutils.velocity.cmd.AdaptedVelocityCommand;
import net.codersky.mcutils.velocity.cmd.VelocityCommand;
import net.codersky.mcutils.velocity.player.VelocityPlayerProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

public class VelocityUtils<P> extends ProxyUtils<P> {

	private final ProxyServer proxy;
	private final VelocityConsole console;
	private PlayerProvider<Player> playerProvider;
	private final Path dataDirectory;

	public VelocityUtils(@NotNull P plugin, @NotNull ProxyServer proxy, @NotNull Path dataDirectory) {
		super(plugin);
		this.proxy = Objects.requireNonNull(proxy);
		this.console = new VelocityConsole(proxy.getConsoleCommandSource());
		this.playerProvider = new VelocityPlayerProvider(proxy);
		this.dataDirectory = dataDirectory;
	}

	@NotNull
	public final ProxyServer getProxy() {
		return this.proxy;
	}

	@NotNull
	public VelocityUtils<P> setPlayerProvider(@NotNull PlayerProvider<Player> playerProvider) {
		this.playerProvider = Objects.requireNonNull(playerProvider, "Player provider cannot be null");
		return this;
	}

	@NotNull
	public PlayerProvider<Player> getPlayerProvider() {
		return playerProvider;
	}

	@NotNull
	@Override
	public File getDataFolder() {
		return this.dataDirectory.toFile();
	}

	@Nullable
	public MCPlayer getPlayer(@NotNull UUID uuid) {
		return playerProvider.getPlayer(uuid);
	}

	@Override
	public @NotNull VelocityConsole getConsole() {
		return console;
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
		registerCommands(MCCollections.map(
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
