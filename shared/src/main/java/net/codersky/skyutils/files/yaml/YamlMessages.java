package net.codersky.skyutils.files.yaml;

import net.codersky.jsky.strings.Replacer;
import net.codersky.jsky.yaml.YamlFile;
import net.codersky.skyutils.SkyUtils;
import net.codersky.skyutils.files.MessagesFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class YamlMessages extends YamlFile implements MessagesFile {

	private Replacer defReplacer = null;

	public YamlMessages(@NotNull SkyUtils<?> utils, @Nullable File parent, @NotNull String path) {
		super(utils.getPlugin().getClass().getClassLoader(), parent, path);
	}

	public YamlMessages(@NotNull SkyUtils<?> utils, @NotNull String path) {
		this(utils, utils.getDataFolder(), path);
	}

	@Nullable
	@Override
	public Replacer getDefaultReplacer() {
		return defReplacer;
	}

	@NotNull
	@Override
	public MessagesFile setDefaultReplacer(@Nullable Replacer replacer) {
		this.defReplacer = replacer;
		return this;
	}

	@Nullable
	@Override
	public String getRawMessage(@NotNull String path) {
		return getMap().get(path, String.class);
	}
}
