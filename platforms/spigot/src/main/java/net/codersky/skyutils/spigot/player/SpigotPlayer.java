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

import java.util.UUID;

public class SpigotPlayer implements SkyPlayer {

	private final Player handle;

	protected SpigotPlayer(@NotNull Player handle) {
		this.handle = handle;
	}

	/*
	 * SkyPlayer implementation
	 */

	// Player identification //

	@NotNull
	@Override
	public final Player getHandle() {
		return handle;
	}

	@NotNull
	@Override
	public UUID getUniqueId() {
		return handle.getUniqueId();
	}

	@NotNull
	@Override
	public String getName() {
		return handle.getName();
	}

	@Override
	public boolean isOnline() {
		return handle.isOnline();
	}

	// Messages //

	@Override
	public boolean sendActionBar(@NotNull String message) {
		if (canReceive(message))
			handle.spigot().sendMessage(ChatMessageType.ACTION_BAR, toBase(message));
		return true;
	}

	@Override
	public boolean sendActionBar(@NotNull Component message) {
		if (canReceive(message))
			handle.spigot().sendMessage(ChatMessageType.ACTION_BAR, toBase(message));
		return true;
	}

	@Override
	public boolean playSound(@NotNull Sound sound) {
		handle.playSound(handle.getLocation(), sound.name().asString(), sound.volume(), sound.pitch());
		return true;
	}

	/*
	 * MessageReceiver implementation
	 */

	@Override
	public boolean sendMessage(@NotNull String message) {
		if (canReceive(message))
			handle.sendMessage(message);
		return true;
	}

	@Override
	public boolean sendMessage(@NotNull Component message) {
		if (canReceive(message))
			handle.spigot().sendMessage(toBase(message));
		return false;
	}

	/*
	 * BaseComponent array conversion
	 */

	private BaseComponent[] toBase(@NotNull String legacyStr) {
		return TextComponent.fromLegacyText(legacyStr);
	}

	private BaseComponent[] toBase(@NotNull Component component) {
		return BungeeComponentSerializer.get().serialize(component);
	}
}
