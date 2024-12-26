package net.codersky.skyutils.spigot.player;

import net.codersky.skyutils.crossplatform.player.OfflineSkyPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OfflineSpigotPlayer implements OfflineSkyPlayer {

	private final OfflinePlayer handle;

	protected OfflineSpigotPlayer(@NotNull OfflinePlayer handle) {
		this.handle = handle;
	}

	/*
	 - OfflineSkyPlayer implementation
	 */

	@NotNull
	@Override
	public OfflinePlayer getHandle() {
		return handle;
	}

	@Nullable
	public Player getOnlineHandle() {
		return handle.getPlayer();
	}

	@NotNull
	@Override
	public UUID getUniqueId() {
		return handle.getUniqueId();
	}

	@NotNull
	@Override
	public String getName() {
		final String name = handle.getName();
		return name == null ? "" : name;
	}

	@Override
	public boolean isOnline() {
		return handle.isOnline();
	}
}
