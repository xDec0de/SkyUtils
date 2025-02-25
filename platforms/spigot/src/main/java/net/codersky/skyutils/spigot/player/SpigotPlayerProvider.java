package net.codersky.skyutils.spigot.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpigotPlayerProvider extends CustomSpigotPlayerProvider<SpigotPlayerImpl, OfflineSpigotPlayerImpl> {

	@NotNull
	@Override
	protected SpigotPlayerImpl buildOnline(@NotNull Player player) {
		return new SpigotPlayerImpl(player);
	}

	@NotNull
	@Override
	protected SpigotPlayerImpl toOnline(@NotNull OfflineSpigotPlayerImpl offline, @NotNull Player on) {
		return new SpigotPlayerImpl(on);
	}

	@NotNull
	@Override
	protected OfflineSpigotPlayerImpl toOffline(@NotNull SpigotPlayerImpl online) {
		return new OfflineSpigotPlayerImpl(online.getHandle());
	}
}
