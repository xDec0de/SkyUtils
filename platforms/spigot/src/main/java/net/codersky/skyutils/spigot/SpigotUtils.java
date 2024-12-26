package net.codersky.skyutils.spigot;

import net.codersky.skyutils.MCPlatform;
import net.codersky.skyutils.SkyUtils;
import net.codersky.skyutils.cmd.GlobalCommand;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.codersky.skyutils.crossplatform.server.ServerUtils;
import net.codersky.skyutils.java.SkyCollections;
import net.codersky.skyutils.java.reflection.RefObject;
import net.codersky.skyutils.java.strings.SkyStrings;
import net.codersky.skyutils.spigot.cmd.AdaptedSpigotCommand;
import net.codersky.skyutils.spigot.cmd.CustomSpigotCommand;
import net.codersky.skyutils.spigot.cmd.SpigotCommandSender;
import net.codersky.skyutils.spigot.console.SpigotConsole;
import net.codersky.skyutils.spigot.console.SpigotConsoleProvider;
import net.codersky.skyutils.spigot.player.SpigotPlayer;
import net.codersky.skyutils.spigot.player.SpigotPlayerProvider;
import net.codersky.skyutils.spigot.time.SpigotTaskScheduler;
import net.codersky.skyutils.spigot.worldgen.SingleBiomeProvider;
import net.codersky.skyutils.spigot.worldgen.VoidGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Spigot platform extension of the {@link ServerUtils} class,
 * which at the same time extends the {@link SkyUtils} class.
 * It offers access to the SkyUtils API for Spigot servers.
 *
 * @param <P> The {@link JavaPlugin} that owns this utils.
 *
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 */
public class SpigotUtils<P extends JavaPlugin> extends ServerUtils<P> {

	private final SpigotTaskScheduler scheduler;

	public SpigotUtils(@NotNull P plugin) {
		super(plugin);
		this.scheduler = new SpigotTaskScheduler(plugin);
	}

	@NotNull
	@Override
	public File getDataFolder() {
		return getPlugin().getDataFolder();
	}

	/*
	 - Player provider
	 */

	@NotNull
	public SpigotPlayerProvider getPlayerProvider() {
		return SkyUtilsSpigot.instance.playerProvider;
	}

	@NotNull
	public List<SkyPlayer> getOnlinePlayers() {
		return List.of();
	}

	@Nullable
	@Override
	public SpigotPlayer getPlayer(@NotNull UUID uuid) {
		return getPlayerProvider().getOnline(uuid);
	}

	@Nullable
	public SpigotPlayer getPlayer(@NotNull Player bukkit) {
		return getPlayer(bukkit.getUniqueId());
	}

	@Nullable
	public SpigotPlayer getPlayer(@NotNull String name) {
		final Player bukkit = Bukkit.getPlayer(name);
		return bukkit == null ? null : getPlayer(bukkit);
	}

	/*
	 - Console getter
	 */

	@NotNull
	@Override
	public SpigotConsole getConsole() {
		return SpigotConsoleProvider.getConsole();
	}

	/*
	 - MCPlatform
	 */

	@Override
	public @NotNull MCPlatform getPlatform() {
		return MCPlatform.SPIGOT;
	}

	/*
	 - Server version
	 */

