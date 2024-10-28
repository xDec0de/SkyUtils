package net.codersky.mcutils.storage.files.yaml;

import net.codersky.mcutils.Reloadable;
import net.codersky.mcutils.java.MCFiles;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

public class MessagesFile implements Reloadable {

	protected final HashMap<String, Object> keys = new HashMap<>();
	private final YamlManager manager;

	public MessagesFile(String path) {
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

	@Override
	public boolean save() {
		return manager.save(keys);
	}
}
