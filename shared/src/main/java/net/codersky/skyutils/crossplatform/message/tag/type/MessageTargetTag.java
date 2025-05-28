package net.codersky.skyutils.crossplatform.message.tag.type;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.codersky.skyutils.crossplatform.message.tag.MessageTag;
import org.jetbrains.annotations.NotNull;

public interface MessageTargetTag extends MessageTag {

	@NotNull
	MessageTarget getType();
}
