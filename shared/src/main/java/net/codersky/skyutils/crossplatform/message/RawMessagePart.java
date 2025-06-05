package net.codersky.skyutils.crossplatform.message;

import net.codersky.skyutils.crossplatform.MessageReceiver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

class RawMessagePart {

	private StringBuilder content;
	private Predicate<MessageReceiver> condition = null;

	RawMessagePart(@NotNull final StringBuilder content) {
		this.content = new StringBuilder(content);
	}

	RawMessagePart() {
		this.content = new StringBuilder();
	}

	/*
	 - Condition related
	 */

	@NotNull
	Predicate<MessageReceiver> getCondition() {
		return condition;
	}

	void setCondition(@Nullable final Predicate<MessageReceiver> condition) {
		this.condition = condition;
	}

	boolean matches(@NotNull final MessageReceiver receiver) {
		return condition == null || condition.test(receiver);
	}

	/*
	 - Content related
	 */

	boolean isEmpty() {
		return content.isEmpty();
	}

	void append(@NotNull final String str) {
		content.append(str);
	}

	@NotNull
	StringBuilder getContent() {
		return content;
	}
}
