package net.codersky.skyutils.crossplatform.player;

import net.codersky.skyutils.java.strings.Replacement;

/**
 * Cross-platform interface used to represent an <b>online</b> player.
 * This interface allows developers to use common methods
 * on all platforms without needing to actually use a
 * platform specific player type.
 * <p>
 * This interface extends the {@link Replacement} interface. Overriding
 * {@link Replacement#asReplacement()} with a call to {@link #getName()}
 * by default.
 *
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 */
public interface SkyPlayer extends OfflineSkyPlayer {

}
