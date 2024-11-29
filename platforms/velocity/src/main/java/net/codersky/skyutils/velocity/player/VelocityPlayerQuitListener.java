package net.codersky.skyutils.velocity.player;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import net.codersky.skyutils.velocity.VelocityUtils;
import org.jetbrains.annotations.NotNull;

public class VelocityPlayerQuitListener {

	private final VelocityUtils<?> utils;

	public VelocityPlayerQuitListener(@NotNull VelocityUtils<?> utils) {
		this.utils = utils;
	}

	@Subscribe(order = PostOrder.CUSTOM, priority = Short.MAX_VALUE)
	public void onDisconnect(DisconnectEvent event) {
		utils.getPlayerProvider().removeFromCache(event.getPlayer().getUniqueId());
	}
}
