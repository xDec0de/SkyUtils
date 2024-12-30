package net.codersky.skyutils.spigot.player;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class OfflineSpigotPlayerImpl implements OfflineSpigotPlayer {

	private final OfflinePlayer handle;

	protected OfflineSpigotPlayerImpl(@NotNull OfflinePlayer handle) {
		this.handle = handle;
	}

	@NotNull
	@Override
	public OfflinePlayer getHandle() {
		return handle;
	}
}
