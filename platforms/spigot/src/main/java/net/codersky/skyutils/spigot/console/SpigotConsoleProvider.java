package net.codersky.skyutils.spigot.console;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class SpigotConsoleProvider {

	private static SpigotConsole console;

	private SpigotConsoleProvider() {}

	@NotNull
	public static SpigotConsole getConsole() {
		if (console == null)
			console = new SpigotConsole(Bukkit.getConsoleSender());
		return console;
	}
}
