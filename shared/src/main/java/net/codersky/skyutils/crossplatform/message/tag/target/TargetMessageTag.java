package net.codersky.skyutils.crossplatform.message.tag.target;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.codersky.skyutils.crossplatform.message.tag.MessageTag;
import org.jetbrains.annotations.NotNull;

public interface TargetMessageTag extends MessageTag {

	@NotNull
	MessageTarget getType();
}
