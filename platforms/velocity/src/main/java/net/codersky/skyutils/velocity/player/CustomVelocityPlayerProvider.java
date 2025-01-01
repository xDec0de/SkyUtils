package net.codersky.skyutils.velocity.player;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import net.codersky.skyutils.crossplatform.player.PlayerProvider;
import net.codersky.skyutils.velocity.SkyUtilsVelocity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public abstract class CustomVelocityPlayerProvider<ON extends VelocityPlayer, OFF extends OfflineVelocityPlayer>
		extends PlayerProvider<Player, ON, UUID, OFF> {

	@NotNull
	@Override
	public UUID getOnlineUUID(@NotNull Player on) {
		return on.getUniqueId();
	}

	@NotNull
	@Override
	public UUID getOfflineUUID(@NotNull UUID off) {
		return off;
	}

	@Subscribe(order = PostOrder.CUSTOM, priority = Short.MAX_VALUE)
	public void onConnect(ServerPreConnectEvent event) {
		handleJoin(event.getPlayer());
	}

	@Subscribe(order = PostOrder.CUSTOM, priority = Short.MAX_VALUE)
	public void onDisconnect(DisconnectEvent event) {
		handleQuit(event.getPlayer());
	}

	@Override
	protected void scheduleOfflineRemoval(@NotNull UUID uuid) {
		SkyUtilsVelocity.getInstance().getScheduler()
				.delaySync(() -> offlineCache.remove(uuid), TimeUnit.MINUTES, 20);
	}
}
