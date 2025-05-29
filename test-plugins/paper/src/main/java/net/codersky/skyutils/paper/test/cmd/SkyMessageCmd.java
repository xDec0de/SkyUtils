package net.codersky.skyutils.paper.test.cmd;

import net.codersky.skyutils.crossplatform.message.SkyMessage;
import net.codersky.skyutils.paper.PaperUtils;
import net.codersky.skyutils.paper.test.SKUPaperTest;
import net.codersky.skyutils.spigot.cmd.SpigotCommand;
import net.codersky.skyutils.spigot.cmd.SpigotCommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SkyMessageCmd extends SpigotCommand<SKUPaperTest> {

	public SkyMessageCmd(@NotNull final PaperUtils<SKUPaperTest> utils) {
		super(utils, "skymsg");
	}

	@Override
	public boolean onCommand(@NotNull SpigotCommandSender sender, @NotNull String[] args) {
		final String msg = args.length == 0 ? "Provide a custom message" : String.join(" ", args);
		return sender.sendMessage(SkyMessage.of(msg));
	}

	@Override
	public @Nullable List<String> onTab(@NotNull SpigotCommandSender sender, @NotNull String[] args) {
		return List.of();
	}
}
