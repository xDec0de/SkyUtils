package net.codersky.skyutils.crossplatform.message.tag.type;

import net.codersky.skyutils.crossplatform.message.MessageType;
import org.jetbrains.annotations.NotNull;

public class ActionBarMessageType implements MessageTypeTag {

	public static final ActionBarMessageType INSTANCE = new ActionBarMessageType();
	private final String[] aliases = {"actionbar"};

	private ActionBarMessageType() {}

	@Override
	public @NotNull MessageType getType() {
		return MessageType.ACTIONBAR;
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
