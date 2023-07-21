package me.xdec0de.mcutils.strings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;

public interface FormatPattern {

	@Nullable
	String process(@Nonnull CommandSender target, @Nullable String string);
}