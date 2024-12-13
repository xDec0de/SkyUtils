package net.codersky.skyutils.general;

import net.codersky.skyutils.SkyUtils;
import net.codersky.skyutils.cmd.SkyCommand;

/**
 * An interface that when implemented allows to easily
 * register, reload and disable certain {@link SkyUtils plugin}
 * features that, optionally, can be enabled or disabled on the
 * {@link SkyUtils plugin} {@link SkyUtils#getConfig() config}.
 * See the methods below for more information.
 * 
 * @author xDec0de_
 *
 * @see SkyUtils#registerFeature(Feature, String)
 * @see #onEnable()
 * @see #onDisable()
 * 
 * @since SkyUtils 1.0.0
 */
public interface Feature {

	/**
	 * Called whenever this feature gets enabled or reloaded.
	 * A feature is generally enabled for the first time
	 * whenever a plugin enables, however, this may not always
	 * be the case, as {@link SkyUtils#reloadFeatures()} will
	 * call {@link #onDisable()} and {@link #onEnable()}.
	 * 
	 * @return {@code true} if the feature enabled successfully,
	 * {@code false} otherwise.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	public boolean onEnable();

	/**
	 * Called whenever this feature gets disabled or reloaded.
	 * Remember to unregister any {@link Listener listener} and
	 * {@link SkyCommand command} that may be registered when
	 * {@link #onEnable()} is called, as if this method is called
	 * because of a reload, {@link #onEnable()} will be called right
	 * after this method, leading to unexpected behavior if anything
	 * gets registered twice. 
	 * 
	 * @return {@code true} if the feature disabled successfully,
	 * {@code false} otherwise.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	public boolean onDisable();
}
