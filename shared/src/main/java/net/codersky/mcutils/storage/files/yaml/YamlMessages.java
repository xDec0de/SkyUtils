package net.codersky.mcutils.storage.files.yaml;

import net.codersky.mcutils.MCUtils;
import net.codersky.mcutils.storage.files.MessagesFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class YamlMessages extends YamlFile implements MessagesFile {

	public YamlMessages(@NotNull MCUtils<?> utils, @Nullable File parent, @NotNull String path) {
		super(utils, parent, path);
	}

	public YamlMessages(@NotNull MCUtils<?> utils, @NotNull String path) {
		super(utils, path);
	}

	@Nullable
	@Override
	public String getRawMessage(@NotNull String path) {
		return get(path, String.class);
	}
}
