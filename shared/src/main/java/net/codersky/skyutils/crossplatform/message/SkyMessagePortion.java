package net.codersky.skyutils.crossplatform.message;

import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.kyori.adventure.text.Component;

import java.util.Objects;
import java.util.function.Predicate;

class SkyMessagePortion {

	private final MessageTarget type;
	private final Component component;
	private final Predicate<MessageReceiver> filter;

	SkyMessagePortion(final MessageTarget type, final Component component, final Predicate<MessageReceiver> filter) {
		this.type = Objects.requireNonNull(type);
		this.component = Objects.requireNonNull(component);
		this.filter = Objects.requireNonNull(filter);
	}

	MessageTarget getType() {
		return type;
	}

	Component getComponent() {
		return component;
	}

	Predicate<MessageReceiver> getFilter() {
		return filter;
	}
}
