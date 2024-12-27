package net.codersky.skyutils.spigot.player;

import net.codersky.skyutils.time.TaskScheduler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpigotPlayerProvider extends CustomSpigotPlayerProvider<SpigotPlayer, OfflineSpigotPlayer> {

	public SpigotPlayerProvider(@NotNull TaskScheduler scheduler) {
		super(scheduler);
	}

	@NotNull
	@Override
	protected SpigotPlayer buildOnline(@NotNull Player player) {
		return new SpigotPlayer(player);
	}

	@NotNull
	@Override
	protected SpigotPlayer toOnline(@NotNull OfflineSpigotPlayer offline, @NotNull Player on) {
		return new SpigotPlayer(on);
	}

	@NotNull
	@Override
	protected OfflineSpigotPlayer toOffline(@NotNull SpigotPlayer online) {
		return new OfflineSpigotPlayer(online.getHandle());
	}
}
