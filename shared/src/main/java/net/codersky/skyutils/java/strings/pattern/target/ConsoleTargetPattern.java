package net.codersky.skyutils.java.strings.pattern.target;

import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.SkyConsole;
import net.codersky.skyutils.java.strings.SkyStrings;
import net.codersky.skyutils.java.strings.pattern.TargetPattern;
import org.jetbrains.annotations.NotNull;

public class ConsoleTargetPattern implements TargetPattern {

	@NotNull
	@Override
	public String process(@NotNull MessageReceiver target, @NotNull String string, boolean applyEventPatterns) {
		return SkyStrings.match(string, "<c:", "/c>", message -> {
			if (target instanceof SkyConsole console)
				console.sendMessage(message);
		});
	}
}
