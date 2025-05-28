package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageType;
import net.codersky.skyutils.crossplatform.message.tag.MessageTag;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface MessageEvent extends MessageTag {

	/**
	 * Applies this {@link MessageEvent} to the provided {@code component}.
	 * <p>
	 * This method is considered internal as it exposes the <b>internal</b>
	 * Adventure API ({@link Component}) used by SkyUtils.
	 *
	 * @param type The current {@link MessageType}. Some events may
	 * not support some message types, in which case, they can just
	 * return the provided {@code component}.
	 * @param component The {@link Component} to modify.
	 */
	@NotNull
	@ApiStatus.Internal
	Component apply(@NotNull MessageType type, @NotNull Component component, @NotNull String context);
}
