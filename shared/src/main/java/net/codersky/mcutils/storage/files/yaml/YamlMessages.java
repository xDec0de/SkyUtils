package net.codersky.mcutils.storage.files.yaml;

import net.codersky.mcutils.storage.files.MessagesFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlMessages extends YamlFile implements MessagesFile {

	public YamlMessages(String path) {
		super(path);
	}

	@Nullable
	@Override
	public String getRawMessage(@NotNull String path) {
		return get(path, String.class);
	}
}
