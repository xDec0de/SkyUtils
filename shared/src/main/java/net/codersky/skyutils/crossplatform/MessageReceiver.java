package net.codersky.skyutils.crossplatform;

import net.codersky.skyutils.cmd.SkyCommand;
import net.codersky.skyutils.cmd.SkyCommandSender;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.codersky.skyutils.java.strings.Replacer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Cross-platform interface used to handle objects that
 * may receive messages such as {@link SkyPlayer} or {@link SkyConsole}.
 * This interface supports both regular string messages and Adventure
 * {@link Component} messages.
 *
 * @since SkyUtils 1.0.0
 *
 * @see #sendMessage(String)
 * @see #sendMessage(String, Replacer)
 * @see #sendMessage(String, Object...)
 * @see SkyConsole
 * @see SkyPlayer
 * @see SkyCommandSender
 *
 * @author xDec0de_
 */
public interface MessageReceiver {

	/**
	 * Gets the name of this {@link MessageReceiver}. The way this works
	 * can vary depending on the platform and implementation, details are
	 * provided on the documentation of each implementation, but here is
	 * a not so detailed list of what you can expect, this only includes
	 * player and console types that implement {@link SkyPlayer},
	 * {@link SkyConsole} and {@link SkyCommandSender}:
	 * <ul>
	 *   <li>Spigot: <i>CommandSender#getName</i></li>
	 *   <li>Velocity: <i>Player#getUsername</i> or <i>"Console"</i>.</li>
	 * </ul>
	 *
	 * @return The name of this {@link MessageReceiver}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	String getName();

	/**
	 * Checks if this {@link MessageReceiver} can receive the specified
	 * {@code message}. <b>Implementations</b> must make sure to use this
	 * method before sending any type of message to the actual receiver
	 * as {@link MessageReceiver} must ignore empty or {@code null} messages.
	 * <p>
	 * If you really want to send an "empty" message, you can just send a
	 * blank one with a space on it. This is considered an intentionally
	 * empty message and can be sent to {@link MessageReceiver MessageReceivers}.
	 *
	 * @param message The {@link String} message to check.
	 *
	 * @return {@code true} if the {@code message} can be sent to any
	 * {@link MessageReceiver}, {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean canReceive(@Nullable String message) {
		return message != null && !message.isEmpty();
	}

	/**
	 * Checks if this {@link MessageReceiver} can receive the specified
	 * {@code message}. <b>Implementations</b> must make sure to use this
	 * method before sending any type of message to the actual receiver
	 * as {@link MessageReceiver} must ignore empty or {@code null} messages.
	 * <p>
	 * If you really want to send an "empty" message, you can just send a
	 * blank one with a space on it. This is considered an intentionally
	 * empty message and can be sent to {@link MessageReceiver MessageReceivers}.
	 *
	 * @param message The {@link Component} message to check.
	 *
	 * @return {@code true} if the {@code message} can be sent to any
	 * {@link MessageReceiver}, {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean canReceive(@Nullable Component message) {
		return message != null && Component.IS_NOT_EMPTY.test(message);
	}

	/*
	 * Legacy messages (String)
	 */

	/**
	 * Sends a {@code message} to this {@link MessageReceiver}.
	 *
	 * @param message The message to send to this {@link MessageReceiver}.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand MCCommands}.
	 *
	 * @throws NullPointerException if {@code message} is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	boolean sendMessage(@NotNull String message);

	/**
	 * Sends a {@code message} to this {@link MessageReceiver}, applying {@code replacer}
	 * to {@code message} before sending it.
	 *
	 * @param message The message to send to this {@link MessageReceiver}.
	 * @param replacer The {@link Replacer} to apply to the {@code message} before sending it.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand MCCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean sendMessage(@NotNull String message, @NotNull Replacer replacer) {
		return !canReceive(message) || sendMessage(replacer.replaceAt(message));
	}

	/**
	 * Sends a {@code message} to this {@link MessageReceiver}, applying a {@link Replacer} made
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
	default boolean sendMessage(@NotNull String message, @NotNull Object... replacements) {
		return !canReceive(message) || sendMessage(message, new Replacer(replacements));
	}

	/*
	 * Adventure messages
	 */

	/**
	 * Sends a {@link Component} {@code message} to this {@link MessageReceiver}.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link MessageReceiver}.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand MCCommands}.
	 *
	 * @throws NullPointerException if {@code message} is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	boolean sendMessage(@NotNull Component message);

	/**
	 * Sends a {@link Component} {@code message} to this {@link MessageReceiver}, applying {@code replacer}
	 * to {@code message} before sending it.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link MessageReceiver}.
	 * @param replacer The {@link Replacer} to apply to the {@code message} before sending it.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand MCCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean sendMessage(@NotNull Component message, @NotNull Replacer replacer) {
		return !canReceive(message) || sendMessage(replacer.replaceAt(message));
	}

	/**
	 * Sends a {@link Component} {@code message} to this {@link MessageReceiver}, applying
	 * a {@link Replacer} made with the specified {@code replacements} to {@code message} before sending it.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link MessageReceiver}.
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
	default boolean sendMessage(@NotNull Component message, @NotNull Object... replacements) {
		return !canReceive(message) || sendMessage(message, new Replacer(replacements));
	}
}
