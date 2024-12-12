package net.codersky.skyutils.velocity.cmd;

import com.velocitypowered.api.command.SimpleCommand;
import net.codersky.skyutils.cmd.MCCommand;
import net.codersky.skyutils.cmd.MCCommandSender;
import net.codersky.skyutils.cmd.SubCommandHandler;
import net.codersky.skyutils.java.SkyCollections;
import net.codersky.skyutils.velocity.VelocityUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public abstract class CustomVelocityCommand<P, S extends MCCommandSender> implements SimpleCommand, MCCommand<P, S> {

	private final VelocityUtils<P> utils;
	private final String name;
	private final String[] aliases;
	private final SubCommandHandler<P, S> subCmdHandler = new SubCommandHandler<>();

	public CustomVelocityCommand(@NotNull VelocityUtils<P> utils, @NotNull String name, @NotNull String... aliases) {
		this.utils = utils;
		this.name = Objects.requireNonNull(name).toLowerCase();
		this.aliases = Objects.requireNonNull(aliases);
	}

	public CustomVelocityCommand(@NotNull VelocityUtils<P> utils, @NotNull String name) {
		this(utils, name, new String[0]);
	}

	@NotNull
	public VelocityUtils<P> getUtils() {
		return utils;
	}

	@NotNull
	public P getPlugin() {
		return utils.getPlugin();
	}

	@NotNull
	@Override
	public final String getName() {
		return name;
	}

	@NotNull
	@Override
	public final List<String> getAliases() {
		return SkyCollections.asArrayList(aliases);
	}

	protected abstract S getSender(@NotNull Invocation invocation);

	@NotNull
	public final String[] getAliasesArray() {
		return aliases;
	}

	@NotNull
	@Override
	public CustomVelocityCommand<P, S> inject(MCCommand<P, S>... commands) {
		subCmdHandler.inject(commands);
		return this;
	}

	@Override
	@ApiStatus.Internal
	public final void execute(@NotNull final Invocation invocation) {
		subCmdHandler.onCommand(this, getSender(invocation), invocation.arguments());
	}

	@Override
	@ApiStatus.Internal
	public final CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
		return CompletableFuture.supplyAsync(() -> subCmdHandler.onTab(this, getSender(invocation), invocation.arguments()));
	}
}
