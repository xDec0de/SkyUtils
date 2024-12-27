package net.codersky.skyutils.velocity.player;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.codersky.skyutils.crossplatform.player.OfflineSkyPlayer;
import net.codersky.skyutils.velocity.SkyUtilsVelocity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OfflineVelocityPlayer implements OfflineSkyPlayer {

	private final UUID uuid;

	protected OfflineVelocityPlayer(@NotNull UUID uuid) {
		this.uuid = uuid;
	}

	@NotNull
	public ProxyServer getProxy() {
		return SkyUtilsVelocity.getInstance().getProxy();
	}

	/*
	 - OfflineSkyPlayer implementation
	 */

	@NotNull
	@Override
	public Object getHandle() {
		return uuid;
	}

	@Nullable
	public Player getOnlineHandle() {
		return getProxy().getPlayer(uuid).orElse(null);
	}

	@NotNull
	@Override
	public UUID getUniqueId() {
		return uuid;
	}

	@NotNull
	@Override
	public String getName() {
		// TODO: Add an actual implementation for this. Maybe with Mojang's API?
		final Player online = getOnlineHandle();
		return online == null ? "" : online.getUsername();
	}

	@Override
	public boolean isOnline() {
		final Player online = getOnlineHandle();
		return online != null && online.isActive();
	}
}
