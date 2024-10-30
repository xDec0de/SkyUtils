package net.codersky.mcutils.storage;

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