	/**
	 * Gets the user-friendly name of the server version, for example, <i>"1.19.3"</i>.
	 *
	 * @return The current server version.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public static String getServerVersion() {
		String ver = Bukkit.getBukkitVersion();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < ver.length(); i++) {
			char c = ver.charAt(i);
			if (c == '-')
				break;
			builder.append(c);
		}
		return builder.toString();
	}

	/**
	 * Checks if the server supports the specified <b>version</b>.
	 * Supported formats are <i>"X.X"</i> and <i>"X.X.X"</i>.
	 * <br><br>
	 * <b>Note about 1.7.10</b>:
	 * <br>
	 * Because of the way this method works, if the server is running on
	 * 1.7.10 and you check if the server supports 1.7.9, the result will
	 * be false, that is because 1.7.10 gets translated to 17.1f, which is
	 * lower to 17.9f, this is the only version with this issue and this is
	 * highly unlikely to be fixed as this version is currently unsupported.
	 *
	 * @param version the server version to check, for example, <i>"1.19"</i>.
	 *
	 * @return true if the server version is higher or equal to the specified
	 * <b>version</b>, false otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 */
	public static boolean serverSupports(@NotNull String version) {
		if (version == null || version.isBlank())
			return false;
		float[] versions = new float[2]; // Convert to float, so 1.19.3 would be 119.3
		String server = getServerVersion();
		for (int v = 0; v <= 1; v++) {
			String ver = v == 0 ? server : version;
			int points = 0;
			for (int i = 0; i < ver.length(); i++) {
				char c = ver.charAt(i);
				if (c == '.')
					points++;
				else if (c >= '0' && c <= '9')
					versions[v] = points >= 2 ? versions[v] + ((c - '0') / 10.0f) : (versions[v] * 10) + (c - '0');
			}
		}
		return versions[0] >= versions[1];
	}

	/*
	 - Event registration
	 */

	/**
	 * A shortcut to register all the events of a {@link Listener}.
	 *
	 * @param <T> must implement {@link Listener}
	 * @param listener the listener to register.
	 *
	 * @return The registered {@code listener}.
	 *
	 * @throws NullPointerException if {@code listener} is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public <T extends Listener> T registerEvents(@NotNull T listener) {
		Bukkit.getPluginManager().registerEvents(listener, getPlugin());
		return listener;
	}

	/**
	 * A shortcut to register multiple {@link Listener Listeners}.
	 *
	 * @param listeners the {@link Listener Listeners} to register.
	 *
	 * @throws NullPointerException if any {@link Listener} or
	 * {@code listeners} itself is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public SpigotUtils<P> registerEvents(@NotNull Listener... listeners) {
		for (Listener listener : listeners)
			registerEvents(listener);
		return this;
	}

	/*
	 - Commands - Map
	 */

	/**
	 * Gets the {@link SimpleCommandMap} instance stored on the {@link Bukkit#getServer() server}.
	 * <b>Reflection is used</b> in order to get this instance by accessing the
	 * {@code public} getCommandMap method found on CraftServer, this means that this method
	 * will stop working if said method is removed or changed, even though using it should be
	 * safe as this method hasn't changed in a very long time.
	 * <p>
	 * <b>Note</b>: In case {@code null} is returned this method prints an error message to
	 * the console to notify administrators that the command map could not be obtained, specifying
	 * that this error is caused by SkyUtils and not by your plugin.
	 *
	 * @return The {@link SimpleCommandMap} instance stored on the {@link Bukkit#getServer() server},
	 * {@code null} if any error occurs.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	public SimpleCommandMap getCommandMap() {
		final RefObject map = new RefObject(Bukkit.getServer()).invoke("getCommandMap");
		if (map != null)
			return (SimpleCommandMap) map.getInstance();
		logCol(	"&8[&6" + getPlugin().getName() + "&8] &cCould get the command map, please inform about this&8.",
				" &8- &7SkyUtils is at fault here, do not contact &e" + getPlugin().getName() + "&7's author(s)&8.",
				" &8- &7Contact&8: &espigotmc.org/members/xdec0de_.178174/ &7or Discord &9@xdec0de_",
				" &8- &7Server info&8: &b" + Bukkit.getServer().getName() + " " + getServerVersion(),
				" &8- &7Using SkyUtils version &bv" + getSkyUtilsVersion());
		return null;
	}

	/*
	 - Commands - Registration
	 */

