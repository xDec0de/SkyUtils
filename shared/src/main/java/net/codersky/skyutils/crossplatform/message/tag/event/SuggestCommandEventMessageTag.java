package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class SuggestCommandEventMessageTag implements EventMessageTag {

	public static final SuggestCommandEventMessageTag INSTANCE = new SuggestCommandEventMessageTag();
	private final String[] aliases = {"suggest_cmd", "suggest_command"};

	private SuggestCommandEventMessageTag() {}

	@Override
	public @NotNull Component apply(@NotNull MessageTarget target, @NotNull Component component, @NotNull String context) {
		if (target != MessageTarget.CHAT)
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
