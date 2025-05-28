package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class OpenUrlEventMessageTag implements EventMessageTag {

	public static final OpenUrlEventMessageTag INSTANCE = new OpenUrlEventMessageTag();
	private final String[] aliases = {"open_url"};

	private OpenUrlEventMessageTag() {}

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
