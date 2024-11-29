package net.codersky.skyutils.crossplatform.proxy;

import net.codersky.skyutils.SkyUtils;
import org.jetbrains.annotations.NotNull;

/**
 * An extension of {@link SkyUtils} meant for proxy
 * platforms such as Velocity or BungeeCord.
 *
 * @param <P> The class that holds an instance of this
 * utils, meant to be the main class of your plugin as
 * it can be accessed via {@link #getPlugin()}.
 *
 * @author xDec0de_
 *
 * @since SkyUtils 1.0.0
 */
public abstract class ProxyUtils<P> extends SkyUtils<P> {

	public ProxyUtils(@NotNull P plugin) {
		super(plugin);
	}
}
