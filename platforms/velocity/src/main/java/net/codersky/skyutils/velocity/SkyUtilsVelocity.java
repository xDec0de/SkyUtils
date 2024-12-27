package net.codersky.skyutils.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import net.codersky.skyutils.velocity.player.VelocityPlayerProvider;
import net.codersky.skyutils.velocity.time.VelocityTaskScheduler;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import java.util.logging.Logger;

public class SkyUtilsVelocity {

	static SkyUtilsVelocity instance;
	private final ProxyServer proxy;
	private final VelocityPlayerProvider playerProvider;

	@Inject
	public SkyUtilsVelocity(ProxyServer proxy, Logger logger) {
		instance = this;
		this.proxy = proxy;
		playerProvider = new VelocityPlayerProvider(new VelocityTaskScheduler(proxy, this));
	}

	@NotNull
	public static SkyUtilsVelocity getInstance() {
		return  instance;
	}

	@NotNull
	public VelocityPlayerProvider getPlayerProvider() {
		return playerProvider;
	}

	@NotNull
	public ProxyServer getProxy() {
		return proxy;
	}
}
