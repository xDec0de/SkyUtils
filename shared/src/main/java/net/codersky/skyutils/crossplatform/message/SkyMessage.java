package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParseAllResult;
import net.codersky.jsky.strings.tag.JTagParser;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.message.tag.MessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.ConsoleMessageFilter;
import net.codersky.skyutils.crossplatform.message.tag.filter.MessageFilter;
import net.codersky.skyutils.crossplatform.message.tag.filter.PlayerMessageFilter;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SkyMessage {

	private final static List<MessageFilter> filters;

	private final List<SkyMessagePart> messageParts;

	static {
		filters = JCollections.asArrayList(new ConsoleMessageFilter(), new PlayerMessageFilter());
	}

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

	/*
	 - Tags
	 */

	private static boolean tagMatches(@NotNull MessageTag tag, @NotNull String id) {
		if (tag.getKey().equalsIgnoreCase(id))
			return true;
		for (final String alias : tag.getAliases())
			if (alias.equalsIgnoreCase(id))
				return true;
		return false;
	}

	@Nullable
	public static MessageFilter getFilter(@NotNull String key) {
		return JCollections.get(filters, filter -> tagMatches(filter, key));
	}
}
