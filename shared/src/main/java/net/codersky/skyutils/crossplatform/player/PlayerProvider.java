package net.codersky.skyutils.crossplatform.player;

import net.codersky.skyutils.SkyUtils;
import net.codersky.skyutils.time.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * {@code abstract class} used to provide {@link SkyPlayer online} and
 * {@link OfflineSkyPlayer offline} players to {@link SkyUtils} instances.
 *
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 *
 * @param <ON_HANDLE> The {@link SkyPlayer#getHandle() handle} type of {@link ON online} players.
 * @param <ON> The {@link SkyPlayer online} player type.
 * @param <OFF_HANDLE> The {@link OfflineSkyPlayer#getHandle() handle} type of {@link OFF offline} players.
 * @param <OFF> The {@link OfflineSkyPlayer offline} player type.
 */
public abstract class PlayerProvider<ON_HANDLE, ON extends SkyPlayer, OFF_HANDLE, OFF extends OfflineSkyPlayer> {

	/**
	 * The {@link ON online} cache {@link HashMap}.
	 * Only edit it manually if you <b>REALLY</b> know what you are doing.
	 *
	 * @since SkyUtils 1.0.0
	 */
	protected final HashMap<UUID, ON> onlineCache = new HashMap<>();

	/**
	 * The {@link OFF offline} cache {@link HashMap}.
	 * Only edit it manually if you <b>REALLY</b> know what you are doing.
	 *
	 * @since SkyUtils 1.0.0
	 */
	protected final HashMap<UUID, OFF> offlineCache = new HashMap<>();

	/*
	 - UUID providers
	 */

	/**
	 * Gets the {@link UUID} of the provided {@link ON online} {@code handle} instance.
	 *
	 * @param handle The {@link ON_HANDLE handle} instance to get the {@link UUID} from.
	 *
	 * @return {@code handle}'s {@link UUID}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public abstract UUID getOnlineUUID(@NotNull ON_HANDLE handle);

	/**
	 * Gets the {@link UUID} of the provided {@link OFF offline} {@code handle} instance.
	 *
	 * @param handle The {@link OFF_HANDLE handle} instance to get the {@link UUID} from.
	 *
	 * @return {@code handle}'s {@link UUID}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public abstract UUID getOfflineUUID(@NotNull OFF_HANDLE handle);

	/*
	 - Player providers
	 */

	/**
	 * Builds a new {@link ON online} player from the provided {@code handle}.
	 *
	 * @param handle The {@link ON_HANDLE online} player to use.
	 *
	 * @return An {@link ON online} {@link SkyPlayer} player instance,
	 * created with the provided {@code handle}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	protected abstract ON buildOnline(@NotNull ON_HANDLE handle);

	/**
	 * Converts the provided {@code offline} player to an {@link ON online}
	 * {@link SkyPlayer} instance.
	 *
	 * @param offline The {@link OFF offline} player to use.
	 * @param onlineHandle The {@link ON_HANDLE} online player to use if needed.
	 *
	 * @return An {@link ON online} {@link SkyPlayer} instance, created with the
	 * provided {@code offline} player and {@code onlineHandle} instances.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	protected abstract ON toOnline(@NotNull OFF offline, @NotNull ON_HANDLE onlineHandle);

	/**
	 * Converts the provided {@code online} player to an {@link OFF offline}
	 * {@link SkyPlayer} instance.
	 *
	 * @param online The {@link OFF online} player to use.
	 *
	 * @return An {@link OFF offline} {@link OfflineSkyPlayer} instance, created with the
	 * provided {@code online} player instance.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	protected abstract OFF toOffline(@NotNull ON online);

	/*
	 - Player getters (UUID)
	 */

	/**
	 * Gets an {@link ON online} player instance by {@link UUID}, if cached.
	 *
	 * @param uuid The {@link UUID} of the <b>online</b> player to get.
	 *
	 * @return The matching {@link ON online} player instance, if cached.
	 * {@code null} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	public ON getOnline(@NotNull UUID uuid) {
		return onlineCache.get(uuid);
	}

	/**
	 * Gets an {@link OFF offline} player instance by {@link UUID}, if cached.
	 * An {@link OFF offline} player may still be returned if a matching
	 * online player is found. You can check if the player is actually offline
	 * with {@link OFF#isOnline()}
	 *
	 * @param uuid The {@link UUID} of the player to get.
	 *
	 * @return The matching {@link OFF offline} player instance, if cached.
	 * {@code null} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	public OFF getOffline(@NotNull UUID uuid) {
		final ON online = getOnline(uuid);
		return online == null ? offlineCache.get(uuid) : toOffline(online);
	}

	/*
	 - Public cache access
	 */

	/**
	 * Gets the {@link Collection} of all cached {@link ON online} players.
	 *
	 * @return The {@link Collection} of all cached {@link ON online} players.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public Collection<ON> getOnlineCache() {
		return onlineCache.values();
	}

	/*
	 - Cache modification
	 */

	/**
	 * Utility method that contains the <b>default</b> logic to handle
	 * {@link ON_HANDLE online} player joining. By default, this method
	 * gets the {@link UUID} of the {@code handle} with {@link #getOnlineUUID(Object)}.
	 * Then, attempts to get an already cached {@link OFF offline} player. If
	 * an {@link OFF offline} player is found, {@link #toOnline(OfflineSkyPlayer, Object)}
	 * is used to convert it to an {@link ON online} player instance. If not,
	 * {@link #buildOnline(Object)} is used to create a brand new {@link ON online} player instance.
	 * Then, the instance is added to the internal {@link #onlineCache} {@link HashMap}.
	 *
	 * @param handle The online {@link ON_HANDLE player} to handle.
	 *
	 * @since SkyUtils 1.0.0
	 */
	protected void handleJoin(@NotNull ON_HANDLE handle) {
		final UUID uuid = getOnlineUUID(handle);
		final OFF offline = offlineCache.remove(uuid);
		onlineCache.put(uuid, offline == null ? buildOnline(handle) : toOnline(offline, handle));
	}

	/**
	 * Utility method that contains the <b>default</b> logic to handle
	 * {@link OFF_HANDLE offline} player quitting. By default, this method
	 * gets the {@link UUID} of the {@code handle} with {@link #getOnlineUUID(Object)}.
	 * Then, attempts to remove a cached {@link ON_HANDLE online} player. If found,
	 * it converts it {@link #toOffline(SkyPlayer) to an offline} player and adds it to
	 * the {@link #offlineCache offline cache}. A task is then
	 * {@link #scheduleOfflineRemoval(UUID) scheduled} to remove the {@link OFF offline}
	 * player from the {@link #offlineCache offline cache}.
	 *
	 * @param handle The online {@link ON_HANDLE player} to handle.
	 *
	 * @since SkyUtils 1.0.0
	 */
	protected void handleQuit(@NotNull ON_HANDLE handle) {
		final UUID uuid = getOnlineUUID(handle);
		final ON online = onlineCache.remove(uuid);
		if (online == null)
			return;
		offlineCache.put(uuid, toOffline(online));
		scheduleOfflineRemoval(uuid);
	}

	/**
	 * Schedules the removal of an offline player from the {@link #offlineCache}.
	 * This is used by {@link #handleQuit(Object)}. By default, all SkyUtils
	 * {@link PlayerProvider player providers} use their plugin {@link TaskScheduler scheduler}
	 * to {@link TaskScheduler#delaySync(Runnable, TimeUnit, int) schedule}
	 * {@link HashMap#remove(Object) offlineCache.remove(uuid)} to be executed in
	 * {@code 20} {@link TimeUnit#MINUTES minutes}.
	 *
	 * @param uuid The {@link UUID} of the player to remove.
	 *
	 * @since SkyUtils 1.0.0
	 */
	protected abstract void scheduleOfflineRemoval(@NotNull UUID uuid);
}
