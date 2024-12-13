package net.codersky.skyutils.crossplatform.player;

import net.codersky.skyutils.cmd.SkyCommand;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.java.strings.Replacement;
import net.codersky.skyutils.java.strings.Replacer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

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
public interface SkyPlayer extends MessageReceiver, Replacement {

	/**
	 * Gets the platform-specific object that is being wrapped by
	 * this {@link SkyPlayer} instance. This can be, for example, a
	 * Bukkit Player instance.
	 *
	 * @return The platform-specific object that is being wrapped by
	 * this {@link SkyPlayer} instance.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	Object getHandle();

	/**
	 * Gets the {@link UUID} that belongs to this {@link SkyPlayer}.
	 * This {@link UUID} is persistent and can be used as a way to
	 * identify players in the future, as player names can change.
	 *
	 * @return The {@link UUID} that belongs to this {@link SkyPlayer}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	UUID getUniqueId();

	/**
	 * Gets the name of this {@link SkyPlayer}. Keep in mind that player
	 * names may change at any given time and should not be used
	 * for storage. {@link #getUniqueId() UUIDs} can be used for that purpose
	 * instead, names are only guaranteed to remain unchanged for a single session
	 * (Until the player logs out).
	 *
	 * @return The name of this {@link SkyPlayer}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	String getName();

	boolean isOnline();

	/*
	 * Legacy actionbar messages (String)
	 */

	/**
	 * Sends an ActionBar {@code message} to this {@link SkyPlayer}.
	 *
	 * @param message The message to send to this {@link SkyPlayer}.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand MCCommands}.
	 *
	 * @throws NullPointerException if {@code message} is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	boolean sendActionBar(@NotNull String message);

	/**
	 * Sends an ActionBar {@code message} to this {@link SkyPlayer}, applying {@code replacer}
	 * to {@code message} before sending it.
	 *
	 * @param message The ActionBar message to send to this {@link SkyPlayer}.
	 * @param replacer The {@link Replacer} to apply to the {@code message} before sending it.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand MCCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean sendActionBar(@NotNull String message, @NotNull Replacer replacer) {
		return !canReceive(message) || sendActionBar(replacer.replaceAt(message));
	}

	/**
	 * Sends an ActionBar {@code message} to this {@link MessageReceiver}, applying a {@link Replacer} made
	 * with the specified {@code replacements} to {@code message} before sending it.
	 *
	 * @param message The message to send to this {@link MessageReceiver}.
	 * @param replacements The replacements used to build a {@link Replacer} that will then be
	 * applied to the {@code message} before sending it. The amount of replacements must be even
	 * as specified on the {@link Replacer} {@link Replacer#Replacer constructor}.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand MCCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean sendActionBar(@NotNull String message, @NotNull Object... replacements) {
		return !canReceive(message) || sendActionBar(message, new Replacer(replacements));
	}

	/*
	 * Actionbar Adventure messages
	 */

	/**
	 * Sends an ActionBar {@link Component} {@code message} to this {@link SkyPlayer}.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link SkyPlayer}.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand MCCommands}.
	 *
	 * @throws NullPointerException if {@code message} is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	boolean sendActionBar(@NotNull Component message);

	/**
	 * Sends an ActionBar {@link Component} {@code message} to this {@link SkyPlayer}, applying {@code replacer}
	 * to {@code message} before sending it.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link SkyPlayer}.
	 * @param replacer The {@link Replacer} to apply to the {@code message} before sending it.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand MCCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean sendActionBar(@NotNull Component message, @NotNull Replacer replacer) {
		return !canReceive(message) || sendActionBar(replacer.replaceAt(message));
	}

	/**
	 * Sends an ActionBar {@link Component} {@code message} to this {@link SkyPlayer}, applying
	 * a {@link Replacer} made with the specified {@code replacements} to {@code message} before sending it.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link SkyPlayer}.
	 * @param replacements The replacements used to build a {@link Replacer} that will then be
	 * applied to the {@code message} before sending it. The amount of replacements must be even
	 * as specified on the {@link Replacer} {@link Replacer#Replacer constructor}.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand MCCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean sendActionBar(@NotNull Component message, @NotNull Object... replacements) {
		return !canReceive(message) || sendActionBar(message, new Replacer(replacements));
	}

	/*
	 * Sound
	 */

	boolean playSound(@NotNull Sound sound);

	default boolean playSound(@KeyPattern @NotNull String key, @NotNull Sound.Source source, float volume, float pitch) {
		return playSound(Sound.sound(Key.key(key), source, volume, pitch));
	}

	default boolean playSound(@KeyPattern @NotNull String key, float volume, float pitch) {
		return playSound(key, Sound.Source.AMBIENT, volume, pitch);
	}

	default boolean playSound(@KeyPattern @NotNull String key, float volume) {
		return playSound(key, volume, 1.0f);
	}

	default boolean playSound(@KeyPattern @NotNull String key) {
		return playSound(key, 1.0f);
	}

	/*
	 * Replacement
	 */

	@NotNull
	@Override
	default String asReplacement() {
		return getName();
	}
}
