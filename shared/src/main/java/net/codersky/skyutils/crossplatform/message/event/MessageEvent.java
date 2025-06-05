package net.codersky.skyutils.crossplatform.message.event;

import org.jetbrains.annotations.NotNull;

public abstract class MessageEvent {

	private final String name;
	private final String content;

	MessageEvent(final String name, final String content) {
		this.name = name;
		this.content = content;
	}

	@NotNull
	public String getName() {
		return name;
	}

	@NotNull
	public String getContent() {
		return content;
	}
}
