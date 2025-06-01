package net.codersky.skyutils.crossplatform.message.tag.filter;

import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerFilterMessageTag implements FilterMessageTag {

	public final static PlayerFilterMessageTag INSTANCE = new PlayerFilterMessageTag();

	private PlayerFilterMessageTag() {}

	@Override
	public boolean filter(@NotNull MessageReceiver receiver) {
		return receiver instanceof SkyPlayer;
	}

	@Override
	public boolean matches(@NotNull String name) {
		return name.equals("p") || name.equals("player");
	}
}
