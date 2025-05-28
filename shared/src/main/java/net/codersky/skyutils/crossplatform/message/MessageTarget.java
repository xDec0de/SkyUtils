package net.codersky.skyutils.crossplatform.message;

import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.SkyConsole;
import org.jetbrains.annotations.NotNull;

public enum MessageTarget {

	CHAT,
	ACTIONBAR;

	public boolean isSupportedBy(@NotNull final MessageReceiver receiver) {
		if (receiver instanceof SkyConsole)
			return this == CHAT;
		return true;
	}
}
