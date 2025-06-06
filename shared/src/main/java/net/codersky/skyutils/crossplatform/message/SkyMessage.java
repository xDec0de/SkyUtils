package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParseAllResult;
import net.codersky.jsky.strings.tag.JTagParser;
import net.codersky.skyutils.cmd.SkyCommandSender;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.message.tag.MessageTagProvider;
import net.codersky.skyutils.crossplatform.message.tag.event.EventMessageTag;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.codersky.skyutils.java.strings.TempSkyStrings;
import net.kyori.adventure.text.Component;
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

	@NotNull Component player;
	@NotNull Component console;

	private SkyMessage(@NotNull final Component player, @NotNull final Component console) {
		this.player = Objects.requireNonNull(player);
		this.console = Objects.requireNonNull(console);
	}

	@NotNull
	public static SkyMessage of(@NotNull final String message) {
		return of(SkyMessageBuilder.of(message));
	}

	@NotNull
	public static SkyMessage of(@NotNull final SkyMessageBuilder builder) {
		final String player = TempSkyStrings.applyColor(builder.toPlayerString());
		final String console = TempSkyStrings.applyColor(builder.toConsoleString());
		return new SkyMessage(buildComponent(player), buildComponent(console));
	}

	@NotNull
	private static Component buildComponent(@NotNull final String colored) {
		Component component = Component.empty();
		final JTagParseAllResult res = JTagParser.parseAll(colored, 0, 1);
		for (final Object obj : res) {
			if (obj instanceof final JTag tag)
				component = appendTag(component, tag);
			else if (obj instanceof final String str)
				component = component.append(Component.text(str));
		}
		return component;
	}

	private static Component appendTag(@NotNull final Component component, @NotNull final JTag tag) {
		if (MessageTagProvider.isMainEventTag(tag))
			return processEvents(component, tag);
		else
			return component.append(Component.text(tag.getRaw()));
	}

	private static Component processEvents(@NotNull final Component component, final JTag tag) {
		Component comp = Component.text(tag.getContent());
		for (JTag eTag : tag.getChildren()) {
			final EventMessageTag event = MessageTagProvider.getEventTag(eTag);
			if (event != null)
				comp = event.apply(MessageTarget.CHAT, comp, eTag.getContent());
		}
		return component.append(comp);
	}

	/*
	 - Sending
	 */

	public boolean send(@NotNull final MessageReceiver receiver) {
		MessageReceiver actualReceiver = receiver;
		if (actualReceiver instanceof final SkyCommandSender sender)
			actualReceiver = sender.asReceiver();
		if (actualReceiver instanceof final SkyPlayer p)
			p.sendJsonMessage(GsonComponentSerializer.gson().serialize(this.player));
		else
			receiver.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(this.console));
		return true;
	}
}
