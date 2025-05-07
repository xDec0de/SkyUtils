package net.codersky.skyutils.crossplatform.player;

import net.codersky.jsky.strings.Replacer;
import net.codersky.skyutils.SkyReplacer;
import net.codersky.skyutils.cmd.SkyCommand;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Cross-platform interface used to represent an <b>online</b> player.
 * This interface allows developers to use common methods
 * on all platforms without needing to actually use a
 * platform specific player type.
 *
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 */
public interface SkyPlayer extends OfflineSkyPlayer, MessageReceiver {

	@NotNull
	@Override
	default String asReplacement() {
		return getName();
	}

	/*
	 - JSON messages
	 */

	boolean sendJsonMessage(@NotNull String json);

	/*
	 - Legacy actionbar messages (String)
	 */

	/**
	 * Sends an ActionBar {@code message} to this {@link OfflineSkyPlayer player}.
	 *
	 * @param message The message to send to this {@link OfflineSkyPlayer player}.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand SkyCommands}.
	 *
	 * @throws NullPointerException if {@code message} is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	boolean sendActionBar(@NotNull String message);

	/**
	 * Sends an ActionBar {@code message} to this {@link OfflineSkyPlayer player}, applying {@code replacer}
	 * to {@code message} before sending it.
	 *
	 * @param message The ActionBar message to send to this {@link OfflineSkyPlayer player}.
	 * @param replacer The {@link Replacer} to apply to the {@code message} before sending it.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand SkyCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean sendActionBar(@NotNull String message, @NotNull Replacer replacer) {
		return !canReceive(message) || sendActionBar(replacer.replaceAt(message));
	}

	/**
	 * Sends an ActionBar {@code message} to this {@link OfflineSkyPlayer player}, applying a {@link Replacer} made
	 * with the specified {@code replacements} to {@code message} before sending it.
	 *
	 * @param message The message to send to this {@link OfflineSkyPlayer player}.
	 * @param replacements The replacements used to build a {@link Replacer} that will then be
	 * applied to the {@code message} before sending it. The amount of replacements must be even
	 * as specified on the {@link Replacer} {@link Replacer#Replacer constructor}.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand SkyCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean sendActionBar(@NotNull String message, @NotNull Object... replacements) {
		return !canReceive(message) || sendActionBar(message, new Replacer(replacements));
	}

	/*
	 - Actionbar Adventure messages
	 */

	/**
	 * Sends an ActionBar {@link Component} {@code message} to this {@link OfflineSkyPlayer player}.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link OfflineSkyPlayer player}.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand SkyCommands}.
	 *
	 * @throws NullPointerException if {@code message} is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	boolean sendActionBar(@NotNull Component message);

	/**
	 * Sends an ActionBar {@link Component} {@code message} to this {@link OfflineSkyPlayer player},
	 * applying {@code replacer} to {@code message} before sending it.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link OfflineSkyPlayer player}.
	 * @param replacer The {@link Replacer} to apply to the {@code message} before sending it.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand SkyCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean sendActionBar(@NotNull Component message, @NotNull SkyReplacer replacer) {
		return !canReceive(message) || sendActionBar(replacer.replaceAt(message));
	}

	/**
	 * Sends an ActionBar {@link Component} {@code message} to this {@link OfflineSkyPlayer player}, applying
	 * a {@link Replacer} made with the specified {@code replacements} to {@code message} before sending it.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link OfflineSkyPlayer player}.
	 * @param replacements The replacements used to build a {@link Replacer} that will then be
	 * applied to the {@code message} before sending it. The amount of replacements must be even
	 * as specified on the {@link Replacer} {@link Replacer#Replacer constructor}.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand SkyCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean sendActionBar(@NotNull Component message, @NotNull Object... replacements) {
		return !canReceive(message) || sendActionBar(message, new Replacer(replacements));
	}

	/*
	 - Sound
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
}
