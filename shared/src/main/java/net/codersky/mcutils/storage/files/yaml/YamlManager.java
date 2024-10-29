package net.codersky.mcutils.storage.files.yaml;

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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class YamlManager {

	private final File file;
	private final Yaml yaml;

	YamlManager(@NotNull String path) {
		this.yaml = getNewYaml();
		this.file = new File(path);
	}

	private Yaml getNewYaml() {
		final DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
		return new Yaml(dumperOptions);
	}

	@NotNull
	public File getFile() {
		return file;
	}

	public boolean reload(HashMap<String, Object> keys) {
		try {
			keys.clear();
			keys.putAll(this.yaml.load(new FileInputStream(this.file)));
			return true;
		} catch (FileNotFoundException | SecurityException ex) {
			return false;
		}
	}

	public boolean save(HashMap<String, Object> keys) {
		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
			writer.write(yaml.dump(keys));
			writer.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void update(@NotNull InputStream source, @NotNull HashMap<String, Object> keys) {
		final HashMap<String, Object> updMap = getNewYaml().load(source);
		for (Map.Entry<String, Object> entry : updMap.entrySet())
			if (!keys.containsKey(entry.getKey()))
				keys.put(entry.getKey(), entry.getValue());
	}
}
