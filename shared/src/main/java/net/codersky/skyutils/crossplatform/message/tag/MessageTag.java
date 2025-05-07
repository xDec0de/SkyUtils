package net.codersky.skyutils.crossplatform.message.tag;

import org.jetbrains.annotations.NotNull;

public interface MessageTag {

	String[] NO_ARGS = new String[0];

	@NotNull
	String getKey();

	@NotNull
	default String @NotNull [] getAliases() {
		return NO_ARGS;
	}
}
