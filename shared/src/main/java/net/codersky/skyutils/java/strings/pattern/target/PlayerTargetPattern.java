package net.codersky.skyutils.java.strings.pattern.target;

import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.codersky.skyutils.java.strings.SkyStrings;
import net.codersky.skyutils.java.strings.pattern.TargetPattern;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link TargetPattern} that matches the following pattern:
 * <p>
 * {@code <p:content/p>}
 * <p>
 * Where {@code content} can be replaced with whatever message you want to send.
 * This {@link TargetPattern} will only send the content to a {@link MessageReceiver}
 * if it is an instance of {@link SkyPlayer}.
 * <p>
 * This {@link TargetPattern} supports event patterns, details about this can be found
 * on {@link TargetPattern here}, under the "<b>ABOUT EVENT PATTERNS</b>" section.
 *
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 */
public class PlayerTargetPattern implements TargetPattern {

	@NotNull
	@Override
	public String process(@NotNull MessageReceiver target, @NotNull String string, boolean applyEventPatterns) {
		return SkyStrings.match(string, "<p:", "/p>", message -> {
			if (target instanceof SkyPlayer player) {
				if (applyEventPatterns)
					player.sendMessage(SkyStrings.applyEventPatterns(message));
				else
					player.sendMessage(message);
			}
		});
	}
}
