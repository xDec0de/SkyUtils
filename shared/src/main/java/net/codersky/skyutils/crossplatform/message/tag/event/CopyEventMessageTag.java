package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class CopyEventMessageTag implements EventMessageTag {

	public static final CopyEventMessageTag INSTANCE = new CopyEventMessageTag();

	private CopyEventMessageTag() {}

	@Override
	public @NotNull Component apply(@NotNull MessageTarget target, @NotNull Component component, @NotNull String context) {
		if (target != MessageTarget.CHAT)
			return component;
		return component.clickEvent(ClickEvent.copyToClipboard(context));
	}

	@Override
	public boolean matches(@NotNull String name) {
		return name.equals("copy") || name.equals("copy_to_clipboard");
	}
}
