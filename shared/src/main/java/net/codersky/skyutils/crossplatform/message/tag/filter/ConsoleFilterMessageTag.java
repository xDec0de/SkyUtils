package net.codersky.skyutils.crossplatform.message.tag.filter;

import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.SkyConsole;
import org.jetbrains.annotations.NotNull;

public class ConsoleFilterMessageTag implements FilterMessageTag {

	public static final ConsoleFilterMessageTag INSTANCE = new ConsoleFilterMessageTag();

	private ConsoleFilterMessageTag() {};

	@Override
	public boolean filter(@NotNull MessageReceiver receiver) {
		return receiver instanceof SkyConsole;
	}

	@Override
	public boolean matches(@NotNull String name) {
		return name.equals("c") || name.equals("console");
	}
}
