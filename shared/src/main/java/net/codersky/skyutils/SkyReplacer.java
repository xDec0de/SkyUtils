package net.codersky.skyutils;

import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.Replacer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Extension of JSky's {@link Replacer} class to support
 * Adventure's {@link Component Components}.
 *
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 */
public class SkyReplacer extends Replacer {

	/**
	 * Applies this {@link SkyReplacer} to the specified {@link Component}.
	 *
	 * @param component The {@link Component} to apply the replacements to.
	 *
	 * @return A new {@link Component} with all {@link #getReplacements() replacements} applied to it.
	 *
	 * @throws NullPointerException if {@code component} is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #replaceAt(String)
	 * @see #replaceAt(String...)
	 */
	@NotNull
	public Component replaceAt(@NotNull Component component) {
		final int repLstLen = getReplacements().size();
		if (repLstLen == 0)
			return component;
		Component result = component;
		for (int i = 0; i <= repLstLen - 1; i += 2) {
			final String match = getReplacements().get(i).toString();
			final Object value = getReplacements().get(i + 1);
			if (value instanceof ComponentLike componentLike)
				result = result.replaceText(b -> b.matchLiteral(match).replacement(componentLike));
			else
				result = result.replaceText(b -> b.matchLiteral(match).replacement(value.toString()));
		}
		return result;
	}

	/**
	 * Applies this {@link SkyReplacer} to the specified {@link List} of {@link Component components}.
	 *
	 * @param list The {@link Component} {@link List} to apply the replacements to.
	 *
	 * @return A new <b>modifiable</b> {@link Component} {@link List} with the replacements applied to it.
	 *
	 * @throws NullPointerException if {@code list} or any element of it is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #replaceAt(String)
	 * @see #replaceAt(String...)
	 * @see #replaceAt(Component)
	 * @see #replaceAtComponents(List)
	 */
	@NotNull
	public List<Component> replaceAtComponents(@NotNull List<Component> list) {
		return JCollections.map(list, this::replaceAt);
	}
}
