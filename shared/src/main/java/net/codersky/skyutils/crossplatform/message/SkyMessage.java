package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParseAllResult;
import net.codersky.jsky.strings.tag.JTagParser;
import net.codersky.skyutils.SkyStrings;
import net.codersky.skyutils.cmd.SkyCommandSender;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.message.tag.MessageTagProvider;
import net.codersky.skyutils.crossplatform.message.tag.event.EventMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.FilterMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.filter.PlayerFilterMessageTag;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Class used to represent a compiled message ready to be
 * {@link #send(MessageReceiver) sent} to any {@link MessageReceiver}.
 *
 * @since SkyUtils 1.0.0
 *
 * @see SkyMessageBuilder
 * @see SkyReplacer
 *
 * @author xDec0de_
 */
public class SkyMessage {

	@NotNull final String player;
	@NotNull final String console;

	SkyMessage(@NotNull String player, @NotNull String console) {
		this.player = Objects.requireNonNull(player);
		this.console = Objects.requireNonNull(console);
	}

	// TODO: Custom filter support. This filter code is temporary.

	@NotNull
	public static SkyMessage of(@NotNull String raw) {
		final JTagParseAllResult res = JTagParser.parseAll(raw);
		final StringBuilder player = new StringBuilder();
		final StringBuilder console = new StringBuilder();
		for (final Object obj : res) {
			if (obj instanceof final JTag tag) {
				final FilterMessageTag filter = MessageTagProvider.getFilterTag(tag);
				if (filter != null) {
					if (filter.getClass() == PlayerFilterMessageTag.class)
						player.append(tag.getContent());
					else
						console.append(tag.getContent());
				} else {
					player.append(tag.getRaw());
					console.append(tag.getRaw());
				}
			} else if (obj instanceof final String str) {
				player.append(str);
				console.append(str);
			}
		}
		return new SkyMessage(player.toString(), console.toString());
	}

	/*
	 - Sending
	 */

	public boolean send(@NotNull final MessageReceiver receiver) {
		final Component player = MiniMessage.miniMessage().deserialize(this.player);
		final Component console = MiniMessage.miniMessage().deserialize(this.console);
		MessageReceiver actualReceiver = receiver;
		if (actualReceiver instanceof final SkyCommandSender sender)
			actualReceiver = sender.asReceiver();
		if (actualReceiver instanceof final SkyPlayer p)
			p.sendJsonMessage(GsonComponentSerializer.gson().serialize(player));
		else
			receiver.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(console));
		return true;
	}
}
