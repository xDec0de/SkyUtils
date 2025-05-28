package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class OpenFileMessageEvent implements EventMessageTag {

	public static final OpenFileMessageEvent INSTANCE = new OpenFileMessageEvent();
	private final String[] aliases = {"open_file"};

	private OpenFileMessageEvent() {}

	@Override
	public @NotNull Component apply(@NotNull MessageTarget type, @NotNull Component component, @NotNull String context) {
		if (type != MessageTarget.CHAT)
			return component;
		return component.clickEvent(ClickEvent.openFile(context));
	}

	@Override
	public @NotNull String getKey() {
		return "file";
	}

	@Override
	public @NotNull String @NotNull [] getAliases() {
		return aliases;
	}
}
