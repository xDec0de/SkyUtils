package net.codersky.skyutils.files;

import net.codersky.jsky.strings.Replacer;
import net.codersky.skyutils.Reloadable;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.java.strings.SkyStrings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An interface that is designed to get messages from a file that
 * can then be sent to a {@link MessageReceiver}. This interface
 * extends the {@link Reloadable} interface, though it generally
 * also implements {@link UpdatableFile}.
 * <p>
 * Messages sent by files that implement this interface must apply
 * all patterns provided by SkyUtils. This is default behaviour, and
 * it is already provided by the interface itself. The only method
 * that should be implemented is {@link #getRawMessage(String)}, everything
 * else has a default implementation that shouldn't be modified.
 * 
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 */
public interface MessagesFile extends Reloadable {

	/**
	 * Gets the default {@link Replacer} that will be applied to every message
	 * sent by this {@link MessagesFile}. This default {@link Replacer} may be
	 * {@code null}, in which case no default replacements will be applied to
	 * the messages. This is generally used for placeholders such as a plugin
	 * prefix like "prefix". Note that this {@link Replacer} replaces <b>before</b>
	 * any other {@link Replacer} that may be used on other methods such as
	 * {@link #send(MessageReceiver, String, Replacer)}. Meaning that by the
	 * time the {@link Replacer} passed to that method takes action, the message
	 * will already be modified by this {@link Replacer}, so this default {@link Replacer}
	 * <b>always</b> takes priority.
	 *
	 * @return The default {@link Replacer} of this {@link MessagesFile},
	 * {@code null} if no default {@link Replacer} has been set.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	Replacer getDefaultReplacer();

	/**
	 * Sets the default {@link Replacer} of this {@link MessagesFile}. This
	 * default {@link Replacer} may be {@code null}, in which case no default
	 * replacements will be applied to the messages. This is generally used
	 * for placeholders such as a plugin prefix like "prefix".
	 *
	 * @param replacer the new default {@link Replacer}.
	 *
	 * @return This {@link MessagesFile}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	MessagesFile setDefaultReplacer(@Nullable Replacer replacer);

	/**
	 * Creates a new {@link Replacer} to be used as the default {@link Replacer}
	 * for this {@link MessagesFile}. Keep in mind that the amount of {@code replacements}
	 * must be even as specified by {@link Replacer#Replacer(Object...)}
	 * 
	 * @param replacements The replacements to use for the new default {@link Replacer}.
	 * The format is <i>"str1", "obj1", "str2", "obj2"...</i>, so for example <i>"%test%", 1</i>
	 * would replace every occurrence of "%test%" with 1.
	 *
	 * @return This {@link MessagesFile}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	default MessagesFile setDefaultObjReplacer(@NotNull Object... replacements) {
		return setDefaultReplacer(new Replacer(replacements));
	}

	/**
	 * Working in a similar way to {@link #setDefaultObjReplacer(Object...)}, this method
	 * creates a new {@link Replacer} to be used as the default {@link Replacer} for this
	 * {@link MessagesFile}. With the difference being that replacements will be obtained
	 * with the {@link #getMessage(String)} method. This is useful if you have a file such
	 * as this one:
	 * <pre>
	 * prefix: "&6My&ePlugin &8|&7"
	 * test: "%prefix% Hello"
	 * test2: "%prefix World"
	 * </pre>
	 * And you want to apply the message at the {@code prefix} path to every message as
	 * the <i>%prefix%</i> placeholder. For that, you can use the parameters
	 * <i>"%prefix%", "prefix"</i> and the replacement for <i>"%prefix%"</i> will be
	 * "&6My&ePlugin &8|&7". Note that this is calculated on this method, so if you want
	 * to implement a messages file reload, you will need to redefine the default replacer.
	 *
	 * @param replacements The replacements to use for the new default {@link Replacer}.
	 *
	 * @return This {@link MessagesFile}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	default MessagesFile setDefaultMsgReplacer(@NotNull String... replacements) {
		final Replacer rep = new Replacer();
		if (replacements.length % 2 != 0)
			throw new IllegalArgumentException("Invalid Replacer size: " + replacements.length);
		for (int i = 0; i <= replacements.length - 1; i += 2) {
			final String msg = getRawMessage(replacements[i + 1]);
			rep.add(replacements[i], msg == null ? "null" : msg);
		}
		return setDefaultReplacer(rep);
	}

	/*
	 * Raw message getters
	 */

	/**
	 * Gets a message {@link String} from this {@link MessagesFile}.
	 * This {@link String} may be {@code null} if the message isn't found.
	 * <p>
	 * This method does <b>NOT</b> apply the {@link #getDefaultReplacer() default replacer}.
	 *
	 * @param path The path of the message to get.
	 *
	 * @return The {@link String} stored at the specified {@code path}.
	 * {@code null} if no {@link String} is stored on said {@code path} or
	 * if the {@code path} itself doesn't exist.
	 *
	 * @throws NullPointerException If {@code path} is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	String getRawMessage(@NotNull String path);

	/**
	 * Gets a message {@link String} from this {@link MessagesFile},
	 * applying a {@link Replacer} to it. This {@link String} may be
	 * {@code null} if the message isn't found.
	 * <p>
	 * This method does <b>NOT</b> apply the {@link #getDefaultReplacer() default replacer}.
	 *
	 * @param path The path of the message to get.
	 * @param replacer The {@link Replacer} to apply to the message. If
	 * no message is found and hence the {@link String} is {@code null},
	 * the {@link Replacer} won't be applied.
	 *
	 * @return The {@link String} stored at the specified {@code path},
	 * with the specified {@link Replacer} applied to it.
	 * {@code null} if no {@link String} is stored on said {@code path} or
	 * if the {@code path} itself doesn't exist.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	default String getRawMessage(@NotNull String path, @NotNull Replacer replacer) {
		final String str = getRawMessage(path);
		return str == null ? null : replacer.replaceAt(str);
	}

	/**
	 * Gets a message {@link String} from this {@link MessagesFile},
	 * applying a {@link Replacer} built from the specified {@code replacements} to it.
	 * This {@link String} may be {@code null} if the message isn't found.
	 * <p>
	 * This method does <b>NOT</b> apply the {@link #getDefaultReplacer() default replacer}.
	 *
	 * @param path The path of the message to get.
	 * @param replacements The {@link Object Objects} that will be used in
	 * order to build a new {@link Replacer} that will later be applied to
	 * the {@link String} found at the specified {@code path}. If said
	 * {@link String} is {@code null}, no {@link Replacer} will be created in
	 * order to save a tiny bit of resources. Keep in mind that the amount
	 * of {@code replacements} must be even as specified on the {@link Replacer}
	 * {@link Replacer#Replacer(Object...) constructor}.
	 *
	 * @return The {@link String} stored at the specified {@code path},
	 * with the specified {@code replacements} applied to it.
	 * {@code null} if no {@link String} is stored on said {@code path} or
	 * if the {@code path} itself doesn't exist.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	default String getRawMessage(@NotNull String path, @NotNull Object... replacements) {
		return getRawMessage(path, new Replacer(replacements));
	}

	/*
	 * Message getters
	 */

	/**
	 * Gets a message {@link String} from this {@link MessagesFile}.
	 * This {@link String} may be {@code null} if the message isn't found.
	 * <br>
	 * This method will {@link SkyStrings#applyColor(String) apply} color patterns.
	 * If you don't want this, you can use {@link #getRawMessage(String)} to get
	 * the unmodified stored {@link String} as is, without any processing.
	 *
	 * @param path The path of the message to get.
	 *
	 * @return The {@link String} stored at the specified {@code path}.
	 * {@code null} if no {@link String} is stored on said {@code path} or
	 * if the {@code path} itself doesn't exist.
	 *
	 * @throws NullPointerException If {@code path} is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	default String getMessage(@NotNull String path) {
		final String str = getRawMessage(path);
		final Replacer rep = this.getDefaultReplacer();
		return str == null ? null : SkyStrings.applyColor(rep == null ? str : rep.replaceAt(str));
	}

	/**
	 * Gets a message {@link String} from this {@link MessagesFile},
	 * applying a {@link Replacer} to it. This {@link String} may be
	 * {@code null} if the message isn't found.
	 * <br>
	 * This method will {@link SkyStrings#applyColor(String) apply} color patterns.
	 * If you don't want this, you can use {@link #getRawMessage(String, Replacer)} to get
	 * the unmodified stored {@link String} as is, without any processing.
	 *
	 * @param path The path of the message to get.
	 * @param replacer The {@link Replacer} to apply to the message. If
	 * no message is found and hence the {@link String} is {@code null},
	 * the {@link Replacer} won't be applied.
	 *
	 * @return The {@link String} stored at the specified {@code path},
	 * with the specified {@link Replacer} applied to it.
	 * {@code null} if no {@link String} is stored on said {@code path} or
	 * if the {@code path} itself doesn't exist.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	default String getMessage(@NotNull String path, @NotNull Replacer replacer) {
		final String str = getMessage(path);
		return str == null ? null : replacer.replaceAt(str);
	}

	/**
	 * Gets a message {@link String} from this {@link MessagesFile},
	 * applying a {@link Replacer} built from the specified {@code replacements} to it.
	 * This {@link String} may be {@code null} if the message isn't found.
	 * <br>
	 * This method will {@link SkyStrings#applyColor(String) apply} color patterns.
	 * If you don't want this, you can use {@link #getRawMessage(String, Object...)} to get
	 * the unmodified stored {@link String} as is, without any processing.
	 *
	 * @param path The path of the message to get.
	 * @param replacements The {@link Object Objects} that will be used in
	 * order to build a new {@link Replacer} that will later be applied to
	 * the {@link String} found at the specified {@code path}. If said
	 * {@link String} is {@code null}, no {@link Replacer} will be created in
	 * order to save a tiny bit of resources. Keep in mind that the amount
	 * of {@code replacements} must be even as specified on the {@link Replacer}
	 * {@link Replacer#Replacer(Object...) constructor}.
	 *
	 * @return The {@link String} stored at the specified {@code path},
	 * with the specified {@code replacements} applied to it.
	 * {@code null} if no {@link String} is stored on said {@code path} or
	 * if the {@code path} itself doesn't exist.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	default String getMessage(@NotNull String path, @NotNull Object... replacements) {
		final String message = getMessage(path);
		return message == null ? null : new Replacer(replacements).replaceAt(message);
	}

	/*
	 * Message senders
	 */

	private boolean processMessage(@NotNull MessageReceiver target, @Nullable String message) {
		if (message != null && !message.isBlank())
			SkyStrings.sendMessage(target, message);
		return true;
	}

	/**
	 * Sends the message located at the specified {@code path} to the provided
	 * {@code target}. If said message doesn't exist, or is blank ({@link String#isBlank()}),
	 * then no message will be sent to the {@code target}.
	 * <p>
	 * This method purely relies on the behaviour of {@link #getMessage(String)}.
	 *
	 * @param target the {@link MessageReceiver} that will receive the message.
	 * @param path the path at where to obtain the message, read {@link #getMessage(String)}
	 * for more details.
	 *
	 * @return Always {@code true}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean send(@NotNull MessageReceiver target, @NotNull String path) {
		return processMessage(target, getMessage(path));
	}

	/**
	 * Sends the message located at the specified {@code path} to the provided
	 * {@code target}, applying a {@link Replacer} before sending it.
	 * If said message doesn't exist, or is blank ({@link String#isBlank()}),
	 * then no message will be sent to the {@code target}.
	 * <p>
	 * This method purely relies on the behaviour of {@link #getMessage(String, Replacer)}.
	 *
	 * @param target the {@link MessageReceiver} that will receive the message.
	 * @param path the path at where to obtain the message, read {@link #getMessage(String, Replacer)}
	 * for more details.
	 * @param replacer The {@link Replacer} to apply to the message. If
	 * no message is found, the {@link Replacer} won't be applied.
	 *
	 * @return Always {@code true}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean send(@NotNull MessageReceiver target, @NotNull String path, @NotNull Replacer replacer) {
		return processMessage(target, getMessage(path, replacer));
	}

	/**
	 * Sends the message located at the specified {@code path} to the provided
	 * {@code target}, applying a {@link Replacer} before sending it.
	 * If said message doesn't exist, or is blank ({@link String#isBlank()}),
	 * then no message will be sent to the {@code target}.
	 * <p>
	 * This method purely relies on the behaviour of {@link #getMessage(String, Object...)}.
	 *
	 * @param target the {@link MessageReceiver} that will receive the message.
	 * @param path the path at where to obtain the message, read {@link #getMessage(String, Object...)}
	 * for more details.
	 * @param replacements The {@link Object Objects} that will be used in
	 * order to build a new {@link Replacer} that will later be applied to
	 * the message found at the specified {@code path}. If said
	 * {@link String} is {@code null}, no {@link Replacer} will be created in
	 * order to save a tiny bit of resources. Keep in mind that the amount
	 * of {@code replacements} must be even as specified on the {@link Replacer}
	 * {@link Replacer#Replacer(Object...) constructor}.
	 *
	 * @return Always {@code true}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean send(@NotNull MessageReceiver target, @NotNull String path, @NotNull Object... replacements) {
		return processMessage(target, getMessage(path, replacements));
	}
}