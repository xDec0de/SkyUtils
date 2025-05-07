package net.codersky.skyutils.velocity.player;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.Player;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
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
	 - Version
	 */

	@NotNull
	public ProtocolVersion getVersion() {
		return getHandle().getProtocolVersion();
	}

	public boolean supportsRgb() {
		return getVersion().greaterThan(ProtocolVersion.MINECRAFT_1_15_2);
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
	 - JSON messages
	 */

	@Override
	public boolean sendJsonMessage(@NotNull String json) {
		final GsonComponentSerializer serializer;
		serializer = supportsRgb() ? GsonComponentSerializer.gson() : GsonComponentSerializer.colorDownsamplingGson();
		final Component component = serializer.deserialize(json);
		if (Component.IS_NOT_EMPTY.test(component))
			getHandle().sendMessage(component);
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
