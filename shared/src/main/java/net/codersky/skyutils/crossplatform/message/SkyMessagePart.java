package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.strings.tag.JTag;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.message.tag.MessageTagProvider;
import net.codersky.skyutils.crossplatform.message.tag.filter.MessageFilter;
import net.codersky.skyutils.java.strings.SkyStrings;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SkyMessagePart {

	private MessageType type = MessageType.CHAT;
	private final Component component;
	private final Predicate<MessageReceiver> filter;

	SkyMessagePart(@NotNull String basic) {
		this.component = Component.text(SkyStrings.applyColor(basic));
		this.filter = r -> true;
	}

	SkyMessagePart(@NotNull JTag tag) {
		this.component = Component.text(tag.getContent());
		this.filter = getFilter(tag);
	}

	private Predicate<MessageReceiver> getFilter(@NotNull JTag tag) {
		final MessageFilter msgFilter = MessageTagProvider.getFilter(tag.getName());
		return msgFilter == null ? r -> true : msgFilter::filter;
	}

	/*
	 - Sending utility
	 */

	@NotNull
	MessageType getType() {
		return type;
	}

	boolean matches(@NotNull final MessageReceiver receiver) {
		return type.isSupportedBy(receiver) && filter.test(receiver);
	}

	Component getComponent() {
		return component;
	}
}
