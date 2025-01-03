package net.codersky.skyutils.spigot.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nonnull;

/**
 * A simple type of {@link MCEvent} that
 * implements the {@link Cancellable} interface,
 * so you don't have to manually do it, see
 * {@link MCEvent} for more details.
 * 
 * @author xDec0de_
 *
 * @since SkyUtils 1.0.0
 */
public abstract class CancellableMCEvent extends MCEvent implements Cancellable {

	private boolean cancelled = false;

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	/**
	 * Calls this {@link CancellableMCEvent}, a shortcut to
	 * {@link PluginManager#callEvent(Event)} that returns this
	 * {@link CancellableMCEvent} to be used further instead of {@code void}.
	 *
	 * @return This {@link CancellableMCEvent}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nonnull
	public CancellableMCEvent call() {
		Bukkit.getPluginManager().callEvent(this);
		return this;
	}
}
