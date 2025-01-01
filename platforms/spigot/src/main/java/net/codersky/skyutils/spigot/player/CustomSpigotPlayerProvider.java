package net.codersky.skyutils.spigot.player;

import net.codersky.skyutils.crossplatform.player.PlayerProvider;
import net.codersky.skyutils.spigot.SkyUtilsSpigot;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class CustomSpigotPlayerProvider<ON extends SpigotPlayer, OFF extends OfflineSpigotPlayer>
		extends PlayerProvider<Player, ON, OfflinePlayer, OFF> implements Listener {

	@NotNull
	@Override
	public UUID getOnlineUUID(@NotNull Player on) {
		return on.getUniqueId();
	}

	@NotNull
	@Override
	public UUID getOfflineUUID(@NotNull OfflinePlayer off) {
		return off.getUniqueId();
	}

	@ApiStatus.Internal
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent e) {
		handleJoin(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e) {
		handleQuit(e.getPlayer());
	}

	@Override
	protected void scheduleOfflineRemoval(@NotNull UUID uuid) {
		SkyUtilsSpigot.getInstance().getScheduler()
				.delaySync(() -> offlineCache.remove(uuid), TimeUnit.MINUTES, 20);
	}
}
