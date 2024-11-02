package net.codersky.mcutils.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import net.codersky.mcutils.crossplatform.MCConsole;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class VelocityConsole implements MCConsole {

	private final ConsoleCommandSource handle;

	VelocityConsole(@NotNull ConsoleCommandSource handle) {
		this.handle = handle;
	}

	@NotNull
	@Override
	public ConsoleCommandSource getHandle() {
		return handle;
	}

	/**
	 * Gets the name of this {@link VelocityConsole}, which just
	 * returns "Console".
	 *
	 * @return The name of this {@link VelocityConsole}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Override
	public @NotNull String getName() {
		return "Console";
	}

	@Override
	public boolean sendMessage(@NotNull String message) {
		return sendMessage(Component.text(message));
	}

	@Override
	public boolean sendMessage(@NotNull Component message) {
		handle.sendMessage(message);
		return true;
	}
}
