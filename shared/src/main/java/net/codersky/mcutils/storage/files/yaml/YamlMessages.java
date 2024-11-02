package net.codersky.mcutils.storage.files.yaml;

import net.codersky.mcutils.MCUtils;
import net.codersky.mcutils.java.strings.Replacer;
import net.codersky.mcutils.storage.files.MessagesFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class YamlMessages extends YamlFile implements MessagesFile {

	private Replacer defReplacer = null;

	public YamlMessages(@NotNull MCUtils<?> utils, @Nullable File parent, @NotNull String path) {
		super(utils, parent, path);
	}

	public YamlMessages(@NotNull MCUtils<?> utils, @NotNull String path) {
		super(utils, path);
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
