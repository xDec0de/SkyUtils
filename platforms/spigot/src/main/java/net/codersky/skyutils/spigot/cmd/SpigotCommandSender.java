package net.codersky.skyutils.spigot.cmd;

import net.codersky.skyutils.cmd.MCCommandSender;
import net.codersky.skyutils.crossplatform.SkyConsole;
import net.codersky.skyutils.crossplatform.player.MCPlayer;
import net.codersky.skyutils.spigot.SpigotConsole;
import net.codersky.skyutils.spigot.SpigotUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpigotCommandSender implements MCCommandSender {

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
	public MCPlayer asPlayer() {
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

	@Override
	public boolean sendMessage(@NotNull Component message) {
		// MCPlayer & SkyConsole implementations will already check if the message
		// can be received. Not doing it here to avoid double-checking
		final MCPlayer player = asPlayer();
		if (player != null)
			return player.sendMessage(message);
		final SkyConsole console = asConsole();
		if (console != null)
			return console.sendMessage(message);
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
