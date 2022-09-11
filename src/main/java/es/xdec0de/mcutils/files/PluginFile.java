package es.xdec0de.mcutils.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.mcutils.MCPlugin;

/**
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public class PluginFile extends YmlFile {

	/**
	 * Creates an instance, <b>NOT</b> a file, of a {@link PluginFile} for the specified <b>plugin</b> and <b>path</b>.
	 * Fast access for file creation is provided by {@link MCPlugin#registerFile(String, Class)}.
	 * <p>
	 * {@link PluginFile}s are required to be on <b>plugin</b>'s jar file as a resource. For this
	 * exact reason they can be easily updated to newer versions. This files can be used, for example,
	 * as configuration files that require certain content to be present on them and are likely to be
	 * updated on future versions.
	 * 
	 * @param plugin an instance of the plugin creating the file, used to get it's data folder.
	 * @param path the path of the file to create, the ".yml" extension is automatically added if missing,
	 * if the path is null, empty or blank, "file" will be used.
	 * @param charset the charset to use, if null, {@link StandardCharsets#UTF_8} will be used.
	 * 
	 * @throws NullPointerException if <b>plugin</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCPlugin#registerFile(PluginFile)
	 * @see MCPlugin#registerFile(String, Class)
	 * @see #create()
	 */
	public PluginFile(@Nonnull JavaPlugin plugin, @Nullable String path, @Nullable Charset charset) {
		super(plugin, path, charset);
	}

	/**
	 * Creates an instance, <b>NOT</b> a file, of a {@link PluginFile} for the specified <b>plugin</b> and <b>path</b>.
	 * Fast access for file creation is provided by {@link MCPlugin#registerFile(String, Class)}.
	 * <p>
	 * {@link PluginFile}s are required to be on <b>plugin</b>'s jar file as a resource. For this
	 * exact reason they can be easily updated to newer versions. This files can be used, for example,
	 * as configuration files that require certain content to be present on them and are likely to be
	 * updated on future versions.
	 * <p>
	 * This constructor uses {@link StandardCharsets#UTF_8}, to specify use {@link #PluginFile(JavaPlugin, String, Charset)}
	 * 
	 * @param plugin an instance of the plugin creating the file, used to get it's data folder.
	 * @param path the path of the file to create, the ".yml" extension is automatically added if missing,
	 * if the path is null, empty or blank, "file" will be used.
	 * 
	 * @throws NullPointerException if <b>plugin</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCPlugin#registerFile(PluginFile)
	 * @see MCPlugin#registerFile(String, Class)
	 * @see #create()
	 */
	public PluginFile(@Nonnull JavaPlugin plugin, @Nullable String path) {
		super(plugin, path, StandardCharsets.UTF_8);
	}

	/**
	 * Creates this file, the file MUST be present
	 * on the plugin's jar file as a resource.
	 * This is required in order to copy the file from
	 * the plugin source and to update it. If you don't need
	 * this functionality and want an empty file instead, use
	 * {@link YmlFile}
	 * <p>
	 * This method is not required when calling
	 * {@link MCPlugin#registerFile(String, Class)}
	 * as it does this automatically.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Override
	public void create() {
		file.getParentFile().mkdirs();
		if(!file.exists())
			plugin.saveResource(getPath(), false);
		reload(false);
	}

	/**
	 * Reloads this file without updating. Any non saved value contained within this configuration will be removed
	 * and the new values will be loaded from the given file. If there is any error reloading the file,
	 * the errors will be logged and false will be returned. Use {@link #reload(boolean)} to update.
	 * 
	 * @return True if no errors occurred while reloading, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reload(boolean)
	 * @see #reload(List)
	 */
	@Nonnull
	@Override
	public boolean reload() {
		return reload(false);
	}

	/**
	 * Reloads this file. Any non saved value contained within this configuration will be removed
	 * and the new values will be loaded from the given file. If there are any errors reloading or
	 * updating the file, the errors will be logged and false will be returned.
	 * 
	 * @param update whether to update the files or not.
	 * This is recommended to be true to prevent any
	 * missing path after a file modification, if you
	 * want some paths to be ignored by the updater, use
	 * {@link #reload(List)}.
	 * 
	 * @return true if no errors occurred while reloading and updating, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reload()
	 * @see #reload(List)
	 */
	@Nonnull
	public boolean reload(boolean update) {
		final boolean reload = super.reload();
		if (update)
			return update(true, new ArrayList<>(0));
		return reload;
	}

	/**
	 * Reloads this file. The file updater ignores the paths present
	 * on <b>ignoredUpdatePaths</b>, this is specially useful
	 * if you want administrators to create their own paths
	 * without them being removed, for example, on a GUI plugin.
	 * If there are any errors reloading or updating the file,
	 * they will be logged and false will be returned.
	 * <p>
	 * Update is assumed to be true with this method unless
	 * <b>ignoredUpdatePaths</b> is null, if you want to reload without
	 * updating use {@link #reload(boolean)} with <b>update</b> as false.
	 * 
	 * @param ignoredUpdatePaths a list with the paths to
	 * be ignored by the file updater.
	 * 
	 * @return true if no errors occurred while reloading and updating, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reload()
	 * @see #reload(boolean)
	 */
	@Nonnull
	public boolean reload(@Nullable List<String> ignoredUpdatePaths) {
		super.reload();
		return update(true, (ignoredUpdatePaths == null ? new ArrayList<>(0) : ignoredUpdatePaths));
	}

	private File copyInputStreamToFile(String path, InputStream inputStream) {
		File file = new File(path);
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			int read;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1)
				outputStream.write(bytes, 0, read);
			return file;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Updates this file.
	 * 
	 * @param reload if this method is getting called by a reload.
	 * @param ign a list of paths to ignore on update.
	 * 
	 * @return True if no errors occurred, false otherwise.
	 */
	private boolean update(boolean reload, List<String> ign) {
		String pluginName = plugin.getName();
		try {
			int changes = 0;
			CharsetYamlConfiguration updated = new CharsetYamlConfiguration(getCharset());
			if (plugin.getResource(getPath()) != null)
				updated.load(copyInputStreamToFile(plugin.getDataFolder()+ "/"+getPath(), plugin.getResource(getPath())));
			else
				return log("&8[&4"+pluginName+"&8] > &cCould not update &6"+getPath()+"&8: &4File not found", false);
			Set<String> oldKeys = ign.isEmpty() ? getKeys(true) : getKeys(true).stream().filter(str -> !ign.contains(str)).collect(Collectors.toSet());
			Set<String> updKeys = ign.isEmpty() ? updated.getKeys(true) : updated.getKeys(true).stream().filter(str -> !ign.contains(str)).collect(Collectors.toSet());
			for (String str : oldKeys)
				if(!updKeys.contains(str)) {
					set(str, null);
					changes++;
				}
			for (String str : updKeys)
				if(!oldKeys.contains(str)) {
					set(str, updated.get(str));
					changes++;
				}
			save(plugin.getDataFolder() + "/"+getPath());
			if (changes != 0)
				return reload ?
					log("&8[&6"+pluginName+"&8] > &6"+getPath()+" &7has been reloaded with &b"+changes+" &7changes.", true) :
					log("&8[&6"+pluginName+"&8] > &6"+getPath()+" &7has been updated to &ev"+plugin.getDescription().getVersion()+"&7 with &b"+changes+" &7changes.", true);
			return true;
		} catch(InvalidConfigurationException | IOException ex) {
			return log("&8[&4"+pluginName+"&8] > &cCould not update &6"+getPath()+"&8: &4"+ex.getMessage(), false);
		}
	}

	private boolean log(String str, boolean ret) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', str));
		return ret;
	}
}