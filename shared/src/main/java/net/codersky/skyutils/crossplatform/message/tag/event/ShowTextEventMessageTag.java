package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

public class ShowTextEventMessageTag implements EventMessageTag {

	public static final ShowTextEventMessageTag INSTANCE = new ShowTextEventMessageTag();

	private ShowTextEventMessageTag() {}

	@Override
	public @NotNull Component apply(@NotNull MessageTarget target, @NotNull Component component, @NotNull String context) {
		if (target != MessageTarget.CHAT)
			return component;
		return component.hoverEvent(HoverEvent.showText(Component.text(context)));
	}

	@Override
	public boolean matches(@NotNull String name) {
		return name.equals("txt") || name.equals("text") || name.equals("show") || name.equals("show_text");
	}
}
