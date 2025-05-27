package net.codersky.skyutils.crossplatform.message.tag.type;

import net.codersky.skyutils.crossplatform.message.MessageType;
import net.codersky.skyutils.crossplatform.message.tag.MessageTag;
import org.jetbrains.annotations.NotNull;

public interface MessageTypeTag extends MessageTag {

	@NotNull
	MessageType getType();
}
