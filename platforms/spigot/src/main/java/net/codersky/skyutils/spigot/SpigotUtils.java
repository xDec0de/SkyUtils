package net.codersky.skyutils.spigot;

import net.codersky.skyutils.MCPlatform;
import net.codersky.skyutils.cmd.GlobalCommand;
import net.codersky.skyutils.cmd.MCCommand;
import net.codersky.skyutils.crossplatform.player.MCPlayer;
import net.codersky.skyutils.crossplatform.server.ServerUtils;
import net.codersky.skyutils.java.SkyCollections;
import net.codersky.skyutils.java.reflection.RefObject;
import net.codersky.skyutils.java.strings.SkyStrings;
import net.codersky.skyutils.spigot.cmd.AdaptedSpigotCommand;
import net.codersky.skyutils.spigot.cmd.SpigotCommand;
import net.codersky.skyutils.spigot.player.SpigotPlayerProvider;
import net.codersky.skyutils.spigot.player.SpigotPlayerQuitListener;
import net.codersky.skyutils.spigot.worldgen.SingleBiomeProvider;
import net.codersky.skyutils.spigot.worldgen.VoidGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SpigotUtils<P extends JavaPlugin> extends ServerUtils<P> {

	private final SpigotConsole console;
	private SpigotPlayerProvider playerProvider = new SpigotPlayerProvider();
	private boolean isPlayerListenerOn = false;

	public SpigotUtils(@NotNull P plugin) {
		super(plugin);
		this.console = new SpigotConsole(Bukkit.getConsoleSender());
	}

	@NotNull
	@Override
	public File getDataFolder() {
		return getPlugin().getDataFolder();
	}

	@NotNull
	public SpigotUtils<P> setPlayerProvider(@NotNull SpigotPlayerProvider playerProvider) {
		this.playerProvider = Objects.requireNonNull(playerProvider, "Player provider cannot be null");
		return this;
	}

	/**
	 * Gets the {@link SpigotPlayerProvider} being used by this {@link SpigotUtils} instance.
	 * 
	 * @return The {@link SpigotPlayerProvider} being used by this {@link SpigotUtils} instance.
	 *
	 * @throws IllegalPluginAccessException If called before the plugin attempts to enable.
	 * This is because the very first time this method is called the {@link SpigotPlayerQuitListener}
	 * will be registered. This is required just to clean players that quit the server from the cache.
	 *
	 * @since SkyUtils 1.0.0
	 * 
	 * @see #setPlayerProvider(SpigotPlayerProvider)
	 * @see #getPlayer(UUID)
	 * @see #getPlayer(Player)
	 */
	@NotNull
	public SpigotPlayerProvider getPlayerProvider() {
		if (!isPlayerListenerOn) {
			Bukkit.getPluginManager().registerEvents(new SpigotPlayerQuitListener(this), getPlugin());
			isPlayerListenerOn = true;
		}
		return playerProvider;
	}

	/**
	 * Gets an {@link MCPlayer} by {@link UUID} from the
	 * {@link #getPlayerProvider() SpigotPlayerProvider}
	 * that this {@link SpigotUtils} is using.
	 *
	 * @param uuid The {@link UUID} of the player to get.
	 *
	 * @throws IllegalPluginAccessException If called before the plugin
	 * attempts to enable, see {@link #getPlayerProvider()} for details.
	 *
	 * @return A possibly {@code null} {@link MCPlayer} instance of an online
	 * {@link Player} that matches the provided {@link UUID}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #getPlayerProvider()
	 * @see #getPlayer(Player)
	 */
	@Nullable
	@Override
	public MCPlayer getPlayer(@NotNull UUID uuid) {
		return getPlayerProvider().getPlayer(uuid);
	}

	/**
	 * Gets an {@link MCPlayer} by {@link Player} from the
	 * {@link #getPlayerProvider() SpigotPlayerProvider}
	 * that this {@link SpigotUtils} is using.
	 *
	 * @param bukkit The {@link Player} instance to convert.
	 *
	 * @throws IllegalPluginAccessException If called before the plugin
	 * attempts to enable, see {@link #getPlayerProvider()} for details.
	 *
	 * @return A {@link MCPlayer} instance that matches the provided {@link Player}.
	 * This can be {@code null} if you use an instance of a {@link Player} that
	 * is not {@link Player#isOnline() online}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see #getPlayerProvider()
	 * @see #getPlayer(Player)
	 */
	@Nullable
	public MCPlayer getPlayer(@NotNull Player bukkit) {
		return getPlayerProvider().getPlayer(bukkit);
	}

	@NotNull
	@Override
	public SpigotConsole getConsole() {
		return console;
	}

	@Override
	public @NotNull MCPlatform getPlatform() {
		return MCPlatform.SPIGOT;
	}

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
	protected SimpleCommandMap getCommandMap() {
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

	@Override
	@SuppressWarnings("unchecked")
	public void registerCommands(@NotNull GlobalCommand<P>... commands) {
		if (commands == null || commands.length == 0)
			return;
		registerCommands(SkyCollections.map(
				commands,
				new AdaptedSpigotCommand[commands.length],
				cmd -> new AdaptedSpigotCommand<>(this, cmd))
		);
	}

	/**
	 * Registers the specified {@code commands}, allowing them to be executed.
	 * <p>
	 * <b>Important note</b>: SkyUtils registers commands in a pretty unusual
	 * way. If the name of the command you are trying to register is present
	 * on your <b>plugin.yml</b>, SkyUtils will just register it the "traditional"
	 * way, if not, it will use some reflection to register it through CraftBukkit's
	 * getCommandMap (Respecting encapsulation!), meaning that you can register commands without adding them
	 * to your <b>plugin.yml</b>. <i>However</i>, this may break on future versions
	 * of the game
	 * <p>
	 * The reflection approach may break on future versions, even though this is pretty unlikely (For Spigot at least),
	 * we are using a <b>public</b> method present on <b>CraftServer</b>, not the private commandMap field that
	 * some plugins use. Paper has deprecated SimpleCommandMap for removal as of 1.20 though, so we may need
	 * to make a check there if they were to change it, but we are aware of it, so no worries. Just
	 * make sure to update SkyUtils if that ever happens (This will be notified as an important update).
	 *
	 * @param commands the list of {@link MCCommand commands} to register.
	 *
	 * @throws ClassCastException if any of the passed {@code commands} isn't an instance of {@link SpigotCommand}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@SuppressWarnings({"unchecked"})
	public void registerCommands(SpigotCommand<P>... commands) {
		if (commands == null || commands.length == 0)
			return;
		final List<Command> remaining = new ArrayList<>();
		for (SpigotCommand<P> command : commands) {
			final PluginCommand plCommand = getPlugin().getCommand(command.getName());
			if (plCommand != null) {
				plCommand.setExecutor(command);
				plCommand.setTabCompleter(command);
			} else
				remaining.add(command);
		}
		if (remaining.isEmpty())
			return;
		final SimpleCommandMap commandMap = getCommandMap();
		if (commandMap == null)
			Bukkit.getPluginManager().disablePlugin(getPlugin());
		else
			commandMap.registerAll(getPlugin().getName(), remaining);
	}

	/**
	 * Unregisters a command by {@code name}. In order to unregister
	 * a command, the command must have been registered by this
	 * plugin, as {@link JavaPlugin#getCommand(String)} is used in order to
	 * get the command to unregister.
	 * <p>
	 * <b>Note</b>: This method uses reflection and may be considered
	 * unsafe by some developers as it breaks the encapsulation principle
	 * by accessing the internal {@link SimpleCommandMap}
	 *
	 * @param name the name of the command to unregister, using
	 * aliases won't work.
	 *
	 * @return {@code true} if the command exists, was registered and
	 * has been unregistered successfully, {@code false} otherwise.
	 */
	public boolean unregisterCommand(@NotNull String name) {
		final PluginCommand plCommand = getPlugin().getCommand(name);
		final SimpleCommandMap commandMap = getCommandMap();
		return plCommand != null && commandMap != null && plCommand.unregister(commandMap);
	}

	/*
	 * World creation
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
