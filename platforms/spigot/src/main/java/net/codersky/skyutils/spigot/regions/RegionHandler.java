package net.codersky.skyutils.spigot.regions;

import net.codersky.jsky.collections.JCollections;
import net.codersky.skyutils.spigot.events.player.CancellableMCPlayerEvent;
import net.codersky.skyutils.spigot.regions.event.RegionEnterEvent;
import net.codersky.skyutils.spigot.regions.event.RegionEnteringEvent;
import net.codersky.skyutils.spigot.regions.event.RegionLeaveEvent;
import net.codersky.skyutils.spigot.regions.event.RegionLeavingEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class RegionHandler implements Listener {

	private final HashMap<UUID, HashSet<Region>> regions = new HashMap<>();

	public RegionHandler init(@Nonnull JavaPlugin plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		return this;
	}

	@Nonnull
	public Set<Region> getRegionsAt(@Nonnull World world) {
		final Set<Region> worldRegions = regions.get(world.getUID());
		return worldRegions == null ? Collections.emptySet() : JCollections.clone(worldRegions);
	}

	@Nonnull
	public Set<Region> getRegionsAt(@Nonnull Location location) {
		if (!location.isWorldLoaded() || location.getWorld() == null)
			return Collections.emptySet();
		final Set<Region> worldRegions = getRegionsAt(location.getWorld());
		final int x = location.getBlockX();
		final int y = location.getBlockY();
		final int z = location.getBlockZ();
		return JCollections.remove(worldRegions, region -> !region.contains(x, y, z));
	}

	public Region getPriorityRegionAt(@Nonnull Location loc) {
		return null;
	}

	public boolean addRegion(@Nonnull Region region) {
		final UUID worldId = region.getWorld().getUID();
		final HashSet<Region> worldRegions = regions.get(worldId);
		if (worldRegions == null) {
			regions.put(worldId, JCollections.asHashSet(region));
		} else
			return worldRegions.add(region);
		return true;
	}

	/*
	 * Enter / Leave handling
	 */

	@ApiStatus.Internal
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (!hasChangedBlock(e.getFrom(), e.getTo()))
			return;
		// Get all regions at from and to
		final Set<Region> to = getRegionsAt(e.getTo());
		final Set<Region> from = getRegionsAt(e.getFrom());
		// Filter regions that are being entered or left
		final Set<Region> entering = getRegionChanges(from, to, r -> new RegionEnteringEvent(e.getPlayer(), r));
		final Set<Region> leaving = getRegionChanges(to, from, r -> new RegionLeavingEvent(e.getPlayer(), r));
		// Cancel event if any called event was cancelled
		if (entering == null || leaving == null)
			e.setCancelled(true);
		else { // Call success events
			entering.forEach(entered -> new RegionEnterEvent(e.getPlayer(), entered).call());
			leaving.forEach(left -> new RegionLeaveEvent(e.getPlayer(), left).call());
		}
	}

	private boolean hasChangedBlock(@Nonnull Location from, @Nullable Location to) {
		if (to == null)
			return false;
		if (!Objects.equals(from.getWorld(), to.getWorld()))
			return true;
		return from.getBlockX() != to.getBlockX() ||
				from.getBlockY() != to.getBlockY() ||
				from.getBlockZ() != to.getBlockZ();
	}

	private Set<Region> getRegionChanges(Set<Region> a, Set<Region> b, Function<Region, CancellableMCPlayerEvent> fun) {
		final Set<Region> entered = new HashSet<>();
		for (Region onB : b) {
			if (a.contains(onB))
				continue;
			if (!fun.apply(onB).call().isCancelled())
				return null;
			else
				entered.add(onB);
		}
		return entered;
	}
}
