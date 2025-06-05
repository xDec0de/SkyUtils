package net.codersky.skyutils.crossplatform.message;

import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.message.event.MessageEvent;
import net.codersky.skyutils.crossplatform.message.tag.filter.ConsoleFilterMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.FilterMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.PlayerFilterMessageTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SkyMessageBuilder {

	private final List<RawMessagePart> portions = new ArrayList<>();
	private RawMessagePart portion = new RawMessagePart();

	public SkyMessageBuilder append(@NotNull final String str) {
		portion.append(str);
		return this;
	}

	/*
	 - Filter
	 */

	@NotNull
	public SkyMessageBuilder withFilter(@NotNull final String msg, @NotNull final FilterMessageTag filter) {
		portion.setCondition(filter::filter);
		if (portion.isEmpty())
			return this;
		portions.add(portion);
		portion = new RawMessagePart();
		return this;
	}

	@NotNull
	public SkyMessageBuilder consoleOnly(@NotNull final String msg) {
		return withFilter(msg, ConsoleFilterMessageTag.INSTANCE);
	}

	@NotNull
	public SkyMessageBuilder playerOnly(@NotNull final String msg) {
		return withFilter(msg, PlayerFilterMessageTag.INSTANCE);
	}

	/*
	 - Events
	 */

	@NotNull
	public SkyMessageBuilder events(@NotNull final String msg, @NotNull final MessageEvent... events) {
		append("<e:");
		for (final MessageEvent event : events)
			append("<").append(event.getName()).append(":").append(event.getContent()).append(">");
		append(msg).append(">");
		return this;
	}

	/*
	 - Build
	 */

	@NotNull
	public SkyMessage build() {
		final List<RawMessagePart> toBuild = new ArrayList<>(portions);
		toBuild.add(portion);
		return SkyMessage.of(toBuild);
	}

	/*
	 - String conversion
	 */

	@NotNull
	public String toString(@NotNull final MessageReceiver receiver) {
		final StringBuilder res = new StringBuilder();
		for (final RawMessagePart portion : portions)
			if (portion.matches(receiver))
				res.append(portion.getContent());
		return res.toString();
	}

	@NotNull
	@Override
	public String toString() {
		final StringBuilder res = new StringBuilder();
		for (final RawMessagePart portion : portions)
			res.append(portion.getContent());
		return res.append(portion.getContent()).toString();
	}
}
