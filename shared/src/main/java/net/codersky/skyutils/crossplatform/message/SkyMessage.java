package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParseAllResult;
import net.codersky.jsky.strings.tag.JTagParser;
import net.codersky.skyutils.cmd.SkyCommandSender;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.message.tag.MessageTagProvider;
import net.codersky.skyutils.crossplatform.message.tag.filter.FilterMessageTag;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.codersky.skyutils.java.strings.TempSkyStrings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SkyMessage {

	private final List<SkyMessagePart> parts;

	private SkyMessage(@NotNull final List<SkyMessagePart> parts) {
		this.parts = parts;
	}

	static SkyMessage of(@NotNull final List<RawMessagePart> rawParts) {
		final List<SkyMessagePart> skyParts = new ArrayList<>(rawParts.size());
		for (final RawMessagePart rawPart : rawParts) {
			SkyMessagePart skyPart = new SkyMessagePart(rawPart.getCondition());
			final String colored = TempSkyStrings.applyColor(rawPart.getContent().toString());
			final JTagParseAllResult res = JTagParser.parseAll(colored, 0, 1);
			for (final Object obj : res) {
				if (obj instanceof final JTag tag)
					skyPart.appendTag(tag);
				else if (obj instanceof final String str)
					skyPart.append(str);
			}
			skyParts.add(skyPart);
		}
		return new SkyMessage(skyParts);
	}

	@NotNull
	public static SkyMessage of(@NotNull final String message) {
		return of(toParts(new LinkedList<>(), null, message));
	}

	/*
	 - Raw message to message parts
	 */

	@NotNull
	private static List<RawMessagePart> toParts(LinkedList<RawMessagePart> parts, RawMessagePart part, @NotNull final String message) {
		final JTagParseAllResult result = JTagParser.parseAll(message, 0, 0);
		RawMessagePart currentPart = part == null ? new RawMessagePart() : part;
		boolean subTagFound = false;
		for (final Object obj : result) {
			if (obj instanceof final JTag tag) {
				final FilterMessageTag filter = MessageTagProvider.getFilterTag(tag);
				if (filter != null) {
					if (!currentPart.isEmpty())
						parts.add(currentPart);
					parts.add(new RawMessagePart().append(tag.getContent()).setCondition(filter::filter));
					currentPart = new RawMessagePart();
				} else {
					subTagFound = true;
					currentPart.append("<").append(tag.getName()).append(":");
					currentPart = toParts(parts, currentPart, tag.getContent()).getLast();
					currentPart.append(">");
				}
			} else if (obj instanceof final String str)
				currentPart.append(str);
		}
		if (!subTagFound && !currentPart.isEmpty())
			parts.add(currentPart);
		return parts;
	}

	/*
	 - Sending
	 */

	public boolean send(@NotNull final MessageReceiver receiver) {
		Component toSend = null;
		MessageReceiver actualReceiver = receiver;
		if (actualReceiver instanceof final SkyCommandSender sender)
			actualReceiver = sender.asReceiver();
		for (final SkyMessagePart part : parts)
			if (part.test(actualReceiver))
				toSend = toSend == null ? part.getComponent() : toSend.append(part.getComponent());
		return toSend == null || sendComponent(toSend, actualReceiver);
	}

	private boolean sendComponent(@NotNull final Component component, @NotNull final MessageReceiver receiver) {
		if (receiver instanceof final SkyPlayer player)
			player.sendJsonMessage(GsonComponentSerializer.gson().serialize(component));
		else
			receiver.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(component));
		return true;
	}
}
