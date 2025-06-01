package net.codersky.skyutils.crossplatform.message.tag.target;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import org.jetbrains.annotations.NotNull;

public class ActionBarTargetMessageTag implements TargetMessageTag {

	public static final ActionBarTargetMessageTag INSTANCE = new ActionBarTargetMessageTag();

	private ActionBarTargetMessageTag() {}

	@Override
	public @NotNull MessageTarget getType() {
		return MessageTarget.ACTIONBAR;
	}

	@Override
	public boolean matches(@NotNull String name) {
		return name.equals("ab") || name.equals("actionbar");
	}
}
