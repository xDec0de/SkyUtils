package net.codersky.skyutils.crossplatform.message.tag.color;

import net.codersky.skyutils.crossplatform.message.tag.MessageTag;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public interface GradientMessageTag extends MessageTag {

	GradientMessageTagResult NO_COLORS = new GradientMessageTagResult(new Color[0], 0);

	@NotNull
	GradientMessageTagResult getColors(@NotNull final String input);

	static @NotNull Color @NotNull[] createGradient(final @NotNull Color @NotNull[] colors, final int size) {
		if (colors.length == size)
			return colors;
		final Color[] gradient = new Color[size];
		final int segments = colors.length - 1;
		final int baseStepsPerSegment = size / segments;
		int remainder = size % segments;
		int index = 0;
		for (int i = 0; i < segments; i++) {
			final Color start = colors[i];
			final Color end = colors[i + 1];
			final int steps = baseStepsPerSegment + (i < remainder ? 1 : 0);
			if (steps == 1) {
				gradient[index++] = start;
				continue;
			}
			final float rStep = (end.getRed()   - start.getRed())   / (float) (steps - 1);
			final float gStep = (end.getGreen() - start.getGreen()) / (float) (steps - 1);
			final float bStep = (end.getBlue()  - start.getBlue())  / (float) (steps - 1);
			for (int j = 0; j < steps; j++) {
				final int r = Math.round(start.getRed()   + rStep * j);
				final int g = Math.round(start.getGreen() + gStep * j);
				final int b = Math.round(start.getBlue()  + bStep * j);
				gradient[index++] = new Color(r, g, b);
			}
		}
		return gradient;
	}
}
