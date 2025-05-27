package net.codersky.skyutils.crossplatform.message.tag;

import net.codersky.jsky.collections.JCollections;
import net.codersky.skyutils.crossplatform.message.tag.filter.ConsoleMessageFilter;
import net.codersky.skyutils.crossplatform.message.tag.filter.MessageFilter;
import net.codersky.skyutils.crossplatform.message.tag.filter.PlayerMessageFilter;
import net.codersky.skyutils.crossplatform.message.tag.type.ActionBarMessageType;
import net.codersky.skyutils.crossplatform.message.tag.type.MessageTypeTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MessageTagProvider {

	private final static List<MessageFilter> filters;
	private final static List<MessageTypeTag> types;

	static {
		// NOTE: This reduces list sizes by default, as in most cases no external tags will be registered.
		filters = JCollections.asArrayList(ConsoleMessageFilter.INSTANCE, PlayerMessageFilter.INSTANCE);
		types = JCollections.asArrayList(ActionBarMessageType.INSTANCE);
	}

	private static boolean tagMatches(@NotNull MessageTag tag, @NotNull String id) {
		if (tag.getKey().equalsIgnoreCase(id))
			return true;
		for (final String alias : tag.getAliases())
			if (alias.equalsIgnoreCase(id))
				return true;
		return false;
	}

	/*
	 - General
	 */

	private static MessageTag searchTagIn(@NotNull final List<? extends MessageTag> list, @NotNull final String key) {
		for (final MessageTag tag : list)
			if (tagMatches(tag, key))
				return tag;
		return null;
	}

	@Nullable
	public static MessageTag getTag(@NotNull final String key) {
		MessageTag tag;
		if ((tag = searchTagIn(filters, key)) != null)
			return tag;
		if ((tag = searchTagIn(types, key)) != null)
			return tag;
		return null;
	}

	@Nullable
	public static <T extends MessageTag> T getTag(@NotNull final String key, @NotNull Class<T> clazz) {
		final MessageTag tag = getTag(key);
		return clazz.isInstance(tag) ? clazz.cast(tag) : null;
	}

	public static boolean isRegistered(@NotNull final MessageTag tag) {
		if (getTag(tag.getKey()) != null)
			return true;
		for (final String alias : tag.getAliases())
			if (getTag(alias) != null)
				return true;
		return false;
	}

	/*
	 - MessageFilters
	 */

	public static boolean registerFilter(@NotNull final MessageFilter filter) {
		return !isRegistered(filter) && filters.add(filter);
	}

	public static int registerFilters(@NotNull final MessageFilter... filters) {
		int errors = 0;
		for (final MessageFilter filter : filters)
			if (!registerFilter(filter))
				errors++;
		return errors;
	}

	@Nullable
	public static MessageFilter getFilter(@NotNull String key) {
		return getTag(key, MessageFilter.class);
	}

	/*
	 - MessageTypes
	 */

	public static boolean registerType(@NotNull final MessageTypeTag type) {
		return !isRegistered(type) && types.add(type);
	}

	public static int registerTypes(@NotNull final MessageTypeTag... types) {
		int errors = 0;
		for (final MessageTypeTag type : types)
			if (!registerType(type))
				errors++;
		return errors;
	}

	@Nullable
	public static MessageTypeTag getType(@NotNull String key) {
		return getTag(key, MessageTypeTag.class);
	}
}
