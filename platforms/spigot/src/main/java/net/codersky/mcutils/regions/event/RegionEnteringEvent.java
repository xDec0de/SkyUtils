package net.codersky.mcutils.regions.event;

import net.codersky.mcutils.events.player.CancellableMCPlayerEvent;
import net.codersky.mcutils.regions.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class RegionEnteringEvent extends CancellableMCPlayerEvent {

	private final Region region;
	private final static HandlerList handlers = new HandlerList();

	public RegionEnteringEvent(@Nonnull Player player, @Nonnull Region region) {
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
