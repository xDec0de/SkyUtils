package net.codersky.skyutils.crossplatform.message.tag.filter;

import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.message.tag.MessageTag;
import org.jetbrains.annotations.NotNull;

public interface MessageFilter extends MessageTag {

	String[] NO_ARGS = new String[0];

	boolean filter(@NotNull MessageReceiver receiver);
}
