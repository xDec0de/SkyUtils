package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.collections.JCollections;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		return of(toParts(new MessagePartMap(), null, message).values());
	}

	/*
	 - Raw message to message parts
	 */

	@NotNull
	private static MessagePartMap toParts(MessagePartMap parts, Class<? extends FilterMessageTag> type, @NotNull final String message) {
		final JTagParseAllResult result = JTagParser.parseAll(message, 0, 0);
		for (final Object obj : result) {
			if (obj instanceof final JTag tag) {
				final FilterMessageTag filter = MessageTagProvider.getFilterTag(tag);
				if (filter != null)
					parts.append(filter.getClass(), tag.getContent());
				else {
					parts.append(type, "<" + tag.getName() + ":");
					toParts(parts, type, tag.getContent());
					parts.append(type, ">");
				}
			} else if (obj instanceof final String str)
				parts.append(type, str);
		}
		return parts;
	}

	private static class MessagePartMap {

		private final RawMessagePart noFilter = new RawMessagePart();
		final Map<Class<? extends FilterMessageTag>, RawMessagePart> map = new HashMap<>();

		void append(Class<? extends FilterMessageTag> clazz, final String str) {
			if (clazz == null) {
				noFilter.append(str);
				for (RawMessagePart part : map.values())
					part.append(str);
			} else
				map.computeIfAbsent(clazz, k -> new RawMessagePart(noFilter.getContent())).append(str);
		}

		List<RawMessagePart> values() {
			final List<RawMessagePart> result = JCollections.asArrayList(noFilter);
			result.addAll(map.values());
			return result;
		}
	}

	/*
	 - Sending
	 */

	public boolean send(@NotNull final MessageReceiver receiver) {
		Component toSend = null;
		for (final SkyMessagePart part : parts)
			if (part.test(receiver))
				toSend = toSend == null ? part.getComponent() : toSend.append(part.getComponent());
		return toSend == null || sendComponent(toSend, receiver);
	}

	private boolean sendComponent(@NotNull final Component component, @NotNull final MessageReceiver receiver) {
		MessageReceiver actualReceiver = receiver;
		if (receiver instanceof SkyCommandSender sender)
			actualReceiver = sender.asReceiver();
		if (actualReceiver instanceof final SkyPlayer player)
			player.sendJsonMessage(GsonComponentSerializer.gson().serialize(component));
		else
			receiver.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(component));
		return true;
	}
}
