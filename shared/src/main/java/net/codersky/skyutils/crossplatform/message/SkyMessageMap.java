package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.Reloadable;
import net.codersky.jsky.storage.DataProvider;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A SkyMessageMap holds a {@link HashMap map} of {@link SkyMessage messages},
 * fetching raw message data from a provider. A provider must:
 * <ul>
 *     <li>Extend the {@link DataProvider} {@code class}.</li>
 *     <li>Implement the {@link Reloadable} {@code interface}.</li>
 * </ul>
 * The main purpose of this {@code class} is to "compile" raw message
 * data from a provider into {@link SkyMessage}s. This leads to performance
 * improvements when sending messages from any provider, as messages are
 * already cached, compiled and ready to send.
 * <p>
 * <h2>Providers included by SkyUtils:</h2>
 * <ul>
 *     <li>{@link net.codersky.jsky.yaml.YamlFile YamlFile}</li>
 * </ul>
 *
 * @since SkyUtils 1.0.0
 *
 * @see #SkyMessageMap(DataProvider)
 */
public class SkyMessageMap implements Reloadable {

	private final Object provider;
	private final SkyMessage fallback;
	private final HashMap<String, SkyMessage> messageCache = new HashMap<>();

	/**
	 * Creates a new {@link SkyMessageMap} based that will fetch raw
	 * message data from the specified {@code provider}. Remember to
	 * {@link #reload(boolean) reload} the map in order to cache messages!
	 *
	 * @param provider The raw message {@link P provider} to use. Read
	 * {@link SkyMessageMap this} if you don't know what provider to use.
	 * @param fallback The fallback {@link SkyMessage} to send when sending
	 * a message that isn't found. Can be {@code null}, see {@link #getFallback()}.
	 *
	 * @param <P> Must extend {@link DataProvider} and implement {@link Reloadable}.
	 *
	 * @see #reload(boolean)
	 * @see #get(String)
	 */
	public <P extends DataProvider & Reloadable> SkyMessageMap(final @NotNull P provider, @Nullable final SkyMessage fallback) {
		this.provider = Objects.requireNonNull(provider);
		this.fallback = fallback;
	}

	/**
	 * Creates a new {@link SkyMessageMap} based that will fetch raw
	 * message data from the specified {@code provider}. Remember to
	 * {@link #reload(boolean) reload} the map in order to cache messages!
	 *
	 * @param provider The raw message {@link P provider} to use. Read
	 * {@link SkyMessageMap this} if you don't know what provider to use.
	 *
	 * @param <P> Must extend {@link DataProvider} and implement {@link Reloadable}.
	 *
	 * @see #reload(boolean)
	 * @see #get(String)
	 */
	public <P extends DataProvider & Reloadable> SkyMessageMap(final @NotNull P provider) {
		this(provider, null);
	}

	/*
	 - Internal provider getters
	 */

	@NotNull
	private DataProvider dataProvider() {
		return (DataProvider) provider;
	}

	@NotNull
	private Reloadable reloadable() {
		return (Reloadable) provider;
	}

	/*
	 - Reload
	 */

	/**
	 * Reloads this {@link SkyMessageMap}, optionally clearing
	 * its raw message provider to free some memory.
	 *
	 * @param clearProvider Whether to clear the raw message
	 * provider of this {@link SkyMessageMap} or not. If you
	 * are not using the provider for anything else than providing
	 * message data to this map, you might as well set this
	 * to {@code true} to save up some memory.
	 *
	 * @return {@code true} if the raw message provider of this
	 * {@link SkyMessageMap} reloads successfully, {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #reload()
	 */
	public boolean reload(boolean clearProvider) {
		if (!reloadable().reload())
			return false;
		messageCache.clear();
		final DataProvider provider = dataProvider();
		for (Map.Entry<String, Object> entry : provider.getEntries())
			if (entry.getValue() instanceof final String raw)
				messageCache.put(entry.getKey(), SkyMessage.of(raw));
		if (clearProvider)
			provider.getEntries().clear();
		return true;
	}

	/**
	 * Reloads this {@link SkyMessageMap}. By default, this method
	 * will <b>clear</b> the raw message provider of the map to reduce
	 * memory usage, as it is assumed that you are using this {@link SkyMessageMap}
	 * as a wrapper of the provider. If you are using the raw message
	 * provider for something else and don't want it to be cleared,
	 * then you can use {@link #reload(boolean)} and set
	 * {@code clearProvider} to {@code false}.
	 *
	 * @return {@code true} if the raw message provider of this
	 * {@link SkyMessageMap} reloads successfully, {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #reload(boolean)
	 */
	@Override
	public boolean reload() {
		return reload(true);
	}

	/*
	 - Message getters
	 */

