package net.codersky.skyutils.spigot.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpigotPlayerImpl extends OfflineSpigotPlayerImpl implements SpigotPlayer {

	protected SpigotPlayerImpl(@NotNull Player handle) {
		super(handle);
	}

	@NotNull
	@Override
	public Player getHandle() {
		return (Player) super.getHandle();
	}
}
