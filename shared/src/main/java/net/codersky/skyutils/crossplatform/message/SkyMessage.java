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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SkyMessage {

	private final List<SkyMessagePart> messageParts;

	private SkyMessage(@NotNull List<SkyMessagePart> parts) {
		this.messageParts = Objects.requireNonNull(parts);
	}

	/*
	 - Building
	 */

	@NotNull
	public static SkyMessage of(@NotNull String message) {
		final List<SkyMessagePart> parts = new ArrayList<>();
		final JTagParseAllResult result = JTagParser.parseAll(message);
		for (final Object obj : result.getTags()) {
			if (obj instanceof final JTag tag)
				parts.add(new SkyMessagePart(tag));
			else if (obj instanceof final String str)
				parts.add(new SkyMessagePart(str));
		}
		return new SkyMessage(parts);
	}

	/*
	 - Sending
	 */

	public boolean send(@NotNull final MessageReceiver receiver) {
		Component toSend = null;
		for (final SkyMessagePart part : messageParts)
			if (part.matches(receiver))
				toSend = toSend == null ? part.getComponent() : toSend.append(part.getComponent());
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
