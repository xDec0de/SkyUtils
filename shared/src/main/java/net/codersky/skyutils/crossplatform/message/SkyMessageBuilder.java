package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParseAllResult;
import net.codersky.jsky.strings.tag.JTagParser;
import net.codersky.skyutils.cmd.SkyCommandSender;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.message.event.MessageEvent;
import net.codersky.skyutils.crossplatform.message.tag.MessageTagProvider;
import net.codersky.skyutils.crossplatform.message.tag.filter.FilterMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.PlayerFilterMessageTag;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import org.jetbrains.annotations.NotNull;

public class SkyMessageBuilder {

	private final StringBuilder player = new StringBuilder();
	private final StringBuilder console = new StringBuilder();

	/*
	 - Of
	 */

	@NotNull
	public static SkyMessageBuilder of(@NotNull final String unfiltered) {
		final SkyMessageBuilder builder = new SkyMessageBuilder();
		final JTagParseAllResult res = JTagParser.parseAll(unfiltered);
		for (final Object obj : res) {
			if (obj instanceof final JTag tag) {
				final FilterMessageTag filter = MessageTagProvider.getFilterTag(tag);
				if (filter != null) {
					if (filter.getClass() == PlayerFilterMessageTag.class)
						builder.playerOnly(tag.getContent());
					else
						builder.consoleOnly(tag.getContent());
				} else
					builder.append("<" + tag.getName() + ":").append(of(tag.getContent())).append(">");
			} else if (obj instanceof final String str)
				builder.append(str);
		}
		return builder;
	}

	/*
	 - Append
	 */

	@NotNull
	public SkyMessageBuilder append(@NotNull final String str) {
		this.player.append(str);
		return this;
	}

	@NotNull
	public SkyMessageBuilder consoleOnly(@NotNull final String msg) {
		this.console.append(msg);
		return this;
	}

	@NotNull
	public SkyMessageBuilder playerOnly(@NotNull final String msg) {
		this.player.append(msg);
		return this;
	}

	@NotNull
	public SkyMessageBuilder append(@NotNull final SkyMessageBuilder other) {
		this.player.append(other.player);
		this.console.append(other.console);
		return this;
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
		return SkyMessage.of(this);
	}

	/*
	 - String conversion
	 */

	@NotNull
	public String toPlayerString() {
		return player.toString();
	}

	@NotNull
	public String toConsoleString() {
		return console.toString();
	}

	@NotNull
	public String toString(@NotNull final MessageReceiver receiver) {
		MessageReceiver actualReceiver = receiver;
		if (actualReceiver instanceof final SkyCommandSender sender)
			actualReceiver = sender.asReceiver();
		return actualReceiver instanceof SkyPlayer ? toPlayerString() : toConsoleString();
	}

	@NotNull
	@Override
	public String toString() {
		return "SkyMessageBuilder{player=" + player + ", console=" + console + "}";
	}
}
