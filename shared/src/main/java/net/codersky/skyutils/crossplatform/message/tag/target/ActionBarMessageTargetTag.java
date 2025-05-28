package net.codersky.skyutils.crossplatform.message.tag.target;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import org.jetbrains.annotations.NotNull;

public class ActionBarMessageTargetTag implements MessageTargetTag {

	public static final ActionBarMessageTargetTag INSTANCE = new ActionBarMessageTargetTag();
	private final String[] aliases = {"actionbar"};

	private ActionBarMessageTargetTag() {}

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
