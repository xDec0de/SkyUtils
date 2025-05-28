package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class OpenUrlMessageEvent implements MessageEvent {

	public static final OpenUrlMessageEvent INSTANCE = new OpenUrlMessageEvent();
	private final String[] aliases = {"open_url"};

	private OpenUrlMessageEvent() {}

	@Override
	public @NotNull Component apply(@NotNull MessageTarget type, @NotNull Component component, @NotNull String context) {
		if (type != MessageTarget.CHAT)
			return component;
		return component.clickEvent(ClickEvent.openUrl(context));
	}

	@Override
	public @NotNull String getKey() {
		return "url";
	}

	@Override
	public @NotNull String @NotNull [] getAliases() {
		return aliases;
	}
}
