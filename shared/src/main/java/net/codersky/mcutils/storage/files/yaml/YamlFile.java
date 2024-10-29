package net.codersky.mcutils.storage.files.yaml;

import net.codersky.mcutils.Reloadable;
import net.codersky.mcutils.java.MCFiles;
import net.codersky.mcutils.storage.DataHandler;
import net.codersky.mcutils.storage.files.UpdatableFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class YamlFile implements DataHandler, Reloadable, UpdatableFile {

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

	/*
	 * UpdatableFile implementation
	 */

	/**
	 * Gets the updated {@link InputStream} of this {@link YamlFile}.
	 * By default, this will be {@link Class#getResourceAsStream(String)}
	 * with the {@code name} being {@link #asFile asFile().}{@link File#getName() getName()}.
	 * The returned value of this method directly affects {@link #update(List)},
	 * as this is the {@link InputStream} that said method will compare against, if
	 * {@code null}, the file won't update.
	 * 
	 * @return The updated {@link InputStream} of this {@link YamlFile}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	protected InputStream getUpdatedStream() {
		return getClass().getResourceAsStream(file.getPath());
	}

	private boolean isIgnored(String path, @Nullable List<String> ignored) {
		if (ignored == null)
			return false;
		for (String ignoredPath : ignored)
			if (path.startsWith(ignoredPath))
				return true;
		return false;
	}

	public boolean update(@Nullable List<String> ignored) {
		final InputStream updated = getUpdatedStream();
		if (updated == null)
			return false;
		final HashMap<String, Object> updMap = getNewYaml().load(getUpdatedStream());
		for (Map.Entry<String, Object> entry : updMap.entrySet())
			if (!keys.containsKey(entry.getKey()) && !isIgnored(entry.getKey(), ignored))
				keys.put(entry.getKey(), entry.getValue());
		return true;
	}

	/*
	 * Object class
	 */

	@Override
	public boolean equals(@Nullable Object obj) {
		return obj instanceof final YamlFile other && other.getMap().equals(this.getMap());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getMap());
	}

	@Override
	public String toString() {
		return "YamlFile" + getMap();
	}
}
