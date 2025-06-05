package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.strings.tag.JTag;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.message.tag.MessageTagProvider;
import net.codersky.skyutils.crossplatform.message.tag.event.EventMessageTag;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SkyMessagePart {

	private final MessageTarget target = MessageTarget.CHAT;
	private final Predicate<MessageReceiver> condition;
	private Component component = Component.empty();

	SkyMessagePart(final Predicate<MessageReceiver> condition) {
		this.condition = condition;
	}

	SkyMessagePart append(@NotNull final String str) {
		this.component = component.append(Component.text(str));
		return this;
	}

	void appendTag(@NotNull final JTag tag) {
		if (MessageTagProvider.isMainEventTag(tag))
			processEvents(tag);
		else
			append("<").append(tag.getName()).append(":").append(tag.getContent()).append(">");
	}

	private void processEvents(final JTag tag) {
		Component comp = Component.text(tag.getContent());
		for (JTag eTag : tag.getChildren()) {
			final EventMessageTag event = MessageTagProvider.getEventTag(eTag);
			if (event != null)
				comp = event.apply(target, comp, eTag.getContent());
		}
		this.component = this.component.append(comp);
	}

	boolean test(@NotNull final MessageReceiver receiver) {
		return condition == null || condition.test(receiver);
	}

	Component getComponent() {
		return component;
	}
}
