package net.codersky.mcutils.storage;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * The most basic type of interface capable of handling data.
 * This interface is designed for any classes that are capable
 * of storing data on a {@link DataMap}. This interface offers
 * no methods to access specific values. Implementations must
 * provide and use the {@link #getMap()} method to get and set data.
 *
 * @since MCUtils 1.0.0
 *
 * @see Config
 * @see Storage
 *
 * @author xDec0de_
 */
public interface DataHandler {

	/**
	 * Provides access to the <b>internal</b> {@link DataMap} that
	 * this {@link DataHandler} is using. Keep in mind once again
	 * that this is for <b>internal</b> usage only, and you should
	 * not use it unless you <b>really</b> know what you are doing.
	 * That being said, for any {@link DataHandler} implementation
	 * that saves this map, please remember that it features a
	 * {@link DataMap#isModified() modification} check and that you
	 * should {@link DataMap#setModified(boolean) reset} it every
	 * time you save in order to avoid saving unmodified data.
	 *
	 * @return the <b>internal</b> {@link DataMap} that this
	 * {@link DataHandler} is using.
	 *
	 * @since MCUtils 1.0.0
	 */
	@ApiStatus.Internal
	@NotNull
	DataMap getMap();

	/**
	 * Checks whether this {@link DataHandler} supports key nesting or not.
	 * Key nesting essentially means that keys can have maps as a value,
	 * for example, {@link net.codersky.mcutils.storage.files.yaml.YamlFile YamlFile}
	 * supports key nesting as this is allowed:
	 * <pre>
	 * notNested: 1
	 * key:
	 *   nestedKey: 2
	 *   anotherNestedKey: 3
	 * </pre>
	 * {@code notNested}'s key would be just <i>"notNested"</i>, however,
	 * {@code nestedKey}'s key would be <i>"key.nestedKey"</i>. Internally, this means
	 * that {@code key}'s value is another map with its own keys.
	 *
	 * @return {@code true} if this {@link DataHandler} supports key nesting,
	 * {@code false} otherwise.
	 */
	default boolean supportsKeyNesting() {
		return getMap().usesNesting();
	}
}
