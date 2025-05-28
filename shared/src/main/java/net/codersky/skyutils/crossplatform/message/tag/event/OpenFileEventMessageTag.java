package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class OpenFileEventMessageTag implements EventMessageTag {

	public static final OpenFileEventMessageTag INSTANCE = new OpenFileEventMessageTag();
	private final String[] aliases = {"open_file"};

	private OpenFileEventMessageTag() {}

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
