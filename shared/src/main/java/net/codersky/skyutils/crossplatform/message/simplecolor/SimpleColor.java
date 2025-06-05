package net.codersky.skyutils.crossplatform.message.simplecolor;

import org.jetbrains.annotations.NotNull;

public interface SimpleColor {

	@NotNull
	String apply(@NotNull final String str);

	@NotNull
	String strip(@NotNull final String str);
}
