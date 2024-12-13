package net.codersky.skyutils;

import net.codersky.skyutils.cmd.GlobalCommand;
import net.codersky.skyutils.crossplatform.SkyConsole;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.UUID;

/**
 * Platform independent class that provides access to
 * most of the features that SkyUtils has to offer, just
 * check the "See Also" section here.
 *
 * @since SkyUtils 1.0.0
 *
 * @see #getPlugin()
 * @see #getPlayer(UUID)
 * @see #getConsole()
 *
 * @author xDec0de_
 *
 * @param <P> The class that holds an instance of this
 * utils, meant to be the main class of your plugin as
 * it can be accessed via {@link #getPlugin()}.
 */
public abstract class SkyUtils<P> {

	private final P plugin;
	protected final LinkedHashSet<Reloadable> reloadables = new LinkedHashSet<>();
	protected final SkyConsole console = getConsoleInstance();

	public SkyUtils(@NotNull P plugin) {
		this.plugin = Objects.requireNonNull(plugin);
	}

	@NotNull
	public final P getPlugin() {
		return plugin;
	}

	@NotNull
	public abstract File getDataFolder();

	/*
	 - Players
	 */

	@Nullable
	public abstract SkyPlayer getPlayer(@NotNull UUID uuid);

	/*
	 - Console
	 */

	@NotNull
	protected abstract SkyConsole getConsoleInstance();

	/**
	 * Provides a cross-platform {@link SkyConsole} instance.
	 *
	 * @return A cross-platform {@link SkyConsole} instance.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public abstract SkyConsole getConsole();

	/*
	 - Platform
	 */

	/**
	 * Gets the {@link MCPlatform} this {@link SkyUtils} class
	 * is designed for. This can be useful when you are using
	 * a generic {@link SkyUtils} instance, to let you know which
	 * platform you are in without casting.
	 *
	 * @return The {@link MCPlatform} this {@link SkyUtils} class
	 * is designed for.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public abstract MCPlatform getPlatform();

	/*
	 - Version
	 */

	/**
	 * Gets the version of SkyUtils being used by this utility
	 * class.
	 *
	 * @return The version of SkyUtils being used by this utility
	 * class.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public final String getSkyUtilsVersion() {
		return "1.0.0"; // Keep it the same as the project version from build.gradle.kts
	}

	/*
	 - Commands
	 */

	public abstract void registerCommands(GlobalCommand<P>... commands);

	/*
	 - Reloadables
	 */

	/**
	 * Registers a new {@link Reloadable} and optionally {@link Reloadable#reload() reloads}
	 * if after its registration. Registered {@link Reloadable Reloadables} will be
	 * {@link Reloadable#reload() reloaded} when calling {@link #reload()}.
	 *
	 * @param reloadable The {@link Reloadable} to register.
	 * @param reload Whether to reload {@link Reloadable#reload() reloaded}
	 * the {@code reloadable} or not upon registering it.
	 *
	 * @return The provided {@link Reloadable}.
	 *
	 * @since SkyUtils 1.0.0
	 * 
	 * @see Reloadable
	 * @see #registerReloadable(Reloadable)
	 * @see #reload()
	 */
	@NotNull
	public Reloadable registerReloadable(@NotNull Reloadable reloadable, boolean reload) {
		reloadables.add(Objects.requireNonNull(reloadable));
		if (reload)
			reloadable.reload();
		return reloadable;
	}

	/**
	 * Registers and {@link Reloadable#reload() reloads} a new {@link Reloadable}.
	 * Registered {@link Reloadable Reloadables} will be {@link Reloadable#reload() reloaded}
	 * when calling {@link #reload()}. To choose whether you want to {@link Reloadable#reload() reload}
	 * the {@link Reloadable} use {@link #registerReloadable(Reloadable, boolean)}
	 *
	 * @param reloadable The {@link Reloadable} to register.
	 *
	 * @return The provided {@link Reloadable}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see Reloadable
	 * @see #registerReloadable(Reloadable, boolean)
	 * @see #reload()
	 */
	@NotNull
	public Reloadable registerReloadable(@NotNull Reloadable reloadable) {
		return registerReloadable(reloadable, true);
	}

	/**
	 * {@link Reloadable#reload() Reloads} every {@link Reloadable} that
	 * has been registered on this instance of {@link SkyUtils}.
	 *
	 * @return The number of {@link Reloadable Reloadables} that failed to {@link Reloadable#reload() reload}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see Reloadable
	 * @see #registerReloadable(Reloadable)
	 * @see #registerReloadable(Reloadable, boolean)
	 */
	public int reload() {
		int failures = 0;
		for (Reloadable reloadable : reloadables)
			if (!reloadable.reload())
				failures++;
		return failures;
	}
}
