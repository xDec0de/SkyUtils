package net.codersky.skyutils.spigot.regions.event;

import net.codersky.skyutils.spigot.events.player.CancellableMCPlayerEvent;
import net.codersky.skyutils.spigot.regions.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class RegionLeavingEvent extends CancellableMCPlayerEvent {

	private final Region region;
	private final static HandlerList handlers = new HandlerList();

	public RegionLeavingEvent(@Nonnull Player player, @Nonnull Region region) {
		super(player);
		this.region = region;
	}

	@Nonnull
	public final Region getRegion() {
		return region;
	}

	@Nonnull
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Nonnull
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
