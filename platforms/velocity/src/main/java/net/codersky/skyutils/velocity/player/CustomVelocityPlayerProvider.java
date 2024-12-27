package net.codersky.skyutils.velocity.player;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import net.codersky.skyutils.crossplatform.player.PlayerProvider;
import net.codersky.skyutils.time.TaskScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("deprecation")
public abstract class CustomVelocityPlayerProvider<ON extends VelocityPlayer, OFF extends OfflineVelocityPlayer>
		extends PlayerProvider<Player, ON, UUID, OFF> {

	public CustomVelocityPlayerProvider(@NotNull TaskScheduler scheduler) {
		super(scheduler);
	}

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
}
