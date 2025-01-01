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
	private final VelocityTaskScheduler scheduler;
	private final VelocityPlayerProvider playerProvider;

	@Inject
	public SkyUtilsVelocity(ProxyServer proxy, Logger logger) {
		instance = this;
		this.proxy = proxy;
		this.scheduler = new VelocityTaskScheduler(proxy, this);
		playerProvider = new VelocityPlayerProvider();
	}

	@NotNull
	public static SkyUtilsVelocity getInstance() {
		return  instance;
	}

	@NotNull
	public ProxyServer getProxy() {
		return proxy;
	}

	@NotNull
	public VelocityTaskScheduler getScheduler() {
		return scheduler;
	}

	@NotNull
	public VelocityPlayerProvider getPlayerProvider() {
		return playerProvider;
	}
}
