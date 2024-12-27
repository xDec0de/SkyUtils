package net.codersky.skyutils.velocity.player;

import com.velocitypowered.api.proxy.Player;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class VelocityPlayer extends OfflineVelocityPlayer implements SkyPlayer {

	private final Player handle;

	protected VelocityPlayer(@NotNull Player handle) {
		super(handle.getUniqueId());
		this.handle = handle;
	}

	/*
	 - OfflineVelocityPlayer Override
	 */

	@NotNull
	@Override
	public final Player getHandle() {
		return handle;
	}

	@NotNull
	@Override
	public UUID getUniqueId() {
		return handle.getUniqueId();
	}

	@NotNull
	@Override
	public String getName() {
		return handle.getUsername();
	}

	@Override
	public boolean isOnline() {
		return handle.isActive();
	}

	/*
	 - MessageReceiver implementation
	 */

	@Override
	public boolean sendMessage(@NotNull String message) {
		return sendMessage(Component.text(message));
	}

	@Override
	public boolean sendMessage(@NotNull Component message) {
		if (canReceive(message))
			handle.sendMessage(message);
		return true;
	}

	/*
	 - ActionBar
	 */

	@Override
	public boolean sendActionBar(@NotNull String message) {
		return sendActionBar(Component.text(message));
	}

	@Override
	public boolean sendActionBar(@NotNull Component message) {
		if (canReceive(message))
			handle.sendActionBar(message);
		return true;
	}

	/*
	 - Sounds
	 */

	@Override
	public boolean playSound(@NotNull Sound sound) {
		handle.playSound(sound);
		return true;
	}
}
