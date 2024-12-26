package net.codersky.skyutils.crossplatform.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public abstract class PlayerProvider<ON_HANDLE, ON extends SkyPlayer, OFF_HANDLE, OFF extends OfflineSkyPlayer> {

	private final HashMap<UUID, ON> onlineCache = new HashMap<>();
	private final HashMap<UUID, OFF> offlineCache = new HashMap<>();

	/*
	 - UUID providers
	 */

	@NotNull
	public abstract UUID getOnlineUUID(@NotNull ON_HANDLE handle);

	@NotNull
	public abstract UUID getOfflineUUID(@NotNull OFF_HANDLE handle);

	/*
	 - Player providers
	 */

	@NotNull
	protected abstract ON buildOnline(@NotNull ON_HANDLE handle);

	@NotNull
	protected abstract ON toOnline(@NotNull OFF offline, @NotNull ON_HANDLE onlineHandle);

	@NotNull
	protected abstract OFF toOffline(@NotNull ON online);

	/*
	 - Player getters (UUID)
	 */

	@Nullable
	public ON getOnline(@NotNull UUID uuid) {
		return onlineCache.get(uuid);
	}

	@Nullable
	public OFF getOffline(@NotNull UUID uuid) {
		return offlineCache.get(uuid);
	}

	/*
	 - Cache access
	 */

	@NotNull
	public Collection<ON> getOnlineCache() {
		return onlineCache.values();
	}

	/*
	 - Cache modification
	 */

	protected void handleJoin(@NotNull ON_HANDLE handle) {
		final UUID uuid = getOnlineUUID(handle);
		final OFF offline = offlineCache.remove(uuid);
		onlineCache.put(uuid, offline == null ? buildOnline(handle) : toOnline(offline, handle));
	}

	protected void handleQuit(@NotNull ON_HANDLE handle) {
		final UUID uuid = getOnlineUUID(handle);
		final ON online = onlineCache.remove(uuid);
		if (online == null)
			return;
		offlineCache.put(uuid, toOffline(online));
	}
}
