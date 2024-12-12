package net.codersky.skyutils.velocity.cmd;

import net.codersky.skyutils.velocity.VelocityUtils;
import org.jetbrains.annotations.NotNull;

public abstract class VelocityCommand<P> extends CustomVelocityCommand<P, VelocityCommandSender> {

	public VelocityCommand(@NotNull VelocityUtils<P> utils, @NotNull String name, @NotNull String... aliases) {
		super(utils, name, aliases);
	}

	public VelocityCommand(@NotNull VelocityUtils<P> utils, @NotNull String name) {
		super(utils, name);
	}

	@Override
	protected final VelocityCommandSender getSender(@NotNull final Invocation invocation) {
		return new VelocityCommandSender(invocation.source(), getUtils());
	}
}
