package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParseAllResult;
import net.codersky.jsky.strings.tag.JTagParser;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class SkyMessage {

	private final List<SkyMessagePortion> portions;

	SkyMessage(@NotNull List<SkyMessagePortion> portions) {
		this.portions = Objects.requireNonNull(portions);
	}

	/*
	 - Building
	 */

	@NotNull
	public static SkyMessage of(String message) {
		final SkyMessageBuilder builder = new SkyMessageBuilder();
		final JTagParseAllResult res = JTagParser.parseAll(message, 0, 1);
		for (final Object obj : res) {
			if (obj instanceof final JTag tag)
				builder.appendTag(tag);
			else if (obj instanceof final String str)
				builder.append(str);
			builder.newPortion(MessageClear.ALL);
		}
		return builder.build();
	}

	/*
	 - Sending
	 */

	public boolean send(@NotNull final MessageReceiver receiver) {
		Component toSend = null;
		for (final SkyMessagePortion portion : portions)
			if (portion.getFilter().test(receiver))
				toSend = toSend == null ? portion.getComponent() : toSend.append(portion.getComponent());
		return toSend == null || sendComponent(toSend, receiver);
	}

	private boolean sendComponent(@NotNull final Component component, @NotNull final MessageReceiver receiver) {
		if (receiver instanceof final SkyPlayer player)
			player.sendJsonMessage(GsonComponentSerializer.gson().serialize(component));
		else
			receiver.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(component));
		return true;
	}
}
