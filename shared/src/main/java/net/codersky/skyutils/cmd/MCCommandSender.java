package net.codersky.skyutils.cmd;

import net.codersky.skyutils.SkyUtils;
import net.codersky.skyutils.crossplatform.MCConsole;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.player.MCPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MCCommandSender extends MessageReceiver {

	/*
	 - Player related
	 */

	boolean isPlayer();

	@Nullable
	MCPlayer asPlayer();

	@Nullable
	default Object asPlayerHandle() {
		final MCPlayer player = asPlayer();
		return player == null ? null : player.getHandle();
	}

	/*
	 - Console related
	 */

	default boolean isConsole() {
		return !isPlayer();
	}

	@Nullable
	MCConsole asConsole();

	@Nullable
	default Object asConsoleHandle() {
		final MCConsole console = asConsole();
		return console == null ? null : console.getHandle();
	}

	/*
	 - Shared methods
	 */

	boolean hasPermission(@NotNull String permission);

	/*
	 - Utilities
	 */

	@NotNull
	SkyUtils<?> getUtils();
}
