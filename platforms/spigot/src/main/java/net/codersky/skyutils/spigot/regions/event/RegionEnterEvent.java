package net.codersky.skyutils.spigot.regions.event;

import net.codersky.skyutils.spigot.events.player.MCPlayerEvent;
import net.codersky.skyutils.spigot.regions.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class RegionEnterEvent extends MCPlayerEvent {

	private final Region region;
	private final static HandlerList handlers = new HandlerList();

	public RegionEnterEvent(@Nonnull Player player, @Nonnull Region region) {
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
