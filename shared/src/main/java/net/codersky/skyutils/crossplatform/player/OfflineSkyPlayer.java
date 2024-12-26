package net.codersky.skyutils.crossplatform.player;

import net.codersky.skyutils.java.strings.Replacement;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Cross-platform interface used to represent an <b>offline</b> player.
 * This interface allows developers to use common methods
 * on all platforms without needing to actually use a
 * platform specific offline player type.
 * <p>
 * This {@code interface} {@code extends} {@link Replacement}. By default
 * {@link #asReplacement()} redirects to {@link #getName()}.
 *
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 */
public interface OfflineSkyPlayer extends Replacement {

	/**
	 * Gets the platform-specific object that is being wrapped by
	 * this {@link OfflineSkyPlayer player} instance. This can be, for example, a
	 * Bukkit OfflinePlayer instance.
	 *
	 * @return The platform-specific object that is being wrapped by
	 * this {@link OfflineSkyPlayer player} instance.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	Object getHandle();

	/**
	 * Gets the {@link UUID} that belongs to this {@link OfflineSkyPlayer player}.
	 * This {@link UUID} is persistent and can be used as a way to
	 * identify players in the future, as player names can change.
	 *
	 * @return The {@link UUID} that belongs to this {@link OfflineSkyPlayer player}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	UUID getUniqueId();

	/**
	 * Gets the name of this {@link OfflineSkyPlayer player}. Keep in mind that player
	 * names may change at any given time and should not be used
	 * for storage. {@link #getUniqueId() UUIDs} can be used for that purpose
	 * instead, names are only guaranteed to remain unchanged for a single session
	 * (Until the player logs out).
	 *
	 * @return The name of this {@link OfflineSkyPlayer player}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	String getName();

	@NotNull
	@Override
	default String asReplacement() {
		return getName();
	}

	boolean isOnline();
}
