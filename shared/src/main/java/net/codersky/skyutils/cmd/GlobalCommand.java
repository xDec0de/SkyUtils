package net.codersky.skyutils.cmd;

import net.codersky.skyutils.SkyUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class GlobalCommand<P> implements SkyCommand<P, MCCommandSender> {

	private final SkyUtils<P> utils;
	private final String name;
	private final List<String> aliases;
	private final SubCommandHandler<P, MCCommandSender> subCmdHandler = new SubCommandHandler<>();

	public GlobalCommand(SkyUtils<P> utils, @NotNull String name, List<String> aliases) {
		this.utils = utils;
		this.name = name;
		this.aliases = aliases;
	}

	public GlobalCommand(SkyUtils<P> utils, @NotNull String name) {
		this(utils, name, List.of());
	}

	public GlobalCommand(SkyUtils<P> utils, @NotNull String name, String... aliases) {
		this(utils, name, List.of(aliases));
	}

	@NotNull
	@Override
	public String getName() {
		return name;
	}

	@Override
	public @NotNull List<String> getAliases() {
		return aliases;
	}

	@NotNull
	@Override
	public SkyUtils<P> getUtils() {
		return utils;
	}

	@Override
	public @NotNull SkyCommand<P, MCCommandSender> inject(@NotNull SkyCommand<P, MCCommandSender>... commands) {
		subCmdHandler.inject(commands);
		return this;
	}
}
