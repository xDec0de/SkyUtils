package net.codersky.skyutils.spigot.cmd;

import net.codersky.skyutils.cmd.SkyCommandSender;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.codersky.skyutils.spigot.SpigotUtils;
import net.codersky.skyutils.spigot.console.SpigotConsole;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpigotCommandSender implements SkyCommandSender {

	private final CommandSender sender;
	private final SpigotUtils<?> utils;

	public SpigotCommandSender(@NotNull CommandSender sender, @NotNull SpigotUtils<?> utils) {
		this.sender = sender;
		this.utils = utils;
	}

	/*
	 * Player related
	 */

	@Override
	public boolean isPlayer() {
		return sender instanceof Player;
	}

	@Nullable
	@Override
	public Player asPlayerHandle() {
		return sender instanceof Player player ? player : null;
	}

	@Nullable
	@Override
	public SkyPlayer asPlayer() {
		return sender instanceof Player player ? utils.getPlayer(player.getUniqueId()) : null;
	}

	/*
	 * Console related
	 */

	@Nullable
	@Override
	public ConsoleCommandSender asConsoleHandle() {
		return sender instanceof ConsoleCommandSender console ? console : null;
	}

	@Nullable
	@Override
	public SpigotConsole asConsole() {
		return isConsole() ? utils.getConsole() : null;
	}

	/*
	 * Messages
	 */

	/**
	 * Gets the name of this {@link SpigotCommandSender}. This just
	 * falls back to {@link CommandSender#getName()}.
	 *
	 * @return The name of this {@link SpigotCommandSender}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	@Override
	public String getName() {
		return sender.getName();
	}

	@Override
	public boolean sendMessage(@NotNull String message) {
		if (canReceive(message))
			sender.sendMessage(message);
		return true;
	}

	/*
	 * Utilities
	 */

	@Override
	public boolean hasPermission(@NotNull String permission) {
		return sender.hasPermission(permission);
	}

	@Override
	public @NotNull SpigotUtils<?> getUtils() {
		return utils;
	}
}
