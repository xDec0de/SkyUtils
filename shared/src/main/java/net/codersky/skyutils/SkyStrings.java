package net.codersky.skyutils;

import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.JStrings;
import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParseAllResult;
import net.codersky.jsky.strings.tag.JTagParser;
import net.codersky.skyutils.crossplatform.message.simplecolor.LegacySimpleColor;
import net.codersky.skyutils.crossplatform.message.simplecolor.SimpleColor;
import net.codersky.skyutils.crossplatform.message.tag.MessageTag;
import net.codersky.skyutils.crossplatform.message.tag.MessageTagProvider;
import net.codersky.skyutils.crossplatform.message.tag.color.GradientMessageTag;
import net.codersky.skyutils.crossplatform.message.tag.color.GradientMessageTagResult;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.List;

public class SkyStrings extends JStrings {

	/** The pattern character used for Minecraft pattern codes. */
	public static char COLOR_CHAR = '§';
	private static final List<SimpleColor> colorPatterns = JCollections.asArrayList(
			LegacySimpleColor.INSTANCE
	);

	private SkyStrings() {}

	/*
	 - Strip pattern
	 */

	@NotNull
	public static String stripColor(@NotNull final CharSequence sequence, final char colorChar) {
		return LegacySimpleColor.INSTANCE.strip(sequence, colorChar);
	}

	/*
	 - Color char
	 */

	/**
	 * A simple convenience method that checks if {@code c} is a character
	 * that can be used to apply pattern or formatting to a string, that is,
	 * r, R, x, X, or a character between these ranges: [a-f], [A-F], [k-o], [K-O] and [0-9].
	 *
	 * @param c the character to check.
	 *
	 * @return {@code true} if the character is a pattern character, {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 */
	public static boolean isColorChar(char c) {
		final char ch = Character.toLowerCase(c);
		return (ch == 'r' || ch == 'x' || (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'k' && ch <= 'o'));
	}

	@NotNull
	public static String applyColorChar(final char ch, @NotNull final String str) {
		return LegacySimpleColor.INSTANCE.apply(ch, str);
	}

	/*
	 - toLegacyHex
	 */

	@NotNull
	public static String toLegacyHex(@NotNull final Color color) {
		final String hex = String.format("%06X", color.getRGB() & 0xFFFFFF);
		final StringBuilder builder = new StringBuilder(14);
		builder.append(COLOR_CHAR).append('x');
		for (char c : hex.toCharArray())
			builder.append(COLOR_CHAR).append(c);
		return builder.toString();
	}

	@NotNull
	public static String[] toLegacyHex(@NotNull final Color[] colors) {
		return JCollections.map(colors, new String[colors.length], SkyStrings::toLegacyHex);
	}

	/*
	 - Apply pattern - String
	 */

	@NotNull
	public static String applyColor(@NotNull String str) {
		if (str.indexOf('<') == -1 || str.indexOf('>') == -1)
			return applySimpleColors(str);
		final StringBuilder result = new StringBuilder();
		for (final Object obj : JTagParser.parseAll(str, 0, 0)) {
			if (obj instanceof final JTag tag)
				appendTag(result, tag);
			else // String
				result.append(applySimpleColors(obj.toString()));
		}
		return result.toString();
	}

	private static String applySimpleColors(String str) {
		String result = str;
		for (final SimpleColor pattern : colorPatterns)
			result = pattern.apply(result);
		return result;
	}

	private static void appendTag(StringBuilder builder, JTag tag) {
		final MessageTag msgTag = MessageTagProvider.getTag(tag);
		if (msgTag instanceof final GradientMessageTag gradient)
			appendGradientTag(builder, gradient, tag.getContent());
		else {
			final JTagParseAllResult parseResult = JTagParser.parseAll(tag.getContent(), 0, 0);
			builder.append('<').append(tag.getName()).append(':');
			for (final Object obj : parseResult) {
				if (obj instanceof final JTag child)
					appendTag(builder, child);
				else
					builder.append(applySimpleColors(obj.toString()));
			}
			builder.append('>');
		}
	}

	/*
	 - Append - GradientMessageTag
	 */

	// TODO: Ugly method. I don't like it, but I'm tired... Should be improved though

	private static void appendGradientTag(StringBuilder builder, GradientMessageTag tag, String content) {
		final JTagParseAllResult parseResult = JTagParser.parseAll(content);
		final GradientMessageTagResult res = getGradientResult(tag, parseResult);
		final String[] colors = toLegacyHex(res.colors());
		int lastColor = 0;
		boolean jumped = false;
		boolean close = false;
		for (final Object obj : parseResult) {
			String str = null;
			if (obj instanceof final JTag child) {
				builder.append('<').append(child.getName()).append(':');
				if (MessageTagProvider.isMainEventTag(child)) {
					for (final JTag eventChild : child.getChildren())
						appendTag(builder, eventChild);
					str = child.getContent();
					close = true;
				} else
					appendTag(builder, child);
			} if (obj instanceof String)
				str = (String) obj;
			int start = 0;
			if (!jumped) {
				start = res.start();
				jumped = true;
			}
			if (str == null)
				continue;
			for (int i = start; i < str.length(); i++, lastColor++)
				builder.append(colors[lastColor]).append(str.charAt(i));
			if (close)
				builder.append('>');
		}
	}

	private static GradientMessageTagResult getGradientResult(GradientMessageTag tag, JTagParseAllResult parseResult) {
		final StringBuilder extracted = new StringBuilder();
		for (final Object obj : parseResult) {
			if (obj instanceof final String str)
				extracted.append(str);
			if (obj instanceof final JTag child && MessageTagProvider.isMainEventTag(child))
				extracted.append(child.getContent());
		}
		return tag.getColors(extracted.toString());
	}

	/*public static void main(String[] args) {
		System.out.println(applyColor("<g:FF0:000:Hi <e:<txt:Hi>world>>"));
	}*/
}