	/**
	 * Registers any amount of {@code commands}. This method registers commands
	 * in two phases depending on what type of commands you attempt to register:
	 * <p>
	 * <b>Phase 1</b>: The "traditional" way of registering commands. Commands
	 * are obtained from your {@code plugin.yml} file with {@link JavaPlugin#getCommand(String)},
	 * then their {@link PluginCommand#setExecutor(CommandExecutor) executor} and
	 * {@link PluginCommand#setTabCompleter(TabCompleter) tab completer} are set.
	 * <p>
	 * <b>Phase 2</b>: This phase is completely skipped if no commands are left after phase 1.
	 * For those commands that aren't on your {@code plugin.yml}, the
	 * {@link #getCommandMap() command map} is obtained <i>(Details below)</i>. Then these
	 * remaining commands are all {@link SimpleCommandMap#registerAll(String, List) registered}
	 * on said {@link #getCommandMap() map}.
	 * <p>
	 * <b>About {@link #getCommandMap()}</b>: On Spigot, the method to get the command map
	 * is {@code public}, but part of the CraftServer class and not the {@link Server}
	 * {@code interface}. Which means that we invoke the method with reflection in order to
	 * obtain the instance. On Paper, this is not required as a patch exists to expose the
	 * command map, however, you need to use the Paper version of SkyUtils to obtain
	 * the map without using reflection.
	 *
	 * @param commands The {@link CustomSpigotCommand spigot commands} to register.
	 *
	 * @return {@code true} if all {@code commands} were registered successfully,
	 * {@code false} otherwise. Command registration may <b>only</b> fail on <b>phase 2</b>
	 * if the {@link #getCommandMap()} method fails and returns {@code null}. That means
	 * of course that if all of your commands are registered on <b>phase 1</b>, this
	 * method will always return {@code true}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@SafeVarargs
	public final boolean registerCommands(@NotNull CustomSpigotCommand<P, ? extends SpigotCommandSender>... commands) {
		final List<Command> phase2 = new ArrayList<>();
		// Phase 1: Register via plugin.yml
		for (CustomSpigotCommand<P, ?> command : commands) {
			final PluginCommand plCommand = getPlugin().getCommand(command.getName());
			if (plCommand != null) {
				plCommand.setExecutor(command);
				plCommand.setTabCompleter(command);
			} else
				phase2.add(command);
		}
		// Phase 2: Register via SimpleCommandMap
		if (!phase2.isEmpty()) {
			final SimpleCommandMap map = getCommandMap();
			if (map == null)
				return false;
			map.registerAll(getPlugin().getName(), phase2);
		}
		return true;
	}

	/**
	 * Adapts all {@link GlobalCommand commands} to {@link AdaptedSpigotCommand},
	 * then registers all of them with the {@link #registerCommands(CustomSpigotCommand[])}
	 * method. Details about how command registration works on Spigot are provided on
	 * said method.
	 *
	 * @param commands The {@link GlobalCommand commands} to register.
	 *
	 * @return {@code true} if all commands were registered successfully,
	 * {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #registerCommands(CustomSpigotCommand[])
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean registerCommands(@NotNull GlobalCommand<P>... commands) {
		return registerCommands(SkyCollections.map(
				commands,
				new AdaptedSpigotCommand[commands.length],
				cmd -> new AdaptedSpigotCommand<>(this, cmd))
		);
	}

	/**
	 * Unregisters a command by {@code name}.
	 *
	 * @param name the name of the command to unregister.
	 *
	 * @return {@code true} if the command exists, was registered and
	 * has been unregistered successfully, {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Override
	public boolean unregisterCommand(@NotNull String name) {
		final SimpleCommandMap map = getCommandMap();
		if (map == null)
			return false;
		final Command cmd = map.getCommand(name);
		return cmd != null && cmd.unregister(map);
	}

	/**
	 * Unregisters the provided {@code command}.
	 *
	 * @param command The {@link Command} to unregister.
	 *
	 * @return {@code true} if the command was previously registered
	 * and has been
	 */
	public boolean unregisterCommand(@NotNull Command command) {
		final SimpleCommandMap map = getCommandMap();
		return map != null && command.unregister(map);
	}

	/*
	 - Commands - Getters
	 */

	/**
	 * Gets a {@link Collection} of all <b>registered</b> commands on
	 * the {@link Bukkit#getServer() server}, <b>not only</b> those registered
	 * by the {@link P plugin} that manages this {@link SpigotUtils} instance.
	 *
	 * @return A {@link Collection} of all <b>registered</b> commands on
	 * the {@link Bukkit#getServer() server}
	 *
	 * @since SkyUtils 1.0.0
	 * 
	 * @see #getCommand(String)
	 * @see #getCommand(Class)
	 */
	@Nullable
	public Collection<Command> getCommands() {
		final SimpleCommandMap map = getCommandMap();
		return map == null ? null : map.getCommands();
	}

