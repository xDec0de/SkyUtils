package net.codersky.mcutils.storage.files.yaml;

import net.codersky.mcutils.storage.Config;
import net.codersky.mcutils.Reloadable;
import net.codersky.mcutils.java.MCFiles;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

public class YamlConfig implements Config {

	protected final HashMap<String, Object> keys = new HashMap<>();
	private final YamlManager manager;

	public YamlConfig(String path) {
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

	@Override
	public boolean setup() {
		return MCFiles.create(manager.getFile());
	}

	@Override
	public boolean reload() {
		return manager.reload(keys);
	}

	@Override
	public @NotNull HashMap<String, Object> getMap() {
		return keys;
	}

	@Override
	public boolean save() {
		return manager.save(keys);
	}
}
