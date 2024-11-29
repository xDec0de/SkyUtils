package net.codersky.skyutils.spigot.updaters.sources;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.codersky.skyutils.spigot.updaters.UpdateChecker;
import net.codersky.skyutils.spigot.updaters.UpdaterSource;

/**
 * An interface that represents version information generated
 * by an {@link UpdaterSource}.
 * 
 * @since SkyUtils 1.0.0
 * 
 * @author xDec0de_
 *
 * @see UpdateChecker#getLatestVersion()
 */
public abstract class VersionInfo {

	protected UpdaterSource source;

	VersionInfo() {}

	/**
	 * The {@link UpdaterSource} that provided this info.
	 * This may be {@code null} if no source provided the version,
	 * in that case, {@link #getVersion()} will be the current
	 * version of the plugin. 
	 * 
	 * @return The {@link UpdaterSource} that provided this info.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	public final UpdaterSource getSource() {
		return source;
	}

	/**
	 * Returns the never {@code null} version {@link String}.
	 * 
	 * @return The version {@link String}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@Nonnull
	public abstract String getVersion();

	/**
	 * Gets the download url of this version. This method
	 * is allowed to point to any page that contains a
	 * download link to the latest version of the plugin,
	 * for example, {@link SpigotUpdaterSource.SpigotVersionInfo} and
	 * {@link SpigetUpdaterSource.SpigetVersionInfo} point to the list of versions
	 * of the plugin instead of the latest version.
	 * 
	 * @return The download url of this version.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@Nonnull
	public abstract String getVersionUrl();
}
