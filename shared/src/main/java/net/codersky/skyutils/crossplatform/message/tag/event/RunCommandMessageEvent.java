package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class RunCommandMessageEvent implements EventMessageTag {

	public static final RunCommandMessageEvent INSTANCE = new RunCommandMessageEvent();
	private final String[] aliases = {"run_cmd", "run_command"};

	private RunCommandMessageEvent() {}

	@Override
	public @NotNull Component apply(@NotNull MessageTarget type, @NotNull Component component, @NotNull String context) {
		if (type != MessageTarget.CHAT)
			return component;
		return component.clickEvent(ClickEvent.runCommand(context));
	}

	@Override
	public @NotNull String getKey() {
		return "run";
	}

	@Override
	public @NotNull String @NotNull [] getAliases() {
		return aliases;
	}
}