	/**
	 * Gets a <b>registered</b> {@link Command} by {@code name}.
	 * The {@link Command} is not required to be registered by the
	 * {@link P plugin} that manages this {@link SpigotUtils} instance.
	 *
	 * @param name The name of the <b>registered</b> {@link Command} to get.
	 *
	 * @return A <b>registered</b> {@link Command} instance that matches the provided
	 * {@code name}, if found. {@code null} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #getCommand(Class)
	 * @see #getCommands()
	 */
	@Nullable
	public Command getCommand(@NotNull String name) {
		// TODO: Check if aliases are supported. Docs seem to indicate they aren't.
		final SimpleCommandMap map = getCommandMap();
		return map == null ? null : map.getCommand(name);
	}

	/**
	 * Gets a <b>registered</b> {@link C command} by {@link Class}.
	 * The {@link Command} is not required to be registered by the
	 * {@link P plugin} that manages this {@link SpigotUtils} instance.
	 *
	 * @param <C> The type of the class, {@code extends} {@link Command}.
	 *
	 * @param cmdClass The {@link Class} of the <b>registered</b> {@link C command} to get.
	 *
	 * @return A <b>registered</b> {@link C command} instance that matches the provided
	 * {@code cmdClass}, if found. {@code null} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #getCommand(String)
	 * @see #getCommands()
	 */
	@Nullable
	public <C extends Command> C getCommand(@NotNull Class<C> cmdClass) {
		final Collection<Command> cmds = getCommands();
		if (cmds == null)
			return null;
		final Command cmd = SkyCollections.get(cmds, c -> cmdClass.isAssignableFrom(c.getClass()));
		return cmd == null ? null : cmdClass.cast(cmd);
	}

	@NotNull
	@Override
	public SpigotTaskScheduler getScheduler() {
		return scheduler;
	}

	/*
	 - World creation
	 */

	/**
	 * Creates or loads a new {@link World} with the specified
	 * <b>creator</b>. If <b>creator</b> is {@code null},
	 * nothing will be done.
	 *
	 * @param creator the {@link WorldCreator} to use
	 * for the new {@link World}.
	 *
	 * @return The newly created or loaded {@link World}, {@code null}
	 * if {@code creator} is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	public World createWorld(@NotNull WorldCreator creator) {
		return Bukkit.createWorld(creator);
	}

	/**
	 * Creates or loads a new {@link World} with the specified
	 * <b>name</b> and world <b>type</b>. If <b>type</b>
	 * is {@code null}, {@link WorldType#NORMAL} will be used.
	 *
	 * @param name the name of the new {@link World}.
	 * @param type the {@link WorldType} of the new {@link World}.
	 *
	 * @return The newly created or loaded {@link World}, {@code null}
	 * if <b>name</b> is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	public World createWorld(@NotNull String name, @Nullable WorldType type) {
		return createWorld(WorldCreator.name(name).type(type == null ? WorldType.NORMAL : type));
	}

	/**
	 * Creates or loads a new {@link World} with the specified
	 * {@code name} and world {@code generator}.
	 *
	 * @param name the name of the new {@link World}.
	 * @param generator the {@link ChunkGenerator} of the new {@link World}.
	 *
	 * @return The newly created or loaded {@link World}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @throws NullPointerException If {@code name} is {@code null}.
	 *
	 * @see VoidGenerator
	 */
	@Nullable
	public World createWorld(@NotNull String name, @Nullable ChunkGenerator generator) {
		return createWorld(WorldCreator.name(name).generator(generator));
	}

