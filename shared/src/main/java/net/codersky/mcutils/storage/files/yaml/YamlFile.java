package net.codersky.mcutils.storage.files.yaml;

import net.codersky.mcutils.Reloadable;
import net.codersky.mcutils.java.MCFiles;
import net.codersky.mcutils.storage.DataHandler;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class YamlFile implements DataHandler, Reloadable {

	protected final HashMap<String, Object> keys = new HashMap<>();
	private final File file;
	private final Yaml yaml;

	YamlFile(@NotNull String path) {
		this.yaml = getNewYaml();
		this.file = new File(path);
	}

	/*
	 * DataHandler implementation
	 */

	@NotNull
	@Override
	public HashMap<String, Object> getMap() {
		return keys;
	}

	/*
	 * SnakeYaml utilities
	 */

	@NotNull
	private Yaml getNewYaml() {
		final DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
		return new Yaml(dumperOptions);
	}

	/*
	 * File handling
	 */

	@NotNull
	public File asFile() {
		return file;
	}

	public boolean exists() {
		return file.exists();
	}

	public boolean setup() {
		return MCFiles.create(file);
	}

	public boolean save() {
		if (!setup())
			return false;
		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
			writer.write(yaml.dump(keys));
			writer.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	protected void update(@NotNull InputStream source) {
		final HashMap<String, Object> updMap = getNewYaml().load(source);
		for (Map.Entry<String, Object> entry : updMap.entrySet())
			if (!keys.containsKey(entry.getKey()))
				keys.put(entry.getKey(), entry.getValue());
	}

	/*
	 * Reloadable implementation
	 */

	public boolean reload() {
		try {
			keys.clear();
			keys.putAll(this.yaml.load(new FileInputStream(this.file)));
			return true;
		} catch (FileNotFoundException | SecurityException ex) {
			return false;
		}
	}
}
