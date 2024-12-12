package net.codersky.skyutils.spigot.cmd;

import net.codersky.skyutils.spigot.SpigotUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class SpigotCommand<P extends JavaPlugin> extends CustomSpigotCommand<P, SpigotCommandSender> {

	public SpigotCommand(@NotNull SpigotUtils<P> utils, @NotNull String name) {
		super(utils, name);
	}

	public SpigotCommand(@NotNull SpigotUtils<P> utils, @NotNull String name, @NotNull String... aliases) {
		super(utils, name, aliases);
	}

	@Override
	protected final SpigotCommandSender getSender(@NotNull CommandSender sender) {
		return new SpigotCommandSender(sender, getUtils());
	}
}
