package net.codersky.skyutils.crossplatform.message.tag;

import org.jetbrains.annotations.NotNull;

public interface MessageTag {

	boolean matches(@NotNull String name);
}
