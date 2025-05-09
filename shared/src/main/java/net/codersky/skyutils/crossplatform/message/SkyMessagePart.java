package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.strings.tag.JTag;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.SkyConsole;
import net.codersky.skyutils.crossplatform.message.tag.filter.MessageFilter;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.codersky.skyutils.java.strings.SkyStrings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SkyMessagePart {

	private final Component component;
	private final Predicate<MessageReceiver> filter;

	SkyMessagePart(@NotNull String basic) {
		this.component = Component.text(SkyStrings.applyColor(basic));
		this.filter = r -> true;
	}

	SkyMessagePart(@NotNull JTag tag) {
		this.component = Component.text(tag.getContent());
		this.filter = getFilter(tag);
	}

	private Predicate<MessageReceiver> getFilter(@NotNull JTag tag) {
		final MessageFilter msgFilter = SkyMessage.getFilter(tag.getName());
		return msgFilter == null ? r -> true : msgFilter::filter;
	}

	/*
	 - Sending
	 */

	void send(@NotNull MessageReceiver receiver) {
		if (!filter.test(receiver))
			return;
		if (receiver instanceof final SkyPlayer player)
			player.sendJsonMessage(GsonComponentSerializer.gson().serialize(component));
		else
			receiver.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(component));
	}
}
