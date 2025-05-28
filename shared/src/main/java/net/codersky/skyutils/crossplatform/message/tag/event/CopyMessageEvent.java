package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class CopyMessageEvent implements MessageEvent {

	public static final CopyMessageEvent INSTANCE = new CopyMessageEvent();
	private final String[] aliases = {"copy_to_clipboard"};

	private CopyMessageEvent() {}

	@Override
	public @NotNull Component apply(@NotNull MessageType type, @NotNull Component component, @NotNull String context) {
		if (type != MessageType.CHAT)
			return component;
		return component.clickEvent(ClickEvent.copyToClipboard(context));
	}

	@Override
	public @NotNull String getKey() {
		return "copy";
	}

	@Override
	public @NotNull String @NotNull [] getAliases() {
		return aliases;
	}
}
