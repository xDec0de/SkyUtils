package net.codersky.skyutils.spigot.cmd;

import net.codersky.skyutils.cmd.MCCommand;
import net.codersky.skyutils.cmd.SubCommandHandler;
import net.codersky.skyutils.spigot.SpigotUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;

public abstract class CustomSpigotCommand<P extends JavaPlugin, S extends SpigotCommandSender> extends Command implements MCCommand<P, S>, PluginIdentifiableCommand, TabExecutor {

	private final SpigotUtils<P> utils;
	private final SubCommandHandler<P, S> subCommandHandler = new SubCommandHandler<>();

	public CustomSpigotCommand(@NotNull SpigotUtils<P> utils, @NotNull String name) {
		super(name);
		this.utils = utils;
	}

	public CustomSpigotCommand(@NotNull SpigotUtils<P> utils, @NotNull String name, @NotNull String... aliases) {
		this(utils, name);
		super.setAliases(Arrays.asList(aliases));
	}

	/*
	 - Utils & plugin access
	 */

	@NotNull
	@Override
	public SpigotUtils<P> getUtils() {
		return utils;
	}

	@NotNull
	@Override
	public final P getPlugin() {
		return utils.getPlugin();
	}

	/*
	 - Sender getter
	 */

	protected abstract S getSender(@NotNull CommandSender sender);

	/*
	 - Subcommand injection
	 */

	@NotNull
	@Override
	public CustomSpigotCommand<P, S> inject(@NotNull MCCommand<P, S>... commands) {
		subCommandHandler.inject(commands);
		return this;
	}

	/*
	 - Command execution
	 */

	@Override
	@Deprecated
	@ApiStatus.Internal
	public final boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		return subCommandHandler.onCommand(this, getSender(sender), args);
	}

	@Override
	@Deprecated
	@ApiStatus.Internal
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return execute(sender, label, args);
	}

	/*
	 - Tab complete
	 */

	@NotNull
	@Override
	@Deprecated
	@ApiStatus.Internal
	public final List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) {
		return subCommandHandler.onTab(this, getSender(sender), args);
	}

	@NotNull
	@Override
	@Deprecated
	@ApiStatus.Internal
	public final List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return tabComplete(sender, label, args);
	}

	/*
	 - Argument conversion - Players
	 */

	/**
	 * Argument conversion method to get an <b>online</b> {@link Player}.
	 * The method used to check for a matching <b>online</b> {@link Player}
	 * is {@link Bukkit#getPlayerExact(String)}.
	 *
	 * @param arg The array position of the argument to get, can be out of bounds.
	 * @param args The array of arguments to use.
	 * @param def The default {@link Player} to return if {@code arg} is out of bounds or
	 * no <b>online</b> {@link Player} matches the found argument.
	 *
	 * @return The matching {@link Player} instance with the name found
	 * on position {@code arg} from the {@code args} array. If no <b>online</b>
	 * {@link Player} matches the provided name, or if the name hasn't been
	 * provided to begin with, {@code def} is returned.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public Player asPlayer(int arg, @NotNull String[] args, @NotNull Player def) {
		return asGeneric(Bukkit::getPlayerExact, arg, args, def);
	}

	/**
	 * Argument conversion method to get an <b>online</b> {@link Player}.
	 * The method used to check for a matching <b>online</b> {@link Player}
	 * is {@link Bukkit#getPlayerExact(String)}.
	 *
	 * @param arg The array position of the argument to get, can be out of bounds.
	 * @param args The array of arguments to use.
	 *
	 * @return The matching {@link Player} instance with the name found
	 * on position {@code arg} from the {@code args} array. If no <b>online</b>
	 * {@link Player} matches the provided name, or if the name hasn't been
	 * provided to begin with, {@code null} is returned.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	public Player asPlayer(int arg, @NotNull String[] args) {
		return asGeneric(Bukkit::getPlayerExact, arg, args);
	}

	/*
	 - Argument conversion - Offline players
	 */

	/**
	 * Argument conversion method to get an <b>online</b> or <b>offline</b>
	 * {@link OfflinePlayer}. The method used to check for a matching {@link OfflinePlayer}
	 * is {@link Bukkit#getOfflinePlayer(String)}. Then, {@link OfflinePlayer#hasPlayedBefore()}
	 * is used to check if the {@link OfflinePlayer} actually exists.
	 *
	 * @param arg The array position of the argument to get, can be out of bounds.
	 * @param args The array of arguments to use.
	 * @param def The default {@link OfflinePlayer} to return if {@code arg} is out of bounds or
	 * no {@link OfflinePlayer} matches the found argument.
	 *
	 * @return The matching {@link OfflinePlayer} instance with the name found
	 * on position {@code arg} from the {@code args} array. If no {@link OfflinePlayer}
	 * tha {@link OfflinePlayer#hasPlayedBefore() has played before} matches the provided
	 * name, or if the name hasn't been provided to begin with, {@code def} is returned.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public OfflinePlayer asOfflinePlayer(int arg, @NotNull String[] args, @NotNull OfflinePlayer def) {
		final String name = asString(arg, args);
		if (name == null)
			return def;
		@SuppressWarnings("deprecation")
		final OfflinePlayer off = Bukkit.getOfflinePlayer(name);
		return off.hasPlayedBefore() ? off : def;
	}

	/**
	 * Argument conversion method to get an <b>online</b> or <b>offline</b>
	 * {@link OfflinePlayer}. The method used to check for a matching {@link OfflinePlayer}
	 * is {@link Bukkit#getOfflinePlayer(String)}. Then, {@link OfflinePlayer#hasPlayedBefore()}
	 * is used to check if the {@link OfflinePlayer} actually exists.
	 *
	 * @param arg The array position of the argument to get, can be out of bounds.
	 * @param args The array of arguments to use.
	 *
	 * @return The matching {@link OfflinePlayer} instance with the name found
	 * on position {@code arg} from the {@code args} array. If no {@link OfflinePlayer}
	 * tha {@link OfflinePlayer#hasPlayedBefore() has played before} matches the provided
	 * name, or if the name hasn't been provided to begin with, {@code null} is returned.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	public OfflinePlayer asOfflinePlayer(int arg, @NotNull String[] args) {
		final String name = asString(arg, args);
		if (name == null)
			return null;
		@SuppressWarnings("deprecation")
		final OfflinePlayer off = Bukkit.getOfflinePlayer(name);
		return off.hasPlayedBefore() ? off : null;
	}
}
