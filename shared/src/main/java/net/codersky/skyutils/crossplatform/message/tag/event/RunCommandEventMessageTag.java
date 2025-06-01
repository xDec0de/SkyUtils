package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class RunCommandEventMessageTag implements EventMessageTag {

	public static final RunCommandEventMessageTag INSTANCE = new RunCommandEventMessageTag();

	private RunCommandEventMessageTag() {}

	@Override
	public @NotNull Component apply(@NotNull MessageTarget target, @NotNull Component component, @NotNull String context) {
		if (target != MessageTarget.CHAT)
			return component;
		return component.clickEvent(ClickEvent.runCommand(context));
	}

	@Override
	public boolean matches(@NotNull String name) {
		return name.equals("run") || name.equals("run_cmd") || name.equals("run_command");
	}
}
