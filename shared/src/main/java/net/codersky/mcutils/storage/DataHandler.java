package net.codersky.mcutils.storage;

import net.codersky.mcutils.java.MCCollections;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * The most basic type of interface capable of handling data.
 * This interface is designed for any classes that are capable
 * of storing data on a {@link HashMap} cache where the key is
 * always a {@link String}. This interface offers no methods to
 * access specific values, it only offers very basic control over a
 * {@link HashMap} that must be provided by implementations on
 * the {@link #getMap()} method. Generic getters and setters are
 * also provided to manipulate the map, while public, these getters
 * and setters are marked as {@link ApiStatus.Internal} as extensions
 * such as {@link Config} provide specific type getters and setters
 * like {@link Config#getString(String)} or {@link Config#setString(String, String)}.
 *
 * @since MCUtils 1.0.0
 *
 * @see Config
 * @see Storage
 *
 * @author xDec0de_
 */
public interface DataHandler {

	/*
	 * Key access
	 */

	@NotNull
	HashMap<String, Object> getMap();

	default Set<Map.Entry<String, Object>> getEntries() {
		return getMap().entrySet();
	}

	default Set<Map.Entry<String, Object>> getEntries(@NotNull Predicate<String> filter) {
		return MCCollections.clone(getEntries(), entry -> filter.test(entry.getKey()));
	}

	@NotNull
	default DataHandler removeEntries(@NotNull String... keys) {
		for (String key : keys)
			getMap().remove(key);
		return this;
	}

	/**
	 * Gets the {@link Set} of keys that are currently
	 * cached on this {@link Storage}. This set
	 * supports element removal but not addition as
	 * explained on {@link HashMap#keySet()}. Elements
	 * removed from this {@link Set} will also be removed
	 * from the storage cache. This can be used, for example
	 * to {@link Set#clear() clear} the file.
	 *
	 * @return The {@link Set} of keys that are currently
	 * cached on this {@link Config}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #removeEntries(String...)
	 * @see #containsKey(String...)
	 */
	default Set<String> getKeys() {
		return getMap().keySet();
	}

	@NotNull
	default Set<String> getKeys(@NotNull Predicate<String> filter) {
		return MCCollections.clone(getKeys(), filter);
	}

	default boolean containsKey(@NotNull String... keys) {
		for (String key : keys)
			if (!getMap().containsKey(key))
				return false;
		return true;
	}

	@NotNull
	default DataHandler clear() {
		getMap().clear();
		return this;
	}

	/*
	 * Setters
	 */

	// - Utility - //

	@NotNull
	@ApiStatus.Internal
	default <T> T set(@NotNull String key, @NotNull T value) {
		getMap().put(Objects.requireNonNull(key), Objects.requireNonNull(value));
		return value;
	}

	@NotNull
	@ApiStatus.Internal
	default <T> List<T> setList(@NotNull String key, @NotNull List<T> list) {
		Objects.requireNonNull(list);
		final ArrayList<T> lst = list instanceof ArrayList ? (ArrayList<T>) list : new ArrayList<>(list);
		getMap().put(Objects.requireNonNull(key), lst);
		return list;
	}

	/*
	 * Getters
	 */

	// - Utility - //

	@ApiStatus.Internal
	@SuppressWarnings("unchecked")
	default <T> T get(@NotNull String key, @NotNull Class<T> type) {
		final Object obj = getMap().get(key);
		return (obj != null && obj.getClass().equals(type)) ? (T) obj : null;
	}

	@ApiStatus.Internal
	@SuppressWarnings("unchecked")
	default <T> List<T> getList(@NotNull String key, @NotNull Class<T> type) {
		final Object obj = getMap().get(key);
		if (obj instanceof ArrayList<?> lst)
			return lst.getClass().getTypeParameters()[0].getClass().equals(type) ? (List<T>) lst : null;
		return null;
	}
}
