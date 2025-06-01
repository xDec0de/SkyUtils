package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class SuggestCommandEventMessageTag implements EventMessageTag {

	public static final SuggestCommandEventMessageTag INSTANCE = new SuggestCommandEventMessageTag();

	private SuggestCommandEventMessageTag() {}

	@Override
	public @NotNull Component apply(@NotNull MessageTarget target, @NotNull Component component, @NotNull String context) {
		if (target != MessageTarget.CHAT)
			return component;
		return component.clickEvent(ClickEvent.suggestCommand(context));
	}

	@Override
	public boolean matches(@NotNull String name) {
		return name.equals("suggest") || name.equals("suggest_cmd") || name.equals("suggest_command");
	}
}
