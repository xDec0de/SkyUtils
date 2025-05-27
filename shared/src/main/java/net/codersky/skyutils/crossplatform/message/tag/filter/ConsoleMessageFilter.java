package net.codersky.skyutils.crossplatform.message.tag.filter;

import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.SkyConsole;
import org.jetbrains.annotations.NotNull;

public class ConsoleMessageFilter implements MessageFilter {

	public static final ConsoleMessageFilter INSTANCE = new ConsoleMessageFilter();
	private final String[] aliases = {"console"};

	private ConsoleMessageFilter() {};

	@Override
	public boolean filter(@NotNull MessageReceiver receiver) {
		return receiver instanceof SkyConsole;
	}

	@Override
	public @NotNull String getKey() {
		return "c";
	}

	@Override
	public @NotNull String @NotNull [] getAliases() {
		return aliases;
	}
}
