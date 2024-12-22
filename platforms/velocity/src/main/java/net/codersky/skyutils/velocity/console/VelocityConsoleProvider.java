package net.codersky.skyutils.velocity.console;

import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;

public class VelocityConsoleProvider {

	private static VelocityConsole console;

	private VelocityConsoleProvider() {}

	@NotNull
	public static VelocityConsole getConsole(@NotNull ProxyServer proxy) {
		if (console == null)
			console = new VelocityConsole(proxy.getConsoleCommandSource());
		return console;
	}
}
