package net.codersky.skyutils.velocity.cmd;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import net.codersky.skyutils.cmd.MCCommandSender;
import net.codersky.skyutils.crossplatform.player.MCPlayer;
import net.codersky.skyutils.velocity.VelocityConsole;
import net.codersky.skyutils.velocity.VelocityUtils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VelocityCommandSender implements MCCommandSender {

	private final CommandSource source;
	private final VelocityUtils<?> utils;

	public VelocityCommandSender(@NotNull CommandSource source, @NotNull VelocityUtils<?> utils) {
		this.source = source;
		this.utils = utils;
	}

	/*
	 * Player related
	 */

	@Override
	public boolean isPlayer() {
		return source instanceof Player;
	}

	@Nullable
	@Override
	public MCPlayer asPlayer() {
		return source instanceof Player player ? utils.getPlayer(player.getUniqueId()) : null;
	}

	@Nullable
	@Override
	public Player asPlayerHandle() {
		return source instanceof Player player ? player : null;
	}

	/*
	 * Console related
	 */

	@Nullable
	@Override
	public VelocityConsole asConsole() {
		return isConsole() ? utils.getConsole() : null;
	}

	@Nullable
	@Override
	public ConsoleCommandSource asConsoleHandle() {
		return source instanceof ConsoleCommandSource console ? console : null;
	}

	/*
	 * Messages
	 */

	/**
	 * Gets the name of this {@link VelocityCommandSender}. If
	 * {@link #isPlayer()} is {@code true}, {@link Player#getUsername()}
	 * is used, otherwise <i>"Console"</i> is returned.
	 *
 	 * @return The name of this {@link VelocityCommandSender}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	@Override
	public String getName() {
		final Player player = asPlayerHandle();
		return player == null ? "Console" : player.getUsername();
	}

	@Override
	public boolean sendMessage(@NotNull String message) {
		if (canReceive(message))
			source.sendPlainMessage(message);
		return true;
	}

	@Override
	public boolean sendMessage(@NotNull Component message) {
		if (canReceive(message))
			source.sendMessage(message);
		return true;
	}

	/*
	 * Utilities
	 */

	@Override
	public boolean hasPermission(@NotNull String permission) {
		return source.hasPermission(permission);
	}

	@NotNull
	@Override
	public VelocityUtils<?> getUtils() {
		return utils;
	}
}
