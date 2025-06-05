package net.codersky.skyutils.crossplatform.message.event;

import org.jetbrains.annotations.NotNull;

public class HoverEvent extends MessageEvent {

	HoverEvent(String name, String content) {
		super(name, content);
	}

	@NotNull
	public static HoverEvent copy(@NotNull final String text) {
		return new HoverEvent("copy", text);
	}

	@NotNull
	public static HoverEvent openFile(@NotNull final String path) {
		return new HoverEvent("file", path);
	}

	@NotNull
	public static HoverEvent openUrl(@NotNull final String url) {
		return new HoverEvent("url", url);
	}

	@NotNull
	public static HoverEvent runCommand(@NotNull final String command) {
		return new HoverEvent("run", command);
	}

	@NotNull
	public static HoverEvent showText(@NotNull final String command) {
		return new HoverEvent("txt", command);
	}

	@NotNull
	public static HoverEvent suggestCommand(@NotNull final String command) {
		return new HoverEvent("suggest", command);
	}
}