	/**
	 * Gets the {@link SkyMessage} present at the provided {@code key}.
	 * Keep in mind that you need to {@link #reload(boolean) reload}
	 * at least once after instantiating the map to cache messages.
	 *
	 * @param key The key of the message to obtain. This key will be the
	 * exact same as it was on the raw message provider of this map.
	 *
	 * @return The {@link SkyMessage} present at the provided {@code key},
	 * {@code null} if no cached message has said {@code key}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	public SkyMessage get(@NotNull final String key) {
		return messageCache.get(key);
	}

	/**
	 * Gets the {@link SkyMessage} present at the provided {@code key}.
	 * Keep in mind that you need to {@link #reload(boolean) reload}
	 * at least once after instantiating the map to cache messages.
	 *
	 * @param key The key of the message to obtain. This key will be the
	 * exact same as it was on the raw message provider of this map.
	 * @param replacer The {@link SkyReplacer} to apply to the obtained
	 * message, if any.
	 *
	 * @return The {@link SkyMessage} present at the provided {@code key},
	 * {@code null} if no cached message has said {@code key}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	public SkyMessage get(@NotNull final String key, @NotNull final SkyReplacer replacer) {
		final SkyMessage msg = get(key);
		return msg == null ? null : replacer.replaceAt(msg);
	}

	/**
	 * Gets the {@link SkyMessage} present at the provided {@code key}.
	 * Keep in mind that you need to {@link #reload(boolean) reload}
	 * at least once after instantiating the map to cache messages.
	 *
	 * @param key The key of the message to obtain. This key will be the
	 * exact same as it was on the raw message provider of this map.
	 * @param replacements The objects used to build a new
	 * {@link SkyReplacer}, only if a message is found. This
	 * {@link SkyReplacer} will then be applied to the message.
	 *
	 * @return The {@link SkyMessage} present at the provided {@code key},
	 * {@code null} if no cached message has said {@code key}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	public SkyMessage get(@NotNull final String key, @NotNull final Object @NotNull ... replacements) {
		final SkyMessage msg = get(key);
		return msg == null ? null : new SkyReplacer(replacements).replaceAt(msg);
	}

	/*
	 - Message senders - Fallback
	 */

	/**
	 * Gets the fallback {@link SkyMessage} to use when any of the
	 * {@link #send(MessageReceiver, String) send} methods isn't
	 * able to get the requested message. If {@code null}, said
	 * methods will just ignore the fallback and send no message.
	 *
	 * @return The fallback {@link SkyMessage}.
	 *
	 * @since SkyUtils 1.0.0
	 * 
	 * @see #send(MessageReceiver, String)
	 */
	@Nullable
	public SkyMessage getFallback() {
		return fallback;
	}

	/*
	 - Message senders
	 */

	private boolean send(final MessageReceiver receiver, Supplier<SkyMessage> getter) {
		SkyMessage msg = getter.get();
		if (msg == null)
			msg = getFallback();
		return msg == null || msg.send(receiver);
	}

	/**
	 * Sends the {@link SkyMessage} present at the provided {@code key} to the
	 * specified {@code receiver}. If no message is found, {@link #getFallback()}
	 * will be used. If still, {@link #getFallback()} is {@code null}, no message
	 * will be sent.
	 *
	 * @param receiver The {@link MessageReceiver} that will receive the message.
	 * @param key The key of the message to get, see {@link #get(String)}.
	 *
	 * @return Always {@code true}. Use {@link #has(String)} to check if a
	 * message exists.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #has(String)
	 * @see #get(String)
	 * @see #send(MessageReceiver, String, SkyReplacer)
	 * @see #send(MessageReceiver, String, Object...)
	 */
	public boolean send(@NotNull final MessageReceiver receiver, @NotNull final String key) {
		return send(receiver, () -> get(key));
	}

	/**
	 * Sends the {@link SkyMessage} present at the provided {@code key} to the
	 * specified {@code receiver}. If no message is found, {@link #getFallback()}
	 * will be used. If still, {@link #getFallback()} is {@code null}, no message
	 * will be sent.
	 *
	 * @param receiver The {@link MessageReceiver} that will receive the message.
	 * @param key The key of the message to get, see {@link #get(String)}.
	 * @param replacer The {@link SkyReplacer} to apply to the obtained message.
	 * This won't be applied to the {@link #getFallback() fallback}, only to the
	 * requested message to send, if found.
	 *
	 * @return Always {@code true}. Use {@link #has(String)} to check if a
	 * message exists.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #has(String)
	 * @see #get(String, SkyReplacer)
	 * @see #send(MessageReceiver, String)
	 * @see #send(MessageReceiver, String, Object...)
	 */
	public boolean send(@NotNull final MessageReceiver receiver, @NotNull final String key, @NotNull final SkyReplacer replacer) {
		return send(receiver, () -> get(key, replacer));
	}

	/**
	 * Sends the {@link SkyMessage} present at the provided {@code key} to the
	 * specified {@code receiver}. If no message is found, {@link #getFallback()}
	 * will be used. If still, {@link #getFallback()} is {@code null}, no message
	 * will be sent.
	 *
	 * @param receiver The {@link MessageReceiver} that will receive the message.
	 * @param key The key of the message to get, see {@link #get(String)}.
	 * @param replacements The objects used to create a new {@link SkyReplacer}
	 * to apply to the obtained message. This won't be applied to the
	 * {@link #getFallback() fallback}, only to the requested message to send,
	 * if found. Note that if the message isn't found, the {@link SkyReplacer}
	 * won't be created.
	 *
	 * @return Always {@code true}. Use {@link #has(String)} to check if a
	 * message exists.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #has(String)
	 * @see #get(String, Object...)
	 * @see #send(MessageReceiver, String)
	 * @see #send(MessageReceiver, String, Object...)
	 */
	public boolean send(@NotNull final MessageReceiver receiver, @NotNull final String key, @NotNull final Object... replacements) {
		return send(receiver, () -> get(key, replacements));
	}

	/*
	 - Utility
	 */

	/**
	 * Checks if this {@link SkyMessageMap} has <b>cached</b> a
	 * message with the provided {@code key}. Remember that in
	 * order to cache messages you need to {@link #reload() reload}
	 * at least once.
	 *
	 * @param key The key to search for.
	 *
	 * @return {@code true} if a message with that {@code key} is
	 * found, {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #get(String)
	 */
	public boolean has(@NotNull final String key) {
		return messageCache.containsKey(key);
	}
}
