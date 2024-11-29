package net.codersky.skyutils.java.strings.pattern.target;

import net.codersky.skyutils.crossplatform.MessageReceiver;
import net.codersky.skyutils.crossplatform.player.MCPlayer;
import net.codersky.skyutils.java.strings.SkyStrings;
import net.codersky.skyutils.java.strings.pattern.TargetPattern;
import org.jetbrains.annotations.NotNull;

public class ActionBarTargetPattern implements TargetPattern {

	@NotNull
	@Override
	public String process(@NotNull MessageReceiver target, @NotNull String string, boolean applyEventPatterns) {
		return SkyStrings.match(string, "<ab:", "/ab>", message -> {
			if (target instanceof MCPlayer player)
				player.sendActionBar(message);
		});
	}
}
