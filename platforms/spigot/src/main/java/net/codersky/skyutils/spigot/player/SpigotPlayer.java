package net.codersky.skyutils.spigot.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpigotPlayer extends OfflineSpigotPlayer implements ISpigotPlayer {

	protected SpigotPlayer(@NotNull Player handle) {
		super(handle);
	}

	@NotNull
	@Override
	public Player getHandle() {
		return (Player) super.getHandle();
	}
}
