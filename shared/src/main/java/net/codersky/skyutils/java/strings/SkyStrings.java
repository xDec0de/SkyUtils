package net.codersky.skyutils.java.strings;

import net.codersky.jsky.strings.JStrings;
import net.codersky.skyutils.cmd.SkyCommand;
import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.java.strings.pattern.ColorPattern;
import net.codersky.skyutils.java.strings.pattern.TargetPattern;
import net.codersky.skyutils.java.strings.pattern.color.GradientColorPattern;
import net.codersky.skyutils.java.strings.pattern.color.HexColorPattern;
import net.codersky.skyutils.java.strings.pattern.target.ActionBarTargetPattern;
import net.codersky.skyutils.java.strings.pattern.target.ConsoleTargetPattern;
import net.codersky.skyutils.java.strings.pattern.target.PlayerTargetPattern;
import net.codersky.skyutils.java.strings.pattern.target.SoundTargetPattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class SkyStrings extends JStrings {

	/** The color character used for Minecraft color codes. */
	public static char COLOR_CHAR = '§';

	protected static List<ColorPattern> colorPatterns;
	protected static List<TargetPattern> targetPatterns;

	static {
		colorPatterns = List.of(
				new GradientColorPattern(),
				new HexColorPattern(),
				(str, simple) -> applyColorChar('&', str)
		);
		targetPatterns = List.of(
				new ActionBarTargetPattern(),
				new ConsoleTargetPattern(),
				new PlayerTargetPattern(),
				new SoundTargetPattern()
		);
	}

	/**
	 * Applies all known patterns to the provided {@link String} in order to send it
	 * to the specified {@code target}. That is, all color patterns, target patterns
	 * and event patterns, in that order.
	 *
	 * @param target the {@link MessageReceiver} that will receive the message.
	 * @param str the {@link String} to process.
	 *
	 * @return Always {@code true} to make it easier to create {@link SkyCommand MCCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	public static boolean sendMessage(@NotNull MessageReceiver target, @NotNull String str) {
		return target.sendMessage(applyEventPatterns(applyTargetPatterns(target, applyColor(str), true)));
	}

	/*
	 * Color patterns
	 */

	/**
	 * Applies all {@link ColorPattern color patterns} to the provided {@code string}
	 *
	 * @param str the {@link String} to apply colors to.
	 * @param simple whether to use simple mode or not, read
	 * {@link ColorPattern#applyColor(String, boolean)} for more information.
	 *
	 * @throws NullPointerException if {@code str} is {@code null}.
	 *
	 * @return A new {@link String} with all {@link ColorPattern color patterns}
	 * applied to it.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public static String applyColor(@NotNull String str, boolean simple) {
		String colored = Objects.requireNonNull(str, "The string to process cannot be null");
		for (ColorPattern pattern : colorPatterns)
			colored = pattern.applyColor(colored, simple);
		return colored;
	}

	/**
	 * Applies all {@link ColorPattern color patterns} to the provided {@code string}
	 * <p>
	 * Simple mode is enabled on this method, read {@link ColorPattern#applyColor(String, boolean)}
	 * for more information.
	 *
	 * @param str the {@link String} to apply colors to.
	 *
	 * @throws NullPointerException if {@code str} is {@code null}.
	 *
	 * @return A new {@link String} with all {@link ColorPattern color patterns}
	 * applied to it.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public static String applyColor(@NotNull String str) {
		return applyColor(str, true);
	}

	/*
	 * Color utilities
	 */

	/**
	 * Replaces every occurrence of <b>ch</b> with {@link SkyStrings#COLOR_CHAR} if
	 * followed by a valid color character ({@link #isColorChar(char)}).
	 *
	 * @param ch the character to replace, normally '&', as this is the standard.
	 * @param str the {@link CharSequence} to apply color characters to.
	 *
	 * @return A {@link String} from the specified {@link CharSequence} with translated color characters.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public static String applyColorChar(char ch, @NotNull CharSequence str) {
		final int length = str.length() - 1;
		final char[] arr = str.toString().toCharArray();
		for (int i = 0; i < length; i++)
			if (str.charAt(i) == ch && isColorChar(str.charAt(i + 1)))
				arr[i++] = COLOR_CHAR;
		return new String(arr);
	}

	/**
	 * Strips all <b>vanilla</b> chat formatting from the specified {@link CharSequence}.
	 * that is, color and text formatting, for example, assuming that
	 * {@code colorChar} is '&', this method will remove all occurrences
	 * of &[a-f], &[0-9], &[k-o] and &r, leaving the string as an uncolored,
	 * unformatted, simple string, doing the same with {@link SkyStrings#COLOR_CHAR}
	 *
	 * @param sequence the {@link CharSequence} to strip colors from.
	 * @param colorChar an additional color character to use besides {@link SkyStrings#COLOR_CHAR},
	 * generally '&'.
	 *
	 * @return A new {@code String} with all the contents of the specified char
	 * {@code sequence} except valid chat colors.
	 *
	 * @throws NullPointerException if {@code sequence} is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public static String stripColor(@NotNull CharSequence sequence, char colorChar) {
		final int length = sequence.length();
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char ch = sequence.charAt(i);
			if ((ch == colorChar || ch == COLOR_CHAR) && (i + 1 < length) && isColorChar(sequence.charAt(i + 1)))
				i++;
			else
				result.append(ch);
		}
		return result.toString();
	}

	/**
	 * A simple convenience method that checks if {@code c} is a character
	 * that can be used to apply color or formatting to a string, that is,
	 * r, R, x, X, or a character between these ranges: [a-f], [A-F], [k-o], [K-O] and [0-9].
	 *
	 * @param c the character to check.
	 *
	 * @return {@code true} if the character is a color character, {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 */
	public static boolean isColorChar(char c) {
		final char ch = Character.toLowerCase(c);
		return (ch == 'r' || ch == 'x' || (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'k' && ch <= 'o'));
	}

	/*
	 - Component utilities
	 */

	/**
	 * Gets the {@link LegacyComponentSerializer} used to serialize and deserialize
	 * {@link String Strings} and {@link Component Components}. The {@link LegacyComponentSerializer}
	 * is used for backwards compatibility and due to how colors are {@link #applyColor(String) applied}
	 * by SkyUtils, as the legacy format is used on all {@link ColorPattern color patterns}.
	 *
	 * @return The {@link LegacyComponentSerializer} used to serialize and deserialize
	 * {@link String Strings} and {@link Component Components}.
	 *
	 * @see #toComponent(String)
	 * @see #applyColorComponent(String, boolean)
	 * @see #applyEventPatterns(String)
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public static LegacyComponentSerializer getLegacyComponentSerializer() {
		return LegacyComponentSerializer.builder().useUnusualXRepeatedCharacterHexFormat().build();
	}

	/**
	 * Converts {@code str} to a {@link Component} by deserializing it
	 * with the {@link LegacyComponentSerializer} provided by {@link #getLegacyComponentSerializer()}.
	 *
	 * @param str The {@link String} to convert to a {@link Component}.
	 *
	 * @return {@code str} converted to a {@link Component}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public static Component toComponent(@NotNull String str) {
		return getLegacyComponentSerializer().deserialize(str);
	}

	/**
	 * Converts {@code str} to a {@link Component} by deserializing it
	 * with the {@link LegacyComponentSerializer} provided by {@link #getLegacyComponentSerializer()}.
	 * <p>
	 * {@link #applyColor(String, boolean)} is applied to {@code str} before deserializing it.
	 *
	 * @param str The {@link String} to color and convert to a {@link Component}.
	 * @param simple Whether to use simple mode on {@link ColorPattern color patterns}
	 * or not. Read {@link ColorPattern#applyColor(String, boolean)} for more information.
	 *
	 * @return {@code str} converted to a colored {@link Component}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public static Component applyColorComponent(@NotNull String str, boolean simple) {
		return toComponent(applyColor(str, simple));
	}

	/**
	 * Converts {@code str} to a {@link Component} by deserializing it
	 * with the {@link LegacyComponentSerializer} provided by {@link #getLegacyComponentSerializer()}.
	 * <p>
	 * {@link #applyColor(String, boolean)} is applied to {@code str} before deserializing it.
	 * Simple mode is set to {@code true} on this method. Read {@link ColorPattern#applyColor(String, boolean)}
	 * for more information about simple mode on {@link ColorPattern color patterns}.
	 *
	 * @param str The {@link String} to color and convert to a {@link Component}.
	 *
	 * @return {@code str} converted to a colored {@link Component}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public static Component applyColorComponent(@NotNull String str) {
		return applyColorComponent(str, true);
	}

	/**
	 * Converts the provided {@link Component} back to a legacy colored {@link String}
	 * by serializing it with the {@link LegacyComponentSerializer} provided by
	 * {@link #getLegacyComponentSerializer()}.
	 *
	 * @param component The {@link Component} to convert to a {@link String}.
	 *
	 * @return The legacy colored {@link String} obtained by serializing
	 * the provided {@link Component}.
	 *
	 * @since JSky 1.0.0
	 */
	@NotNull
	public String fromComponent(@NotNull Component component) {
		return getLegacyComponentSerializer().serialize(component);
	}

	/*
	 - Target patterns
	 */

	/**
	 * Applies all {@link TargetPattern target patterns} to the provided {@code string}
	 *
	 * @param target The {@link MessageReceiver} that will receive any matching message.
	 * @param str the {@link String} to process.
	 * @param applyEventPatterns Whether to {@link SkyStrings#applyEventPatterns(String) apply} event patterns
	 * to the content matched by {@link TargetPattern target patterns}. Details about this can be found
	 * {@link TargetPattern here}, under the "<b>ABOUT EVENT PATTERNS</b>" section.
	 *
	 * @throws NullPointerException if {@code string} is {@code null}.
	 *
	 * @return A new {@link String} with all {@link TargetPattern target patterns}
	 * removed from it.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public static String applyTargetPatterns(@NotNull MessageReceiver target, @NotNull String str, boolean applyEventPatterns) {
		String result = Objects.requireNonNull(str, "The string to process cannot be null");
		for (TargetPattern pattern : targetPatterns)
			result = pattern.process(target, result, applyEventPatterns);
		return result;
	}

	/*
	 * Event patterns
	 */

	/**
	 * Removes all event patterns from the provided {@code str}.
	 * This can be used to control user input when necessary, as event
	 * patterns can be used in malicious ways, such as making other
	 * users execute dangerous commands.
	 *
	 * @param str The {@link String} to remove event patterns from.
	 *
	 * @return A new {@link String} with all event patterns removed from it.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public static String stripEventPatterns(@NotNull String str) {
		final StringBuilder builder = new StringBuilder();
		searchEventPatterns(str,
				builder::append,
				(event, txt) -> builder.append(txt));
		return builder.toString();
	}

	/**
	 * Applies event patterns to the provided {@code str}.
	 * The {@link #getLegacyComponentSerializer() LegacyComponentSerializer} is
	 * used to convert {@code str} to a {@link Component}.
	 *
	 * @param str The {@link String} to apply event patterns to.
	 *
	 * @return A {@link Component} with all event patterns applied to it.
	 * This {@link Component} can then be sent to any {@link MessageReceiver}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see MessageReceiver
	 */
	@NotNull
	public static Component applyEventPatterns(@NotNull String str) {
		final LegacyComponentSerializer serializer = getLegacyComponentSerializer();
		final TextComponent.Builder builder = Component.text();
		searchEventPatterns(str,
				txt -> builder.append(serializer.deserialize(txt)),
				(event, txt) -> applyEvents(builder, event, txt));
		return builder.build();
	}

	private static void applyEvents(TextComponent.Builder builder, String eventData, String text) {
		final List<String> eventList = splitEvents(eventData);
		final int safeLen = eventList.size() - 1;
		final Component toAppend = Component.text(text);
		for (int i = 0; i < safeLen; i += 2) {
			final String content = eventList.get(i + 1);
			switch (eventList.get(i).toLowerCase()) {
				case "text", "show_text" -> toAppend.hoverEvent(HoverEvent.showText(Component.text(content)));
				case "url", "open_url" -> toAppend.clickEvent(ClickEvent.openUrl(content));
				case "file", "open_file" -> toAppend.clickEvent(ClickEvent.openFile(content));
				case "run", "run_cmd", "run_command" -> toAppend.clickEvent(ClickEvent.runCommand(content));
				case "suggest", "suggest_cmd", "suggest_command" -> toAppend.clickEvent(ClickEvent.suggestCommand(content));
				case "copy", "copy_to_clipboard" -> toAppend.clickEvent(ClickEvent.copyToClipboard(content));
			};
		}
		builder.append(toAppend);
	}

	// Utility method to split events ignoring string literals, for example
	// text;"x;y;z" will be split as ["text", "x;y;z"]
	private static List<String> splitEvents(String eventData) {
		final List<String> eventList = new ArrayList<>();
		boolean literal = false;
		StringBuilder current = new StringBuilder();
		for (int i = 0; i < eventData.length(); i++) {
			final char ch = eventData.charAt(i);
			if (ch == '"')
				literal = !literal;
			else if (ch == ';' && !literal) {
				eventList.add(current.toString());
				current = new StringBuilder();
			} else
				current.append(ch);
		}
		if (!current.isEmpty())
			eventList.add(current.toString());
		return eventList;
	}

	// Search utility //

	private static void searchEventPatterns(String str, Consumer<String> append, BiConsumer<String, String> replace) {
		final int first = str.indexOf('<');
		if (first == -1) {
			append.accept(str);
			return;
		}
		int lastAppend = 0;
		for (int start = first; start != -1; start = str.indexOf('<', start + 1)) {
			final int eventEnd = str.indexOf('>', start);
			if (eventEnd == -1)
				continue;
			final int textEnd = str.indexOf("\\>", eventEnd);
			if (textEnd == -1)
				continue;
			append.accept(str.substring(lastAppend, start));
			replace.accept(str.substring(start + 1, eventEnd), str.substring(eventEnd + 1, textEnd));
			lastAppend = textEnd + 2;
		}
		if (lastAppend != str.length())
			append.accept(str.substring(lastAppend));
	}
}