	/**
	 * Creates or loads a new {@link World} with the specified
	 * <b>name</b>, world <b>generator</b> and <b>biomeProvider</b>.
	 *
	 * @param name the name of the new {@link World}.
	 * @param generator the {@link ChunkGenerator} of the new {@link World}. If
	 * {@code null}, the "natural" generator for the {@link World} will be used.
	 * @param biomeProvider the {@link BiomeProvider} of the new {@link World}.
	 * If {@code null}, the {@link BiomeProvider} of the <b>generator</b> will be used,
	 * if said <b>generator</b> doesn't specify a provider, the "natural" provider for
	 * the {@link World} will be used.
	 *
	 * @return The newly created or loaded {@link World}, {@code null}
	 * if <b>name</b> is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see VoidGenerator
	 * @see SingleBiomeProvider
	 */
	@Nullable
	public World createWorld(@NotNull String name, @Nullable ChunkGenerator generator, @Nullable BiomeProvider biomeProvider) {
		return createWorld(WorldCreator.name(name).generator(generator).biomeProvider(biomeProvider));
	}

	/**
	 * Utility method to check if the current {@link Thread} is the
	 * {@link Bukkit#isPrimaryThread() primary thread}. This can be
	 * used to fix accidental calls of blocking methods that should
	 * be called asynchronously in order to improve performance.
	 *
	 * @param warning the warning message to send when this method
	 * is executed outside the primary thread. If {@code null},
	 * "PERFORMANCE ISSUE: Blocking method called on the primary thread"
	 * will be used as the warning message.
	 * @param consumer a {@link Consumer} that will {@link Consumer#accept(Object)
	 * accept} the {@link StackTraceElement} array of the primary thread
	 * if this method is called on it.
	 *
	 * @return {@code true} if this method is called from the primary
	 * thread and warns about it, {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #warnOnPrimaryThread(String, Predicate, Consumer)
	 * @see #warnOnPrimaryThread(String)
	 */
	public static boolean warnOnPrimaryThread(@Nullable String warning, @NotNull Consumer<StackTraceElement[]> consumer) {
		if (!Bukkit.isPrimaryThread())
			return false;
		Bukkit.getLogger().warning(warning == null ? "PERFORMANCE ISSUE: Blocking method called on the primary thread" : warning);
		try {
			consumer.accept(Thread.currentThread().getStackTrace());
		} catch (SecurityException ex) {
			// Could be thrown by Thread#getStackTrace
		}
		return true;
	}

	public static boolean warnOnPrimaryThread(@Nullable String warning, @NotNull Predicate<StackTraceElement> condition, @NotNull Consumer<StackTraceElement> action) {
		return warnOnPrimaryThread(warning, elements -> {
			final int len = elements.length - 1;
			for (int i = 0; i < len; i++) {
				if (condition.test(elements[i])) {
					action.accept(elements[i + 1]);
					break;
				}
			}
		});
	}

	public static boolean warnOnPrimaryThread(@Nullable String warning) {
		return warnOnPrimaryThread(warning, elements -> {
			final StackTraceElement e = elements[elements.length >= 4 ? 4 : elements.length];
			Bukkit.getLogger().warning("- At: " + e.getClassName() + "#" + e.getMethodName() + " line " + e.getLineNumber());
		});
	}

	/*
	 * For removal (Legacy) methods
	 */

	@Deprecated(forRemoval = true)
	public boolean log(@Nullable String... stings) {
		Bukkit.getConsoleSender().sendMessage(stings);
		return true;
	}

	@Deprecated(forRemoval = true)
	public boolean logCol(@Nullable String... strings) {
		for (String str : strings)
			Bukkit.getConsoleSender().sendMessage(SkyStrings.applyColor(str));
		return true;
	}

	@Deprecated(forRemoval = true)
	public void logException(@Nullable Throwable throwable, @Nullable String header) {
		if (throwable == null)
			return;
		log(" ");
		if (header != null)
			logCol(header);
		logCol("&4"+throwable.getClass().getSimpleName()+"&8: &c" + throwable.getMessage());
		for(StackTraceElement element : throwable.getStackTrace()) {
			String error = element.toString().trim();
			if (error.contains(".jar")) {
				error = error.substring(error.lastIndexOf(".jar")+6);
				String path = error.substring(0, error.lastIndexOf('('));
				int lastPoint = path.lastIndexOf('.');
				path = "&cAt&8: &c"+path.substring(0, lastPoint) + "&6#&e" + path.substring(lastPoint + 1);
				final String line = error.substring(error.lastIndexOf(':')+1, error.length()-1);
				logCol(path+"&8 - &bline "+line);
			}
		}
		log(" ");
	}
}
