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
import org.jetbrains.annotations.Nullable;

public class SpigotPlayer extends OfflineSpigotPlayer implements SkyPlayer {

	protected SpigotPlayer(@NotNull Player handle) {
		super(handle);
	}

	/*
	 - OfflineSpigotPlayer Override
	 */

	@NotNull
	@Override
	public Player getHandle() {
		return (Player) super.getHandle();
	}

	@Nullable
	public Player getOnlineHandle() {
		return getHandle();
	}

	@NotNull
	@Override
	public String getName() {
		return getHandle().getName();
	}

	/*
	 - BaseComponent array conversion (Message utility)
	 */

	BaseComponent[] toBase(@NotNull String legacyStr) {
		return TextComponent.fromLegacyText(legacyStr);
	}

	BaseComponent[] toBase(@NotNull Component component) {
		return BungeeComponentSerializer.get().serialize(component);
	}

	/*
	 - MessageReceiver implementation
	 */

	@Override
	public boolean sendMessage(@NotNull String message) {
		if (canReceive(message))
			getHandle().sendMessage(message);
		return true;
	}

	@Override
	public boolean sendMessage(@NotNull Component message) {
		if (canReceive(message))
			getHandle().spigot().sendMessage(toBase(message));
		return false;
	}

	/*
	 - ActionBar
	 */

	@Override
	public boolean sendActionBar(@NotNull String message) {
		if (canReceive(message))
			getHandle().spigot().sendMessage(ChatMessageType.ACTION_BAR, toBase(message));
		return true;
	}

	@Override
	public boolean sendActionBar(@NotNull Component message) {
		if (canReceive(message))
			getHandle().spigot().sendMessage(ChatMessageType.ACTION_BAR, toBase(message));
		return true;
	}

	/*
	 - Sounds
	 */

	@Override
	public boolean playSound(@NotNull Sound sound) {
		final Player handle = getHandle();
		handle.playSound(handle.getLocation(), sound.name().asString(), sound.volume(), sound.pitch());
		return true;
	}
}
