package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class SuggestCommandMessageEvent implements EventMessageTag {

	public static final SuggestCommandMessageEvent INSTANCE = new SuggestCommandMessageEvent();
	private final String[] aliases = {"suggest_cmd", "suggest_command"};

	private SuggestCommandMessageEvent() {}

	@Override
	public @NotNull Component apply(@NotNull MessageTarget type, @NotNull Component component, @NotNull String context) {
		if (type != MessageTarget.CHAT)
			return component;
		return component.clickEvent(ClickEvent.suggestCommand(context));
	}

	@Override
	public @NotNull String getKey() {
		return "suggest";
	}

	@Override
	public @NotNull String @NotNull [] getAliases() {
		return aliases;
	}
}
