package net.codersky.skyutils.crossplatform.message.tag.color;

import net.codersky.jsky.math.JNumbers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;

public class HexGradientMessageTag implements ColorMessageTag {

	// NOTE: Tag format: <g:F00:000:Hello world>

	public static final HexGradientMessageTag INSTANCE = new HexGradientMessageTag();

	private HexGradientMessageTag() {}

	/*
	 - MessageTag implementation
	 */

	@Override
	public boolean matches(@NotNull String name) {
		return name.equals("g") || name.equals("gradient");
	}

	/*
	 - ColorMessageTag implementation
	 */

	@Override
	public @NotNull Color @NotNull [] getColors(@NotNull final String input) {
		final int separator = input.lastIndexOf(':');
		if (separator == -1)
			return NO_COLORS;
		final Color[] hexColors = getHexColors(input.substring(0, separator));
		if (hexColors == null)
			return NO_COLORS;
		if (hexColors.length <= 1)
			return hexColors;
		return ColorMessageTag.createGradient(hexColors, input.length() - separator - 1);
	}

	@Nullable
	private Color[] getHexColors(final String input) {
		if (input.length() < 3)
			return null;
		final String[] codes = input.split(":");
		final Color[] colors = new Color[codes.length];
		for (int i = 0; i < codes.length; i++)
			colors[i] = colorFromHex(codes[i]);
		return colors;
	}

	@NotNull
	private Color colorFromHex(@NotNull final String hex) {
		final int start = hex.charAt(0) == '#' ? 1 : 0;
		final int size = hex.length() - start;
		int[] positions = null;
		if (size == 6)
			positions = new int[] {0, 1, 2, 3, 4, 5};
		if (size == 3)
			positions = new int[] {0, 0, 1, 1, 2, 2};
		if (positions == null)
			return Color.BLACK;
		final char[] hexChars = new char[] {'#', '\0', '\0', '\0', '\0', '\0', '\0'};
		for (int i = 0; i < positions.length; i++) {
			final char hexChar = hex.charAt(start + positions[i]);
			if (!JNumbers.isHexChar(hexChar))
				return Color.BLACK;
			hexChars[i + 1] = hexChar;
		}
		return Color.decode(String.valueOf(hexChars));
	}
}
