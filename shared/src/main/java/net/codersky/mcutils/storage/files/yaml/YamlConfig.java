package net.codersky.mcutils.storage.files.yaml;

import net.codersky.mcutils.storage.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class YamlConfig extends YamlFile implements Config {

	public YamlConfig(@Nullable File parent, @NotNull String path) {
		super(parent, path);
	}
}
