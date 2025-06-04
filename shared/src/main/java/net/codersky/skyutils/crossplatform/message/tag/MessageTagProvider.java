package net.codersky.skyutils.crossplatform.message.tag;

import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.tag.JTag;
import net.codersky.skyutils.crossplatform.message.tag.color.ColorMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.color.HexGradientMessageTag;
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

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class MessageTagProvider {

	private final static List<ColorMessageTag> colorTags;
	private final static List<EventMessageTag> eventTags;
	private final static List<FilterMessageTag> filterTags;
	private final static List<TargetMessageTag> targetTags;

	static {
		// NOTE: This reduces list sizes by default, as in most cases no external tags will be registered.
		colorTags = JCollections.asArrayList(
				HexGradientMessageTag.INSTANCE
		);
		eventTags = JCollections.asArrayList(
				CopyEventMessageTag.INSTANCE,
				OpenFileEventMessageTag.INSTANCE,
				OpenUrlEventMessageTag.INSTANCE,
				RunCommandEventMessageTag.INSTANCE,
				ShowTextEventMessageTag.INSTANCE,
				SuggestCommandEventMessageTag.INSTANCE
		);
		filterTags = JCollections.asArrayList(
				ConsoleFilterMessageTag.INSTANCE,
				PlayerFilterMessageTag.INSTANCE
		);
		targetTags = JCollections.asArrayList(
				ActionBarTargetMessageTag.INSTANCE
		);
	}

	/*
	 - General
	 */

	@Nullable
	public static MessageTag getTag(@NotNull final Predicate<MessageTag> condition) {
		MessageTag tag;
		if ((tag = JCollections.get(colorTags, condition::test)) != null)
			return tag;
		if ((tag = JCollections.get(targetTags, condition::test)) != null)
			return tag;
		if ((tag = JCollections.get(filterTags, condition::test)) != null)
			return tag;
		if ((tag = JCollections.get(eventTags, condition::test)) != null)
			return tag;
		return null;
	}

	@Nullable
	public static MessageTag getTag(@NotNull final String name) {
		return getTag(t -> t.matches(name));
	}

	@Nullable
	public static MessageTag getTag(@NotNull final JTag tag) {
		return getTag(tag.getName());
	}

	/*
	 - Colors
	 */

	public static void registerColorTags(@NotNull final ColorMessageTag... colors) {
		colorTags.addAll(Arrays.asList(colors));
	}

	@Nullable
	public static ColorMessageTag getColorTag(@NotNull final String name) {
		return JCollections.get(colorTags, event -> event.matches(name));
	}

	@Nullable
	public static ColorMessageTag getColorTag(@NotNull final JTag tag) {
		return getColorTag(tag.getName());
	}

	/*
	 - Events
	 */

	public static boolean isMainEventTag(@NotNull final JTag tag) {
		return tag.getName().equalsIgnoreCase("e")
				|| tag.getName().equalsIgnoreCase("event");
	}

	public static void registerEventTags(@NotNull final EventMessageTag... events) {
		eventTags.addAll(Arrays.asList(events));
	}

	@Nullable
	public static EventMessageTag getEventTag(@NotNull final String name) {
		return JCollections.get(eventTags, event -> event.matches(name));
	}

	@Nullable
	public static EventMessageTag getEventTag(@NotNull final JTag tag) {
		return getEventTag(tag.getName());
	}

	/*
	 - Filters
	 */

	public static void registerFilterTags(@NotNull final FilterMessageTag... filters) {
		filterTags.addAll(Arrays.asList(filters));
	}

	@Nullable
	public static FilterMessageTag getFilterTag(@NotNull final String name) {
		return JCollections.get(filterTags, filter -> filter.matches(name));
	}

	@Nullable
	public static FilterMessageTag getFilterTag(@NotNull final JTag tag) {
		return getFilterTag(tag.getName());
	}

	/*
	 - Targets
	 */

	public static void registerTargetTags(@NotNull final TargetMessageTag... targets) {
		targetTags.addAll(Arrays.asList(targets));
	}

	@Nullable
	public static TargetMessageTag getTargetTag(@NotNull final String name) {
		return JCollections.get(targetTags, target -> target.matches(name));
	}

	@Nullable
	public static TargetMessageTag getTargetTag(@NotNull final JTag tag) {
		return getTargetTag(tag.getName());
	}
}
