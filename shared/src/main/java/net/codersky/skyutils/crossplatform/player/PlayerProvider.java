package net.codersky.skyutils.crossplatform.player;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public abstract class PlayerProvider<T> {

	private final HashMap<UUID, SkyPlayer> playerCache = new HashMap<>();

	@Nullable
	protected abstract SkyPlayer fetchPlayer(@NotNull UUID uuid);

	@Nullable
	public abstract UUID getUUID(@NotNull T handle);

	@Nullable
	public SkyPlayer getPlayer(@NotNull UUID uuid) {
		SkyPlayer player = playerCache.get(uuid);
		if (player != null) {
			if (player.isOnline()) // Player quit listeners should take care of this, but just in case...
				return player;
			playerCache.remove(uuid);
		}
		player = fetchPlayer(uuid);
		if (player != null)
			playerCache.put(uuid, player);
		return player;
	}

	@Nullable
	public SkyPlayer getPlayer(@NotNull T original) {
		final UUID uuid = getUUID(original);
		return uuid == null ? null : getPlayer(uuid);
	}

	@ApiStatus.Internal
	protected void removeFromCache(@NotNull UUID uuid) {
		playerCache.remove(uuid);
	}
}
