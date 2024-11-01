package net.codersky.mcutils.storage;

import net.codersky.mcutils.Reloadable;
import net.codersky.mcutils.java.MCCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public interface Config extends DataHandler, Reloadable {

	/**
	 * Does any necessary tasks in order to set up this {@link Config}.
	 * Keep in mind that this does <b>NOT</b> load the config, this method
	 * is intended for tasks such as creating any necessary file, obviously
	 * depending on the {@link Config} type.
	 *
	 * @return {@code true} if the {@link Config} was set up correctly,
	 * {@code false} otherwise.
	 *
	 * @since MCUtils 1.0.0
	 */
	boolean setup();

	/**
	 * Saves the cached data of this {@link Config} to
	 * the actual config. Some configs may take a long
	 * time to save, so it is recommended to call this
	 * method <b>asynchronously</b> in order to prevent
	 * possible performance issues.
	 *
	 * @return {@code true} if this {@link Config} was
	 * able to save correctly, {@code false} otherwise.
	 *
	 * @since MCUtils 1.0.0
	 */
	boolean save();

	/**
	 * Loads the data stored on this config to the cache
	 * of this {@link Config} instance. Some configs may
	 * take a long time to load, so it is recommended to
	 * call this method <b>asynchronously</b> in order
	 * to prevent possible performance issues.
	 *
	 * @return {@code true} if this {@link Storage} was
	 * able to load correctly, {@code false} otherwise.
	 *
	 * @since MCUtils 1.0.0
	 */
	boolean reload();

	/*
	 * Key access
	 */

	default Set<Map.Entry<String, Object>> getEntries() {
		return getMap().getEntries();
	}

	@NotNull
	default Config removeEntries(@NotNull String... keys) {
		getMap().removeEntries(keys);
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
		return getMap().getKeys();
	}

	@NotNull
	default Set<String> getKeys(@NotNull String parent) {
		return getMap().getKeys(parent);
	}

	default boolean containsKey(@NotNull String... keys) {
		return getMap().containsKeys(keys);
	}

	@NotNull
	default Config clear() {
		getMap().clear();
		return this;
	}

	/*
	 * Setters
	 */

	// - Strings - //

	@NotNull
	default String setString(@NotNull String key, @NotNull String value) {
		return getMap().set(key, value);
	}

	@NotNull
	default List<String> setStrings(@NotNull String key, @NotNull List<String> value) {
		return getMap().setList(key, value);
	}

	// - Characters - //

	default char setChar(@NotNull String key, char value) {
		return getMap().set(key, value);
	}

	@NotNull
	default List<Character> setChars(@NotNull String key, @NotNull List<Character> value) {
		return getMap().setList(key, value);
	}

	// - Booleans - //

	default boolean setBoolean(@NotNull String key, boolean value) {
		return getMap().set(key, value);
	}

	@NotNull
	default List<Boolean> setBooleans(@NotNull String key, @NotNull List<Boolean> value) {
		return getMap().setList(key, value);
	}

	// - Integers - //

	default int setInt(@NotNull String key, int value) {
		return getMap().set(key, value);
	}

	@NotNull
	default List<Integer> setInts(@NotNull String key, @NotNull List<Integer> value) {
		return getMap().setList(key, value);
	}

	// - Longs - //

	default long setLong(@NotNull String key, long value) {
		return getMap().set(key, value);
	}

	@NotNull
	default List<Long> setLongs(@NotNull String key, @NotNull List<Long> value) {
		return getMap().setList(key, value);
	}

	// - Floats - //

	default float setFloat(@NotNull String key, float value) {
		return getMap().set(key, value);
	}

	default List<Float> setFloats(@NotNull String key, @NotNull List<Float> value) {
		return getMap().setList(key, value);
	}

	// - Doubles - //

	default double setDouble(@NotNull String key, double value) {
		return getMap().set(key, value);
	}

	@NotNull
	default List<Double> setDoubles(@NotNull String key, @NotNull List<Double> value) {
		return getMap().setList(key, value);
	}

	// - UUIDs - //

	@NotNull
	default UUID setUUID(@NotNull String key, @NotNull UUID value) {
		return getMap().set(key, value);
	}

	@NotNull
	default List<UUID> setUUIDs(@NotNull String key, @NotNull List<UUID> value) {
		return getMap().setList(key, value);
	}

	/*
	 * Getters
	 */

	// - Strings - //

	/**
	 * Gets a {@link String} from this {@link Reloadable}.
	 *
	 * @param key the key to get the {@link String} from.
	 *
	 * @return The stored {@link String} or {@code null} if {@code key}
	 * didn't exist or contains a different type of value.
	 *
	 * @throws NullPointerException if {@code key} is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	default String getString(@NotNull String key) {
		return getMap().get(key, String.class);
	}

	@NotNull
	default String getString(@NotNull String key, @NotNull String def) {
		final String str = getString(key);
		return str == null ? def : str;
	}

	@Nullable
	default List<String> getStrings(@NotNull String key) {
		return getMap().getList(key, String.class);
	}

	@NotNull
	default List<String> getStrings(@NotNull String key, @NotNull List<String> def) {
		final List<String> lst = getStrings(key);
		return lst == null ? def : lst;
	}

	// - Characters - //

	@Nullable
	default Character getChar(@NotNull String key) {
		return getMap().get(key, Character.class);
	}

	default char getChar(@NotNull String key, char def) {
		final Character ch = getChar(key);
		return ch == null ? def : ch;
	}

	@Nullable
	default List<Character> getChars(@NotNull String key) {
		return getMap().getList(key, Character.class);
	}

	@NotNull
	default List<Character> getChars(@NotNull String key, @NotNull List<Character> def) {
		final List<Character> lst = getChars(key);
		return lst == null ? def : lst;
	}

	// - Booleans - //

	@Nullable
	default Boolean getBoolean(@NotNull String key) {
		return getMap().get(key, Boolean.class);
	}

	default boolean getBoolean(@NotNull String key, boolean def) {
		final Boolean bool = getBoolean(key);
		return bool == null ? def : bool;
	}

	@Nullable
	default List<Boolean> getBooleans(@NotNull String key) {
		return getMap().getList(key, Boolean.class);
	}

	@NotNull
	default List<Boolean> getBooleans(@NotNull String key, @NotNull List<Boolean> def) {
		final List<Boolean> lst = getBooleans(key);
		return lst == null ? def : lst;
	}

	// - Integers - //

	@Nullable
	default Integer getInt(@NotNull String key) {
		return getMap().get(key, Integer.class);
	}

	default int getInt(@NotNull String key, int def) {
		final Integer i = getInt(key);
		return i == null ? def : i;
	}

	@Nullable
	default List<Integer> getInts(@NotNull String key) {
		return getMap().getList(key, Integer.class);
	}

	@NotNull
	default List<Integer> getInts(@NotNull String key, @NotNull List<Integer> def) {
		final List<Integer> lst = getInts(key);
		return lst == null ? def : lst;
	}

	// - Longs - //

	@Nullable
	default Long getLong(@NotNull String key) {
		return getMap().get(key, Long.class);
	}

	default long getLong(@NotNull String key, long def) {
		final Long l = getLong(key);
		return l == null ? def : l;
	}

	@Nullable
	default List<Long> getLongs(@NotNull String key) {
		return getMap().getList(key, Long.class);
	}

	@NotNull
	default List<Long> getLongs(@NotNull String key, @NotNull List<Long> def) {
		final List<Long> lst = getLongs(key);
		return lst == null ? def : lst;
	}

	// - Floats - //

	@Nullable
	default Float getFloat(@NotNull String key) {
		return getMap().get(key, Float.class);
	}

	default float getFloat(@NotNull String key, float def) {
		final Float f = getFloat(key);
		return f == null ? def : f;
	}

	@Nullable
	default List<Float> getFloats(@NotNull String key) {
		return getMap().getList(key, Float.class);
	}

	@NotNull
	default List<Float> getFloats(@NotNull String key, @NotNull List<Float> def) {
		final List<Float> lst = getFloats(key);
		return lst == null ? def : lst;
	}

	// - Doubles - //

	@Nullable
	default Double getDouble(@NotNull String key) {
		return getMap().get(key, Double.class);
	}

	default double getDouble(@NotNull String key, double def) {
		final Double d = getDouble(key);
		return d == null ? def : d;
	}

	@Nullable
	default List<Double> getDoubles(@NotNull String key)  {
		return getMap().getList(key, Double.class);
	}

	@NotNull
	default List<Double> getDoubles(@NotNull String key, @NotNull List<Double> def) {
		final List<Double> lst = getDoubles(key);
		return lst == null ? def : lst;
	}

	// - UUID - //

	@Nullable
	default UUID getUUID(@NotNull String key) {
		return getMap().get(key, UUID.class);
	}

	@NotNull
	default UUID getUUID(@NotNull String key, @NotNull UUID def) {
		final UUID uuid = getUUID(key);
		return uuid == null ? def : uuid;
	}

	@Nullable
	default List<UUID> getUUIDs(@NotNull String key) {
		return getMap().getList(key, UUID.class);
	}

	@NotNull
	default List<UUID> getUUIDs(@NotNull String key, @NotNull List<UUID> def) {
		final List<UUID> lst = getUUIDs(key);
		return lst == null ? def : lst;
	}
}
