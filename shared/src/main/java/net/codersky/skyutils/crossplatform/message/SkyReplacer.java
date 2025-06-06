package net.codersky.skyutils.crossplatform.message;

import net.codersky.jsky.strings.Replacement;
import net.codersky.jsky.strings.Replacer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Extension of JSky's {@link Replacer} {@code class}
 * to support replacing at {@link SkyMessage}s.
 *
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 */
public class SkyReplacer extends Replacer {

	/**
	 * {@inheritDoc}
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see Replacer#Replacer(Object...)  Replacer
	 */
	public SkyReplacer(@NotNull Object... replacements) {
		super(replacements);
	}

	/**
	 * Applies this {@link SkyReplacer} to the specified {@code message}.
	 *
	 * @param message The {@link SkyMessage} to apply the replacements to.
	 *
	 * @return The {@code message}, with this {@link SkyReplacer} applied
	 * to it.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #replaceAt(String)
	 */
	@NotNull
	public SkyMessage replaceAt(@NotNull final SkyMessage message) {
		message.console = replaceAt(message.console);
		message.player = replaceAt(message.player);
		return message;
	}

	@NotNull
	@Deprecated(forRemoval = true)
	public Component replaceAt(@NotNull Component component) {
		if (this.replacementsMap.isEmpty())
			return component;
		Component result = component;
		for (Map.Entry<String, Object> entry : this.replacementsMap.entrySet()) {
			final String match = entry.getKey();
			final String value = Replacement.toStringValue(entry.getValue());
			result = result.replaceText(b -> b.matchLiteral(match).replacement(value));
		}
		return result;
	}

	@NotNull
	@Override
	public SkyReplacer clone() {
		final SkyReplacer clone = new SkyReplacer();
		clone.replacementsMap.putAll(this.replacementsMap);
		return clone;
	}
}
