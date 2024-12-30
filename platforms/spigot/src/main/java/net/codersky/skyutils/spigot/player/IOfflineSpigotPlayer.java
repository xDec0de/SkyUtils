package net.codersky.skyutils.spigot.player;

import net.codersky.skyutils.crossplatform.player.OfflineSkyPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IOfflineSpigotPlayer extends OfflineSkyPlayer {

	@NotNull
	OfflinePlayer getHandle();

	@Nullable
	default Player getOnlineHandle() {
		return getHandle().getPlayer();
	}

	@NotNull
	@Override
	default UUID getUniqueId() {
		return getHandle().getUniqueId();
	}

	@NotNull
	@Override
	default String getName() {
		final String name = getHandle().getName();
		return name == null ? "" : name;
	}

	@Override
	default boolean isOnline() {
		return getHandle().isOnline();
	}
}
