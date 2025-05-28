package net.codersky.skyutils.crossplatform.message.tag.filter;

import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.SkyConsole;
import org.jetbrains.annotations.NotNull;

public class ConsoleFilterMessageTag implements FilterMessageTag {

	public static final ConsoleFilterMessageTag INSTANCE = new ConsoleFilterMessageTag();
	private final String[] aliases = {"console"};

	private ConsoleFilterMessageTag() {};

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
