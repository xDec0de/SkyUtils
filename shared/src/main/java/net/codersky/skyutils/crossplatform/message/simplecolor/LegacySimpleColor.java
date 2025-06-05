package net.codersky.skyutils.crossplatform.message.simplecolor;

import net.codersky.skyutils.java.strings.TempSkyStrings;
import org.jetbrains.annotations.NotNull;

public class LegacySimpleColor implements SimpleColor {

	public final static LegacySimpleColor INSTANCE = new LegacySimpleColor();

	private LegacySimpleColor() {}

	@NotNull
	public String apply(final char ch, @NotNull final String str) {
		if (str.indexOf(ch) == -1)
			return str;
		final int length = str.length() - 1;
		final char[] arr = str.toCharArray();
		for (int i = 0; i < length; i++)
			if (arr[i] == ch && TempSkyStrings.isColorChar(arr[i + 1]))
				arr[i++] = TempSkyStrings.COLOR_CHAR;
		return new String(arr);
	}

	@NotNull
	@Override
	public String apply(@NotNull final String str) {
		return apply('&', str);
	}

	@NotNull
	public String strip(@NotNull final CharSequence sequence, final char colorChar) {
		final int len = sequence.length();
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < len; i++) {
			char ch = sequence.charAt(i);
			if ((ch == colorChar || ch == TempSkyStrings.COLOR_CHAR) && (i + 1 < len) && TempSkyStrings.isColorChar(sequence.charAt(i + 1)))
				i++;
			else
				result.append(ch);
		}
		return result.toString();
	}

	@NotNull
	@Override
	public String strip(@NotNull String str) {
		return strip(str, '&');
	}
}
