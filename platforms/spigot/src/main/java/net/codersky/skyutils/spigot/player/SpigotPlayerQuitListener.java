package net.codersky.skyutils.spigot.player;

import net.codersky.skyutils.spigot.SpigotUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class SpigotPlayerQuitListener implements Listener {

	private final SpigotUtils<?> utils;

	public SpigotPlayerQuitListener(@NotNull SpigotUtils<?> utils) {
		this.utils = utils;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e) {
		utils.getPlayerProvider().removeFromCache(e.getPlayer().getUniqueId());
	}
}
