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
	@ApiStatus.Internal
	public final boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		return subCommandHandler.onCommand(this, getSender(sender), args);
	}

	@Override
	@ApiStatus.Internal
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return execute(sender, label, args);
	}

	/*
	 - Tab complete
	 */

	@NotNull
	@Override
	@ApiStatus.Internal
	public final List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) {
		return subCommandHandler.onTab(this, getSender(sender), args);
	}

	@NotNull
	@Override
	@ApiStatus.Internal
	public final List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return tabComplete(sender, label, args);
	}

	/*
	 - Argument conversion - Players
	 */

	@Nullable
	public Player asPlayer(int arg, @NotNull String[] args, @Nullable Player def) {
		return asGeneric(Bukkit::getPlayerExact, arg, args, def);
	}

	@Nullable
	public Player asPlayer(int arg, @NotNull String[] args) {
		return asGeneric(Bukkit::getPlayerExact, arg, args);
	}

	/*
	 - Argument conversion - Offline players
	 */

	@Nullable
	public OfflinePlayer asOfflinePlayer(int arg, @NotNull String[] args, @Nullable OfflinePlayer def) {
		@SuppressWarnings("deprecation")
		final OfflinePlayer offline = Bukkit.getOfflinePlayer(args.length > arg ? args[arg] : null);
		return offline == null || !offline.hasPlayedBefore() ? def : offline;
	}

	@Nullable
	public OfflinePlayer asOfflinePlayer(int arg, @NotNull String[] args) {
		return asOfflinePlayer(arg, args, null);
	}
}
