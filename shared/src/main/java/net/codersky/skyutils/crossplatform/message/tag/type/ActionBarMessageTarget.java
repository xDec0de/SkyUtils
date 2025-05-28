package net.codersky.skyutils.crossplatform.message.tag.type;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import org.jetbrains.annotations.NotNull;

public class ActionBarMessageTarget implements MessageTargetTag {

	public static final ActionBarMessageTarget INSTANCE = new ActionBarMessageTarget();
	private final String[] aliases = {"actionbar"};

	private ActionBarMessageTarget() {}

	@Override
	public @NotNull MessageTarget getType() {
		return MessageTarget.ACTIONBAR;
	}

	@Override
	public @NotNull String getKey() {
		return "ab";
	}

	@Override
	public @NotNull String @NotNull [] getAliases() {
		return aliases;
	}
}
