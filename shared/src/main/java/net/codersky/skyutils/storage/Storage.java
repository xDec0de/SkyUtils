package net.codersky.skyutils.storage;

import net.codersky.skyutils.java.SkyCollections;
import net.codersky.skyutils.storage.files.FlatStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Abstract class for any type of storage that
 * can be {@link #reload() reloaded} and {@link #save() saved}.
 * This class also provides methods to get and set most
 * data types, which include:
 * <ul>
 * <li>{@link String Strings}</li>
 * <li>{@link Character Characters}</li>
 * <li>{@link Boolean Booleans}</li>
 * <li>{@link UUID UUIDs}</li>
 * <li>{@link Byte Bytes}</li>
 * <li>{@link Short Shorts}</li>
 * <li>{@link Integer Integers}</li>
 * <li>{@link Long Longs}</li>
 * <li>{@link Float Float}</li>
 * <li>{@link Double Doubles}</li>
 * </ul>
 * Any class extending this one is only required to be
 * able to store the types shown on the list above, as
 * single objects and as lists, as there are methods for
 * both cases, for example {@link #getString(String)}
 * and {@link #getStrings(String)} respectively.
 * <p>
 * Other types that are also supported by this class such as {@link Date}
 * are considered "conversion types", meaning that for example
 * {@link #getDate(String)} instead of getting a {@link Date} object
 * from the {@link #getKeys() keys}, gets a {@link Long}, then converts
 * it to {@link Date}.
 * 
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 * 
 * @see FlatStorage
 */
public abstract class Storage implements Config {

	private final DataMap map;

	public Storage(boolean useNesting) {
		this.map = new DataMap(useNesting);
	}

	@NotNull
	@Override
	public DataMap getMap() {
		return map;
	}

	/**
	 * Does any necessary tasks in order to set up this {@link Storage}.
	 * Keep in mind that this does <b>NOT</b> load the storage, this method
	 * is intended for tasks such as creating any necessary file or connecting
	 * to the storage database, obviously depending on the {@link Storage} type.
	 *
	 * @return {@code true} if the {@link Storage} was set up correctly,
	 * {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 */
	public abstract boolean setup();

	/*
	 * Setters
	 */

	// - Bytes - //

	public byte setByte(@NotNull String key, byte value) {
		return getMap().set(key, value);
	}

	@NotNull
	public List<Byte> setBytes(@NotNull String key, @NotNull List<Byte> value) {
		return getMap().setList(key, value);
	}

	@NotNull
	public List<Byte> setBytes(@NotNull String key, byte[] value) {
		return getMap().setList(key, SkyCollections.asByteList(value));
	}

	// - Shorts - //

	public short setShort(@NotNull String key, short value) {
		return getMap().set(key, value);
	}

	@NotNull
	public List<Short> setShorts(@NotNull String key, @NotNull List<Short> value) {
		return getMap().setList(key, value);
	}

	/*
	 * Setters by conversion
	 */

	// - Dates - //

	@Nullable
	public Date setDate(@NotNull String key, @NotNull Date value) {
		getMap().set(key, value.toInstant().toEpochMilli());
		return value;
	}

	@NotNull
	public List<Date> setDates(@NotNull String key, @NotNull List<Date> value) {
		setLongs(key, SkyCollections.map(value, date -> date.toInstant().toEpochMilli()));
		return value;
	}

	/*
	 * Getters
	 */

	// - Bytes - //

	@Nullable
	public Byte getByte(@NotNull String key) {
		return getMap().get(key, Byte.class);
	}

	public byte getByte(@NotNull String key, byte def) {
		final Byte b = getByte(key);
		return b == null ? def : b;
	}

	@Nullable
	public List<Byte> getBytes(@NotNull String key) {
		return getMap().getList(key, Byte.class);
	}

	@NotNull
	public List<Byte> getBytes(@NotNull String key, @NotNull List<Byte> def) {
		final List<Byte> lst = getBytes(key);
		return lst == null ? def : lst;
	}

	// - Shorts - //

	@Nullable
	public Short getShort(@NotNull String key) {
		return getMap().get(key, Short.class);
	}

	public short getShort(@NotNull String key, short def) {
		final Short s = getShort(key);
		return s == null ? def : s;
	}

	@Nullable
	public List<Short> getShorts(@NotNull String key) {
		return getMap().getList(key, Short.class);
	}

	@NotNull
	public List<Short> getShorts(@NotNull String key, @NotNull List<Short> def) {
		final List<Short> lst = getShorts(key);
		return lst == null ? def : lst;
	}

	/*
	 * Getters by conversion
	 */

	// Date //

	@Nullable
	public Date getDate(@NotNull String key) {
		final Long millis = getLong(key);
		return millis == null ? null : new Date(millis);
	}

	@NotNull
	public Date getDate(@NotNull String key, @NotNull Date def) {
		final Date date = getDate(key);
		return date == null ? def : date;
	}

	@Nullable
	public List<Date> getDates(@NotNull String key) {
		return getDates(key, null);
	}

	@Nullable
	public List<Date> getDates(@NotNull String key, @Nullable List<Date> def) {
		final List<Date> lst = getDates(key);
		return lst == null ? def : lst;
	}

	/*
	 * Object class
	 */

	@Override
	public boolean equals(@Nullable Object obj) {
		return obj instanceof final Config cfg && cfg.getMap().equals(this.getMap());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getMap());
	}

	@Override
	public String toString() {
		return "Storage" + getMap();
	}
}
