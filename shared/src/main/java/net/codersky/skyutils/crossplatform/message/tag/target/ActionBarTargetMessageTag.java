package net.codersky.skyutils.crossplatform.message.tag.target;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import org.jetbrains.annotations.NotNull;

public class ActionBarTargetMessageTag implements TargetMessageTag {

	public static final ActionBarTargetMessageTag INSTANCE = new ActionBarTargetMessageTag();
	private final String[] aliases = {"actionbar"};

	private ActionBarTargetMessageTag() {}

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
