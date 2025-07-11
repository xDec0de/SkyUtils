package net.codersky.skyutils.crossplatform.message;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class MessagePatternFilter {

	private Predicate<String> tagFilter = null;

	private @NotNull MessagePatternFilter addTagFilter(@NotNull Predicate<String> filter) {
		if (this.tagFilter == null)
			this.tagFilter = filter;
		else tagFilter = tagFilter.or(filter);
		return this;
	}

	/*
	 - All colors
	 */

	public @NotNull MessagePatternFilter allColor() {
		return addTagFilter(key -> {;
			if (key.indexOf(':') == -1)
				return checkColorName(key);
			return (key.startsWith("c:") || key.startsWith("color:") || key.startsWith("colour:"));
		});
	}

	private boolean checkColorName(@NotNull String name) {
		return name.equals("black") || name.equals("dark_blue") || name.equals("dark_green")
				|| name.equals("dark_aqua") || name.equals("dark_red") || name.equals("dark_purple")
				|| name.equals("gold") || name.equals("gray") || name.equals("dark_gray")
				|| name.equals("blue") || name.equals("green") || name.equals("aqua")
				|| name.equals("red") || name.equals("light_purple") || name.equals("yellow") || name.equals("white");
	}
}
