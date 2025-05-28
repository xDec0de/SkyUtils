package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class RunCommandEventMessageTag implements EventMessageTag {

	public static final RunCommandEventMessageTag INSTANCE = new RunCommandEventMessageTag();
	private final String[] aliases = {"run_cmd", "run_command"};

	private RunCommandEventMessageTag() {}

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
