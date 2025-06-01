package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParseAllResult;
import net.codersky.jsky.strings.tag.JTagParser;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.message.tag.MessageTag;
import net.codersky.skyutils.crossplatform.message.tag.MessageTagProvider;
import net.codersky.skyutils.crossplatform.message.tag.event.EventMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.ConsoleFilterMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.FilterMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.PlayerFilterMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.target.TargetMessageTag;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class SkyMessageBuilder {

	private final List<SkyMessagePortion> portions = new ArrayList<>();

	private MessageTarget msgTarget = MessageTarget.CHAT;
	private final List<FilterMessageTag> filters = new ArrayList<>();
	private final List<EventMessageTag> modifiers = new ArrayList<>();
	private Component component = Component.empty();

	public SkyMessageBuilder append(@NotNull final String str) {
		this.component = component.append(Component.text(str));
		return this;
	}

	/*
	 - Build
	 */

	@NotNull
	public SkyMessage build() {
		return new SkyMessage(portions);
	}

	/*
	 - Filters
	 */

	@Nullable
	public FilterMessageTag getFilter(@NotNull final String key) {
		return JCollections.get(filters, f -> tagMatches(f, key));
	}

	@Nullable
	public FilterMessageTag getFilter(@NotNull final Class<FilterMessageTag> filterClass) {
		return JCollections.get(filters, f -> filterClass.isAssignableFrom(f.getClass()));
	}

	public boolean hasFilter(@NotNull final String key) {
		return getFilter(key) != null;
	}

	public boolean hasFilter(@NotNull final Class<FilterMessageTag> filterClass) {
		return getFilter(filterClass) != null;
	}

	public boolean hasFilter(@NotNull final FilterMessageTag filter) {
		return JCollections.get(filters, f -> tagMatches(f, filter)) != null;
	}

	@NotNull
	public SkyMessageBuilder addFilter(@NotNull final FilterMessageTag filter) {
		if (!hasFilter(filter))
			filters.add(Objects.requireNonNull(filter));
		return this;
	}

	@NotNull
	public SkyMessageBuilder removeFilter(@NotNull final FilterMessageTag filter) {
		filters.remove(filter);
		return this;
	}

	@NotNull
	public SkyMessageBuilder clearFilters() {
		filters.clear();
		return this;
	}

	@NotNull
	public Predicate<MessageReceiver> getPredicate() {
		Predicate<MessageReceiver> predicate = null;
		for (final FilterMessageTag filter : filters)
			predicate = predicate == null ? filter::filter : predicate.and(filter::filter);
		return predicate == null ? r -> true : predicate;
	}

	/*
	 - Player filter
	 */

	@NotNull
	public SkyMessageBuilder playerOnly(boolean keepModifiers) {
		if (!keepModifiers)
			clearModifiers();
		removeFilter(ConsoleFilterMessageTag.INSTANCE);
		return addFilter(PlayerFilterMessageTag.INSTANCE);
	}

	@NotNull
	public SkyMessageBuilder playerOnly() {
		return playerOnly(true);
	}

	/*
	 - Console filter
	 */

	@NotNull
	public SkyMessageBuilder consoleOnly(boolean keepModifiers) {
		if (!keepModifiers)
			clearModifiers();
		removeFilter(PlayerFilterMessageTag.INSTANCE);
		return addFilter(ConsoleFilterMessageTag.INSTANCE);
	}

	@NotNull
	public SkyMessageBuilder consoleOnly() {
		return consoleOnly(true);
	}

	/*
	 - Modifiers
	 */

	@NotNull
	public SkyMessageBuilder clearModifiers() {
		modifiers.clear();
		return this;
	}

	/*
	 - Targets
	 */

	@NotNull
	public MessageTarget getTarget() {
		return msgTarget;
	}

	@NotNull
	public SkyMessageBuilder setTarget(@NotNull final MessageTarget type) {
		this.msgTarget = Objects.requireNonNull(type);
		return this;
	}

	/*
	 - Specific targets
	 */

	@NotNull
	public SkyMessageBuilder actionBar() {
		return setTarget(MessageTarget.ACTIONBAR);
	}

	@NotNull
	public SkyMessageBuilder chat() {
		return setTarget(MessageTarget.CHAT);
	}

	/*
	 - Portions
	 */

	public SkyMessageBuilder newPortion(@NotNull final MessageClear clear) {
		if (this.component == Component.empty())
			return clear.clearOn(this);
		final SkyMessagePortion portion = new SkyMessagePortion(msgTarget, component, getPredicate());
		portions.add(portion);
		this.component = Component.empty();
		return clear.clearOn(this);
	}

	public SkyMessageBuilder newPortion() {
		return newPortion(MessageClear.ALL);
	}

	/*
	 - Tags (Internal for SkyMessage#of)
	 */

	@NotNull
	SkyMessageBuilder appendTag(@NotNull final JTag tag) {
		final MessageTag msgTag = MessageTagProvider.getTag(tag);
		return switch (msgTag) {
			case null -> MessageTagProvider.isMainEventTag(tag) ? processEvents(tag) : this;
			case final FilterMessageTag filter -> processFilter(filter, tag.getContent());
			case TargetMessageTag target -> setTarget(target.getType()).append(tag.getContent());
			default -> this;
		};
	}

	private SkyMessageBuilder processFilter(final FilterMessageTag filter, final String content) {
		addFilter(filter);
		final JTagParseAllResult res = JTagParser.parseAll(content, 0, 1);
		for (final Object obj : res) {
			if (obj instanceof final JTag tag)
				appendTag(tag);
			else if (obj instanceof final String str)
				append(str);
		}
		return this;
	}

	private SkyMessageBuilder processEvents(final JTag tag) {
		final JTagParseAllResult res = JTagParser.parseAll(tag.getContent(), 0, 1);
		Component comp = Component.empty();
		final StringBuilder builder = new StringBuilder();
		for (final Object obj : res) {
			if (obj instanceof final JTag eTag) {
				final EventMessageTag event = MessageTagProvider.getEvent(eTag);
				if (event != null)
					comp = event.apply(getTarget(), comp, eTag.getContent());
			} else if (obj instanceof final String str)
				builder.append(str);
		}
		comp = Component.text(builder.toString());
		this.component = this.component.append(comp);
		return this;
	}

	/*
	 - Utility
	 */

	private boolean tagMatches(@NotNull MessageTag tag, @NotNull MessageTag other) {
		if (tagMatches(tag, other.getKey()))
			return true;
		for (final String alias : other.getAliases())
			if (tagMatches(tag, alias))
				return true;
		return false;
	}

	private boolean tagMatches(@NotNull MessageTag tag, @NotNull String id) {
		if (tag.getKey().equalsIgnoreCase(id))
			return true;
		for (final String alias : tag.getAliases())
			if (alias.equalsIgnoreCase(id))
				return true;
		return false;
	}
}
