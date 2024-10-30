package net.codersky.mcutils.storage;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataMap {

	private final boolean useNesting;
	private final LinkedHashMap<String, Object> map = new LinkedHashMap<>();

	public DataMap(boolean useNesting) {
		this.useNesting = useNesting;
	}

	@NotNull
	@ApiStatus.Internal
	public HashMap<String, Object> getInternalMap() {
		return map;
	}

	public boolean usesNesting() {
		return useNesting;
	}

	/*
	 * Nesting utilities
	 */

	private String getActualKey(@NotNull String key) {
		final int lastSeparator = useNesting ? key.lastIndexOf('.') : -1;
		return lastSeparator == -1 ? key : key.substring(lastSeparator + 1);
	}

	private Map<String, Object> getActualMap(@NotNull String key, boolean create) {
		final int lastSeparator = useNesting ? key.lastIndexOf('.') : -1;
		return lastSeparator == -1 ? this.map : getNestedMap(key.substring(0, lastSeparator), create);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	private Map<String, Object> getNestedMap(@NotNull String key, boolean create) {
		final String[] keys = key.split("\\.");
		Map<String, Object> last = this.map;
		for (String subKey : keys) {
			final Object value = last.get(subKey);
			if (value instanceof Map<?, ?> subMap)
				last = (Map<String, Object>) subMap;
			else if (create) {
				final Map<String, Object> created = new LinkedHashMap<>();
				last.put(subKey, created);
				last = created;
			} else
				return null;
		}
		return last;
	}

	/*
	 * Map access
	 */

	@NotNull
	public DataMap clear() {
		this.map.clear();
		return this;
	}

	@NotNull
	public Set<Map.Entry<String, Object>> getEntries() {
		return this.map.entrySet();
	}

	@NotNull
	public Set<Map.Entry<String, Object>> getEntries(@NotNull String key) {
		final Map<String, Object> source = getNestedMap(key, false);
		return source == null ? Set.of() : source.entrySet();
	}

	@NotNull
	public DataMap removeEntries(@NotNull String... keys) {
		for (String key : keys) {
			if (useNesting) {
				final Map<String, Object> source = getActualMap(key, false);
				if (source != null)
					source.remove(getActualKey(key));
			} else
				this.map.remove(key);
		}
		return this;
	}

	public boolean containsKeys(@NotNull String... keys) {
		for (String key : keys) {
			if (useNesting) {
				final Map<String, Object> source = getActualMap(key, false);
				if (source == null)
					return false;
			} else if (!this.map.containsKey(key))
				return false;
		}
		return true;
	}

	@NotNull
	public Set<String> getKeys() {
		return this.map.keySet();
	}

	@NotNull
	public Set<String> getKeys(@NotNull String key) {
		final Map<String, Object> source = getNestedMap(key, false);
		return source == null ? Set.of() : source.keySet();
	}

	/*
	 * Getters
	 */

	// - Utility - //

	@Nullable
	@SuppressWarnings("unchecked")
	private <T> T getFromMap(@NotNull Map<?, ?> source, @NotNull String key, @NotNull Class<T> type) {
		final Object obj = source.get(key);
		return obj != null && obj.getClass().equals(type) ? (T) obj : null;
	}

	// - Objects - //

	@Nullable
	public <T> T get(@NotNull String key, @NotNull Class<T> type) {
		final String actualKey = getActualKey(key);
		final Map<String, Object> source = getActualMap(key, false);
		return source == null ? null : getFromMap(source, actualKey, type);
	}

	@NotNull
	@SuppressWarnings("unchecked")
	public <T> T get(@NotNull String key, T def) {
		final Object value = get(key, def.getClass());
		return value == null ? def : (T) value;
	}

	// - Lists - //

	@Nullable
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(@NotNull String key, @NotNull Class<T> type) {
		final Object obj = get(key, Object.class);
		if (obj instanceof LinkedList<?> lst)
			return lst.getClass().getTypeParameters()[0].getClass().equals(type) ? (List<T>) lst : null;
		return null;
	}

	@NotNull
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(@NotNull String key, @NotNull List<T> def) {
		final Class<T> type = (Class<T>) def.getClass().getTypeParameters()[0].getClass();
		final List<T> lst = getList(key, type);
		return lst == null ? def : lst;
	}

	/*
	 * Setters
	 */

	@NotNull
	public <T> T set(@NotNull String key, @NotNull T value) {
		if (value instanceof List<?>) {
			setList(key, (List<?>) value);
			return value;
		}
		final String actualKey = getActualKey(key);
		final Map<String, Object> source = getActualMap(key, true);
		if (source != null)
			source.put(actualKey, value);
		return value;
	}

	@NotNull
	public <T> List<T> setList(@NotNull String key, @NotNull List<T> value) {
		final String actualKey = getActualKey(key);
		final Map<String, Object> source = getActualMap(key, true);
		if (source == null)
			return value;
		if (value instanceof LinkedList<T> lst)
			source.put(actualKey, lst);
		else
			source.put(actualKey, new LinkedList<>(value));
		return value;
	}
}
