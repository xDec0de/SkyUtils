package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

public class ShowTextMessageEvent implements MessageEvent {

	public static final ShowTextMessageEvent INSTANCE = new ShowTextMessageEvent();
	private final String[] aliases = {"txt", "show", "show_text"};

	private ShowTextMessageEvent() {}

	@Override
	public @NotNull Component apply(@NotNull MessageType type, @NotNull Component component, @NotNull String context) {
		if (type != MessageType.CHAT)
			return component;
		return component.hoverEvent(HoverEvent.showText(Component.text(context)));
	}

	@Override
	public @NotNull String getKey() {
		return "text";
	}

	@Override
	public @NotNull String @NotNull [] getAliases() {
		return aliases;
	}
}
