package net.codersky.skyutils.velocity.player;

import com.velocitypowered.api.proxy.Player;
import net.codersky.skyutils.time.TaskScheduler;
import org.jetbrains.annotations.NotNull;

public class VelocityPlayerProvider extends CustomVelocityPlayerProvider<VelocityPlayer, OfflineVelocityPlayer> {

	public VelocityPlayerProvider(@NotNull TaskScheduler scheduler) {
		super(scheduler);
	}

	@NotNull
	@Override
	protected VelocityPlayer buildOnline(@NotNull Player player) {
		return new VelocityPlayer(player);
	}

	@NotNull
	@Override
	protected VelocityPlayer toOnline(@NotNull OfflineVelocityPlayer offline, @NotNull Player onlineHandle) {
		return new VelocityPlayer(onlineHandle);
	}

	@NotNull
	@Override
	protected OfflineVelocityPlayer toOffline(@NotNull VelocityPlayer online) {
		return new OfflineVelocityPlayer(online.getUniqueId());
	}
}
