package net.codersky.mcutils.storage.files.yaml;

import net.codersky.mcutils.MCUtils;
import net.codersky.mcutils.storage.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class YamlConfig extends YamlFile implements Config {

	public YamlConfig(@NotNull MCUtils<?> utils, @Nullable File parent, @NotNull String path) {
		super(utils, parent, path);
	}

	public YamlConfig(@NotNull MCUtils<?> utils, @NotNull String path) {
		super(utils, path);
	}
}
