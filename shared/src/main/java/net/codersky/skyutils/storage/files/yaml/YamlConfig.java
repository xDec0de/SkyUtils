package net.codersky.skyutils.storage.files.yaml;

import net.codersky.skyutils.SkyUtils;
import net.codersky.skyutils.storage.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class YamlConfig extends YamlFile implements Config {

	public YamlConfig(@NotNull SkyUtils<?> utils, @Nullable File parent, @NotNull String path) {
		super(utils, parent, path);
	}

	public YamlConfig(@NotNull SkyUtils<?> utils, @NotNull String path) {
		super(utils, path);
	}
}
