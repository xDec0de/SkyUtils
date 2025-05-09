package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParser;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.message.tag.MessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.ConsoleMessageFilter;
import net.codersky.skyutils.crossplatform.message.tag.filter.MessageFilter;
import net.codersky.skyutils.crossplatform.message.tag.filter.PlayerMessageFilter;
import net.codersky.skyutils.java.strings.SkyStrings;
import net.kyori.adventure.text.Component;
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

	@NotNull
	public static SkyMessage of(@NotNull String message) {
		final StringBuilder excess = new StringBuilder();
		final JTag[] tags = JTagParser.parse(message, 0, excess);
		if (tags.length == 0)
			return new SkyMessage(List.of(new SkyMessagePart(message)));
		final String excessStr = excess.toString();
		final List<SkyMessagePart> parts = new ArrayList<>();
		for (final JTag tag : tags)
			parts.add(new SkyMessagePart(tag));
		if (!excessStr.isBlank())
			parts.add(new SkyMessagePart(excessStr));
		return new SkyMessage(parts);
	}

	public boolean send(@NotNull MessageReceiver receiver) {
		for (final SkyMessagePart part : messageParts)
			part.send(receiver);
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
