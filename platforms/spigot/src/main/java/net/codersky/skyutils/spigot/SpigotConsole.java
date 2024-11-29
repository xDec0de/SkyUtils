package net.codersky.skyutils.spigot;

import net.codersky.skyutils.crossplatform.MCConsole;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class SpigotConsole implements MCConsole {

	private final ConsoleCommandSender handle;

	protected SpigotConsole(@NotNull ConsoleCommandSender handle) {
		this.handle = handle;
	}

	@NotNull
	@Override
	public final ConsoleCommandSender getHandle() {
		return handle;
	}

	/**
	 * Gets the name of this {@link SpigotConsole}. This just
	 * falls back to {@link CommandSender#getName()}.
	 *
	 * @return The name of this {@link SpigotConsole}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Override
	public @NotNull String getName() {
		return handle.getName();
	}

	@Override
	public boolean sendMessage(@NotNull String message) {
		if (canReceive(message))
			handle.sendMessage(message);
		return true;
	}

	@Override
	public boolean sendMessage(@NotNull Component message) {
		if (canReceive(message))
			handle.sendMessage(LegacyComponentSerializer.legacySection().serialize(message));
		return true;
	}
}