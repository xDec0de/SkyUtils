package net.codersky.skyutils.crossplatform.message.tag.filter;

import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerMessageFilter implements MessageFilter {

	public final static PlayerMessageFilter INSTANCE = new PlayerMessageFilter();
	private final String[] aliases = {"player"};

	private PlayerMessageFilter() {}

	@Override
	public boolean filter(@NotNull MessageReceiver receiver) {
		return receiver instanceof SkyPlayer;
	}

	@Override
	public @NotNull String getKey() {
		return "p";
	}

	@Override
	public @NotNull String @NotNull [] getAliases() {
		return aliases;
	}
}
