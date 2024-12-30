package net.codersky.skyutils.spigot.player;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class OfflineSpigotPlayer implements IOfflineSpigotPlayer {

	private final OfflinePlayer handle;

	protected OfflineSpigotPlayer(@NotNull OfflinePlayer handle) {
		this.handle = handle;
	}

	@NotNull
	@Override
	public OfflinePlayer getHandle() {
		return handle;
	}
}
