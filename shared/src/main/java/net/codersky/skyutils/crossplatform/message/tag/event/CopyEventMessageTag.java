package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class CopyEventMessageTag implements EventMessageTag {

	public static final CopyEventMessageTag INSTANCE = new CopyEventMessageTag();
	private final String[] aliases = {"copy_to_clipboard"};

	private CopyEventMessageTag() {}

	@Override
	public @NotNull Component apply(@NotNull MessageTarget type, @NotNull Component component, @NotNull String context) {
		if (type != MessageTarget.CHAT)
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
