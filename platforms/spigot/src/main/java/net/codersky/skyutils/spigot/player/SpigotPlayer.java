package net.codersky.skyutils.spigot.player;

import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface SpigotPlayer extends OfflineSpigotPlayer, SkyPlayer {

	/*
	 - OfflineSpigotPlayerImpl Override
	 */

	@NotNull
	@Override
	Player getHandle();

	@NotNull
	default Player getOnlineHandle() {
		return getHandle();
	}

	@NotNull
	@Override
	default String getName() {
		return getHandle().getName();
	}

	/*
	 - BaseComponent array conversion (Message utility)
	 */

	default BaseComponent[] toBase(@NotNull String legacyStr) {
		return TextComponent.fromLegacyText(legacyStr);
	}

	default BaseComponent[] toBase(@NotNull Component component) {
		return BungeeComponentSerializer.get().serialize(component);
	}

	/*
	 - MessageReceiver implementation
	 */

	@Override
	default boolean sendMessage(@NotNull String message) {
		if (canReceive(message))
			getHandle().sendMessage(message);
		return true;
	}

	@Override
	default boolean sendMessage(@NotNull Component message) {
		if (canReceive(message))
			getHandle().spigot().sendMessage(toBase(message));
		return false;
	}

	/*
	 - ActionBar
	 */

	@Override
	default boolean sendActionBar(@NotNull String message) {
		if (canReceive(message))
			getHandle().spigot().sendMessage(ChatMessageType.ACTION_BAR, toBase(message));
		return true;
	}

	@Override
	default boolean sendActionBar(@NotNull Component message) {
		if (canReceive(message))
			getHandle().spigot().sendMessage(ChatMessageType.ACTION_BAR, toBase(message));
		return true;
	}

	/*
	 - Sounds
	 */

	@Override
	default boolean playSound(@NotNull Sound sound) {
		final Player handle = getHandle();
		handle.playSound(handle.getLocation(), sound.name().asString(), sound.volume(), sound.pitch());
		return true;
	}
}
