package net.codersky.skyutils.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import net.codersky.skyutils.crossplatform.MCConsole;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class VelocityConsole implements MCConsole {

	private final ConsoleCommandSource handle;

	protected VelocityConsole(@NotNull ConsoleCommandSource handle) {
		this.handle = handle;
	}

	@NotNull
	@Override
	public final ConsoleCommandSource getHandle() {
		return handle;
	}

	/**
	 * Gets the name of this {@link VelocityConsole}, which just
	 * returns "Console".
	 *
	 * @return The name of this {@link VelocityConsole}.
	 *
	 * @since SkyUtils 1.0.0
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
		if (canReceive(message))
			handle.sendMessage(message);
		return true;
	}
}
