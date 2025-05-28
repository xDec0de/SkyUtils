package net.codersky.skyutils.crossplatform.message.tag;

import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.tag.JTag;
import net.codersky.skyutils.crossplatform.message.tag.event.CopyEventMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.event.EventMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.event.OpenFileEventMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.event.OpenUrlEventMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.event.RunCommandEventMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.event.ShowTextEventMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.event.SuggestCommandEventMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.ConsoleFilterMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.FilterMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.PlayerFilterMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.target.ActionBarTargetMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.target.TargetMessageTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class MessageTagProvider {

	private final static List<EventMessageTag> events;
	private final static List<FilterMessageTag> filters;
	private final static List<TargetMessageTag> types;

	static {
		// NOTE: This reduces list sizes by default, as in most cases no external tags will be registered.
		events = JCollections.asArrayList(
				CopyEventMessageTag.INSTANCE,
				OpenFileEventMessageTag.INSTANCE,
				OpenUrlEventMessageTag.INSTANCE,
				RunCommandEventMessageTag.INSTANCE,
				ShowTextEventMessageTag.INSTANCE,
				SuggestCommandEventMessageTag.INSTANCE
		);
		filters = JCollections.asArrayList(
				ConsoleFilterMessageTag.INSTANCE,
				PlayerFilterMessageTag.INSTANCE
		);
		types = JCollections.asArrayList(
				ActionBarTargetMessageTag.INSTANCE
		);
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
	 - Events
	 */

	public static boolean registerEvent(@NotNull final EventMessageTag event) {
		return !canRegister(event) && events.add(event);
	}

	public static int registerEvents(@NotNull final EventMessageTag... events) {
		int errors = 0;
		for (final EventMessageTag event : events)
			if (!registerEvent(event))
				errors++;
		return errors;
	}

	@Nullable
	public static EventMessageTag getEvent(@NotNull final String key) {
		return JCollections.get(events, event -> tagMatches(event, key));
	}

	@Nullable
	public static EventMessageTag getEvent(@NotNull final JTag tag) {
		return getEvent(tag.getName());
	}

	/*
	 - Filters
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
	 - Targets
	 */

	public static boolean registerTarget(@NotNull final TargetMessageTag type) {
		return !canRegister(type) && types.add(type);
	}

	public static int registerTargets(@NotNull final TargetMessageTag... types) {
		int errors = 0;
		for (final TargetMessageTag type : types)
			if (!registerTarget(type))
				errors++;
		return errors;
	}

	@Nullable
	public static TargetMessageTag getTarget(@NotNull final String key) {
		return JCollections.get(types, type -> tagMatches(type, key));
	}

	@Nullable
	public static TargetMessageTag getTarget(@NotNull final JTag tag) {
		return getTarget(tag.getName());
	}

	/*
	 - Tag information
	 */

	public static boolean isMainEventTag(@NotNull final JTag tag) {
		return tag.getName().equalsIgnoreCase("e")
				|| tag.getName().equalsIgnoreCase("event");
	}
}
