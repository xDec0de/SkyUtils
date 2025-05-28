package net.codersky.skyutils.crossplatform.message.tag;

import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.tag.JTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.ConsoleFilterMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.FilterMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.PlayerFilterMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.target.ActionBarMessageTargetTag;
import net.codersky.skyutils.crossplatform.message.tag.target.MessageTargetTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class MessageTagProvider {

	private final static List<FilterMessageTag> filters;
	private final static List<MessageTargetTag> types;

	static {
		// NOTE: This reduces list sizes by default, as in most cases no external tags will be registered.
		filters = JCollections.asArrayList(ConsoleFilterMessageTag.INSTANCE, PlayerFilterMessageTag.INSTANCE);
		types = JCollections.asArrayList(ActionBarMessageTargetTag.INSTANCE);
	}

	private static boolean tagMatches(@NotNull final MessageTag tag, @NotNull final String id) {
		if (tag.getKey().equalsIgnoreCase(id))
			return true;
		for (final String alias : tag.getAliases())
			if (alias.equalsIgnoreCase(id))
				return true;
		return false;
	}

	/*
	 - Tag getters
	 */

	@Nullable
	public static MessageTag getTag(@NotNull final Predicate<MessageTag> condition) {
		MessageTag tag;
		if ((tag = JCollections.get(filters, condition::test)) != null)
			return tag;
		if ((tag = JCollections.get(types, condition::test)) != null)
			return tag;
		return null;
	}

	@Nullable
	public static MessageTag getTag(@NotNull final String key) {
		return getTag(tag -> tagMatches(tag, key));
	}

	@Nullable
	public static <T extends MessageTag> T getTag(@NotNull final String key, @NotNull final Class<T> clazz) {
		final MessageTag tag = getTag(key);
		return clazz.isInstance(tag) ? clazz.cast(tag) : null;
	}

	@Nullable
	public static <T extends MessageTag> T getTag(@NotNull final Class<T> clazz) {
		final MessageTag tag = getTag(t -> t.getClass().equals(clazz));
		return tag == null ? null : clazz.cast(tag);
	}

	@Nullable
	public static MessageTag getTag(@NotNull final JTag tag) {
		return getTag(tag.getName());
	}

	/*
	 - Registration check
	 */

	public static boolean canRegister(@NotNull final MessageTag tag) {
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

	public static boolean registerFilter(@NotNull final FilterMessageTag filter) {
		return !canRegister(filter) && filters.add(filter);
	}

	public static int registerFilters(@NotNull final FilterMessageTag... filters) {
		int errors = 0;
		for (final FilterMessageTag filter : filters)
			if (!registerFilter(filter))
				errors++;
		return errors;
	}

	@Nullable
	public static FilterMessageTag getFilter(@NotNull final String key) {
		return JCollections.get(filters, filter -> tagMatches(filter, key));
	}

	@Nullable
	public static FilterMessageTag getFilter(@NotNull final JTag tag) {
		return getFilter(tag.getName());
	}

	/*
	 - MessageTypes
	 */

	public static boolean registerTarget(@NotNull final MessageTargetTag type) {
		return !canRegister(type) && types.add(type);
	}

	public static int registerTargets(@NotNull final MessageTargetTag... types) {
		int errors = 0;
		for (final MessageTargetTag type : types)
			if (!registerTarget(type))
				errors++;
		return errors;
	}

	@Nullable
	public static MessageTargetTag getTarget(@NotNull final String key) {
		return JCollections.get(types, type -> tagMatches(type, key));
	}

	@Nullable
	public static MessageTargetTag getTarget(@NotNull final JTag tag) {
		return getTarget(tag.getName());
	}

	/*
	 - Tag information
	 */

	public static boolean isEventTag(@NotNull final JTag tag) {
		return tag.getName().equalsIgnoreCase("e")
				|| tag.getName().equalsIgnoreCase("event");
	}
}
