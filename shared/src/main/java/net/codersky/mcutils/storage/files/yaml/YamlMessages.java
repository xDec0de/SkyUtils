package net.codersky.mcutils.storage.files.yaml;

import net.codersky.mcutils.Reloadable;
import net.codersky.mcutils.crossplatform.MessageReceiver;
import net.codersky.mcutils.java.MCFiles;
import net.codersky.mcutils.java.strings.MCStrings;
import net.codersky.mcutils.java.strings.Replacer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;

public class YamlMessages implements Reloadable {

	protected final HashMap<String, Object> keys = new HashMap<>();
	private final YamlManager manager;

	public YamlMessages(String path) {
		this.manager = new YamlManager(path);
	}

	/*
	 * File utils
	 */

	@NotNull
	public File asFile() {
		return manager.getFile();
	}

	public boolean exists() {
		return manager.getFile().exists();
	}

	/*
	 * Reloadable implementation
	 */

	public boolean setup() {
		return MCFiles.create(manager.getFile());
	}

	@Override
	public boolean reload() {
		return manager.reload(keys);
	}

	public boolean save() {
		return manager.save(keys);
	}

	/*
	 * Raw message getters
	 */

	@Nullable
	public String getRawMessage(@NotNull String path) {
		final Object obj = keys.get(path);
		return obj instanceof String str ? str : null;
	}

	@Nullable
	public String getRawMessage(@NotNull String path, @NotNull Replacer replacer) {
		final String str = getMessage(path);
		return str == null ? null : replacer.replaceAt(str);
	}

	@Nullable
	public String getRawMessage(@NotNull String path, @NotNull Object... replacements) {
		return getMessage(path, new Replacer(replacements));
	}

	/*
	 * Message getters
	 */

	@Nullable
	public String getMessage(@NotNull String path) {
		final String raw = getRawMessage(path);
		return raw == null ? null : MCStrings.applyColor(raw);
	}

	@Nullable
	public String getMessage(@NotNull String path, @NotNull Replacer replacer) {
		final String str = getMessage(path);
		return str == null ? null : replacer.replaceAt(str);
	}

	@Nullable
	public String getMessage(@NotNull String path, @NotNull Object... replacements) {
		return getMessage(path, new Replacer(replacements));
	}

	/*
	 * Message senders
	 */

	public boolean sendMessage(@NotNull MessageReceiver receiver, @NotNull String path) {
		final String raw = getRawMessage(path);
		return raw == null || MCStrings.sendMessage(receiver, raw);
	}

	public boolean sendMessage(@NotNull MessageReceiver receiver, @NotNull String path, @NotNull Replacer replacer) {
		final String raw = getRawMessage(path, replacer);
		return raw == null || MCStrings.sendMessage(receiver, raw);
	}

	public boolean sendMessage(@NotNull MessageReceiver receiver, @NotNull String path, @NotNull Object... replacements) {
		final String raw = getRawMessage(path, replacements);
		return raw == null || MCStrings.sendMessage(receiver, raw);
	}
}
