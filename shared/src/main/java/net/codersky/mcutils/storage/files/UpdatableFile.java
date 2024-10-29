package net.codersky.mcutils.storage.files;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Interface designed for files that can be updated,
 * optionally ignoring a list of paths.
 *
 * @since MCUtils 1.0.0
 *
 * @see #update(List)
 * @see #update(String...)
 * @see #update()
 *
 * @author xDec0de_
 */
public interface UpdatableFile {

	/**
	 * Updates any files handled by this {@link UpdatableFile}.
	 *
	 * @param ignored A list of paths that shall be
	 * ignored when updating files, {@code null} or an empty
	 * list indicates no paths are ignored.
	 *
	 * @return {@code true} on success, {@code false} on failure.
	 *
	 * @since MCUtils 1.0.0
	 */
	boolean update(@Nullable List<String> ignored);

	/**
	 * Updates any files handled by this {@link UpdatableFile}.
	 *
	 * @param ignored An array of paths that shall be
	 * ignored when updating files, {@code null} or an empty
	 * array indicates no paths are ignored.
	 *
	 * @return {@code true} on success, {@code false} on failure.
	 *
	 * @since MCUtils 1.0.0
	 */
	default boolean update(@Nullable String... ignored) {
		return ignored == null ? update() : update(List.of(ignored));
	}

	/**
	 * Updates any files handled by this {@link UpdatableFile}
	 * without ignoring any path.
	 *
	 * @return {@code true} on success, {@code false} on failure.
	 *
	 * @since MCUtils 1.0.0
	 */
	default boolean update() {
		return update(List.of());
	}
}
