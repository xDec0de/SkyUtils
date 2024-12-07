package net.codersky.skyutils.crossplatform;

import org.jetbrains.annotations.NotNull;

/**
 * {@code interface} used to represent a server or proxy console.
 *
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 */
public interface SkyConsole extends MessageReceiver {

	/**
	 * Gets the platform-specific object that is being wrapped by
	 * this {@link SkyConsole} instance. This can be, for example, a
	 * Bukkit ConsoleCommandSender instance.
	 *
	 * @return The platform-specific object that is being wrapped by
	 * this {@link SkyConsole} instance.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	Object getHandle();
}
