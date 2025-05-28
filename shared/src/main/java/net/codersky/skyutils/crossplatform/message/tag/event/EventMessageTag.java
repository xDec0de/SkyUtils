package net.codersky.skyutils.crossplatform.message.tag.event;

import net.codersky.skyutils.crossplatform.message.MessageTarget;
import net.codersky.skyutils.crossplatform.message.tag.MessageTag;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface EventMessageTag extends MessageTag {

	/**
	 * Applies this {@link EventMessageTag} to the provided {@code component}.
	 * <p>
	 * This method is considered internal as it exposes the <b>internal</b>
	 * Adventure API ({@link Component}) used by SkyUtils.
	 *
	 * @param target The current {@link MessageTarget}. Some events may
	 * not support some message types, in which case, they can just
	 * return the provided {@code component}.
	 * @param component The {@link Component} to modify.
	 */
	@NotNull
	@ApiStatus.Internal
	Component apply(@NotNull MessageTarget target, @NotNull Component component, @NotNull String context);
}
