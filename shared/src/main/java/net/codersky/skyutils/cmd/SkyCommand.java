package net.codersky.skyutils.cmd;

import net.codersky.jsky.JNumbers;
import net.codersky.jsky.collections.JCollections;
import net.codersky.skyutils.SkyUtils;
import net.codersky.skyutils.crossplatform.player.SkyPlayer;
import net.codersky.skyutils.java.strings.SkyStrings;
import net.codersky.skyutils.files.MessagesFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Interface used to represent a cross-platform command.
 * Commands can directly be made and implemented with this interface,
 * though the {@link GlobalCommand} class is easier to use if you
 * intend to make cross-platform commands.
 *
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 *
 * @param <P> The plugin class that owns this {@link SkyCommand}.
 * @param <S> The {@link SkyCommandSender} type of this {@link SkyCommand}.
 */
public interface SkyCommand<P, S extends SkyCommandSender> {

	/*
	 - Command information
	 */

	/**
	 * Gets the name of this  {@link SkyCommand}, which is used in order
	 * to execute it as "/name".
	 *
	 * @return The name of this {@link SkyCommand}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	String getName();

	/**
	 * Gets the {@link List} of aliases that this {@link SkyCommand command}
	 * uses other than its {@link #getName() name}. This {@link List} may
	 * be {@link List#isEmpty() empty} if the {@link SkyCommand command} has
	 * no aliases, but it must never be {@code null}.
	 *
	 * @return The {@link List} of aliases that this {@link SkyCommand command}
	 * uses.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	List<String> getAliases();

	/**
	 * Checks whether this {@link SkyCommand} matches the specified {@code name}.
	 * A command is considered to match when its {@link #getName() name} or any
	 * of its {@link #getAliases() aliases} {@link String#equalsIgnoreCase(String) equal}
	 * (Case insensitive) the provided {@code name}.
	 *
	 * @param name The name to check.
	 *
	 * @return {@code true} if this {@link SkyCommand} matches the provided
	 * {@code name}, {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean matches(@NotNull String name) {
		return name.equalsIgnoreCase(getName()) || JCollections.contains(getAliases(), alias ->
				alias.equalsIgnoreCase(name));
	}

	/*
	 - Utilities
	 */

	/**
	 * Gets the {@link SkyUtils utils} instance that this {@link SkyCommand command}
	 * is using. Generally, this instance is provided at the constructor of each
	 * {@link SkyCommand command} type.
	 *
	 * @return The {@link SkyUtils utils} instance that this {@link SkyCommand command}
	 * is using.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	SkyUtils<P> getUtils();

	/**
	 * Gets the {@link P plugin} that owns this {@link SkyCommand command}.
	 * This {@link P plugin} is, by default, obtained from
	 * the {@link #getUtils() utils}.
	 *
	 * @return The {@link P plugin} that owns this {@link SkyCommand command}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	default P getPlugin() {
		return getUtils().getPlugin();
	}

	/*
	 - Command execution
	 */

	/**
	 * Method that is called whenever this {@link SkyCommand command} is executed. This
	 * is generally controlled by a {@link SubCommandHandler} and not by the
	 * {@link SkyCommand command} itself.
	 *
	 * @param sender The {@link S sender} that executed this {@link SkyCommand command}.
	 * @param args The arguments used on the execution.
	 *
	 * @return Generally {@code true} if the {@link SkyCommand command} has executed correctly.
	 * Behavior when returning {@code false} depends on the platform that the {@link SkyCommand command}
	 * is being executed on, for that reason, the recommendation is to always return {@code true} and
	 * send custom messages to the {@code sender} in case of error by using a {@link MessagesFile}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	boolean onCommand(@NotNull S sender, @NotNull String[] args);

	/*
	 - Tab complete
	 */

	/**
	 * Method that is called whenever this {@link SkyCommand command} is tab completed. This
	 * is generally controlled by a {@link SubCommandHandler} and not by the
	 * {@link SkyCommand command} itself.
	 * <p>
	 * Note that you don't need to worry about sub commands being suggested or about suggestions
	 * being filtered depending on what's already written on the argument. This is all handled
	 * by the {@link SubCommandHandler} that all {@link SkyCommand command} types from SkyUtils use.
	 *
	 * @param sender The {@link S sender} that tab completed this {@link SkyCommand command}.
	 * @param args The arguments provided up to this point.
	 *
	 * @return A {@link Nullable} {@link List} of suggestions to send to the {@code S sender}.
	 * In case of the {@link List} being {@code null}, an empty {@link List} is actually sent
	 * to the {@link SubCommandHandler}. The returned {@link List} can be unmodifiable, as it
	 * gets cloned to include any possible sub commands as suggestions.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	List<String> onTab(@NotNull S sender, @NotNull String[] args);

	/*
	 - SubCommand injection
	 */

	/**
	 * Injects the provided {@code commands} as sub commands of this {@link SkyCommand command}.
	 * Injection must be handled by a {@link SubCommandHandler} with the already provided
	 * {@link SubCommandHandler#inject(SkyCommand[])} method.
	 *
	 * @param commands The {@link SkyCommand commands} to inject.
	 *
	 * @return This {@link SkyCommand command}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	SkyCommand<P, S> inject(@NotNull SkyCommand<P, S>... commands);

	/**
	 * Returns the {@link HashSet set} of sub commands that have been
	 * {@link #inject(SkyCommand[]) injected} to this {@link SkyCommand}.
	 * <p>
	 * Implementations must return a clone of the internal {@link HashSet set}
	 * to avoid accidental modifications. This is already provided by the
	 * {@link SubCommandHandler#getSubCommands()} method.
	 *
	 * @return The {@link HashSet set} of sub commands that have been
	 * {@link #inject(SkyCommand[]) injected} to this {@link SkyCommand}.
	 * May be empty if no sub commands have been injected yet.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	HashSet<SkyCommand<P, S>> getSubCommands();

	/*
	 - Access check
	 */

	/**
	 * Checks if the provided {@link S sender} has access to this {@link SkyCommand command}.
	 * This is taken into account by the {@link SubCommandHandler} of this {@link SkyCommand command}
	 * To cancel {@link #onCommand(SkyCommandSender, String[]) execution} or {@link
	 * #onTab(SkyCommandSender, String[]) tab complete} if {@code false} is returned.
	 * <p>
	 * This method just returns {@code true} by default and exists for you to
	 * specify custom access rules for commands such as required permissions.
	 * <p>
	 * You are expected to send a custom no permission message to the {@code sender}
	 * if {@code message} is {@code true}, otherwise, no message is sent whatsoever.
	 *
	 * @param sender The {@link S sender} to check.
	 * @param message Whether a message should be sent to the {@code sender} or not. This is
	 * actually determined by the {@link SubCommandHandler} and will be {@code true} on
	 * command execution and {@code false} on tab complete.
	 *
	 * @return {@code true} to allow access to the {@link SkyCommand command}, {@code false}
	 * to deny access to it.
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean hasAccess(@NotNull S sender, boolean message) {
		return true;
	}

	/*
	 - Utility
	 */

	/**
	 * Utility method to get a {@link List} with the names of all current <b>online</b> players.
	 * This is generally used on {@link #onTab(SkyCommandSender, String[]) tab complete}.
	 *
	 * @return A {@link List} with the names of all current <b>online</b> players.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	default List<String> getOnlineNames() {
		return getUtils().getOnlinePlayers().stream().map(SkyPlayer::getName).toList();
	}

	/*
	 - Argument conversion - Event pattern removal
	 */

	/**
	 * Returns whether this {@link SkyCommand} removes
	 * <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a>
	 * from string getters or not. This is enabled by default and it is recommended.
	 * <p>
	 * Keep in mind that this doesn't modify the {@code args} {@link String} array from the
	 * {@link #onCommand(SkyCommandSender, String[])} and {@link #onTab(SkyCommandSender, String[])}
	 * methods but instead affects string getter methods such as {@link #asString(int, String[])},
	 * methods that convert arguments to other objects such as {@link #asNumber(int, String[], Class)}
	 * remain unaffected because they don't have this issue.
	 *
	 * @return {@code true} if this {@link SkyCommand} removes
	 * <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a>
	 * from string getters, {@code false} otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 */
	default boolean removesEventPatterns() {
		return true;
	}

	/*
	 - Argument conversion - Generic
	 */

	/**
	 * Converts the specified {@code arg} of the {@code args} array to any object by using the
	 * {@code converter} {@link Function}. Returning {@code def} if no argument is found at the
	 * {@code arg} position or if {@code converter} returns {@code null}.
	 *
	 * @param <T> The return type of the {@code converter} {@link Function}.
	 *
	 * @param converter the {@link Function} that will convert the {@link String}
	 * found at the specified {@code arg} position. The {@link String} passed
	 * to the {@link Function} will <b>never</b> be {@code null}.
	 * @param arg The array position of the argument to get, can be out of bounds.
	 * @param args The array of arguments to use.
	 * @param def The default value to return if {@code arg} is out of bounds or
	 * {@code converter} returns {@code null}.
	 *
	 * @return The argument converted by {@code converter} if found on the {@code args} array
	 * and {@code converter} doesn't return {@code null}. {@code def} otherwise.
	 *
	 * @throws NullPointerException if {@code args}, {@code converter} or {@code def} are {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	default <T> T asGeneric(@NotNull Function<String, T> converter, int arg, @NotNull String[] args, @NotNull T def) {
		Objects.requireNonNull(def, "def cannot be null. Remove the parameter instead.");
		if (args.length <= arg)
			return def;
		final T converted = converter.apply(args[arg]);
		return converted == null ? def : converted;
	}

	/**
	 * Converts the specified {@code arg} of the {@code args} array to any object by using the
	 * {@code converter} {@link Function}. Returning {@code null} if no argument is found at the
	 * {@code arg} position or if {@code converter} returns {@code null}.
	 *
	 * @param <T> The return type of the {@code converter} {@link Function}.
	 *
	 * @param converter The {@link Function} that will convert the {@link String}
	 * found at the specified {@code arg} position. The {@link String} passed
	 * to the {@link Function} will <b>never</b> be {@code null}.
	 * @param arg The array position of the argument to get, can be out of bounds.
	 * @param args The array of arguments to use.
	 *
	 * @return The argument as converted by {@code converter} if found
	 * on the {@code args} array, {@code null} otherwise.
	 *
	 * @throws NullPointerException If {@code args} or {@code converter} are {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	default <T> T asGeneric(@NotNull Function<String, T> converter, int arg, @NotNull String[] args) {
		return args.length > arg ? converter.apply(args[arg]) : null;
	}

	/*
	 - Argument conversion - Strings
	 */

	// NOTE: Outdated documentation below.

	// Regular //

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link String}, this
	 * method won't do any actual conversion and will just return the argument if found, <b>def</b> if not.
	 *
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>arg</b> is out of bounds.
	 *
	 * @return The argument as a {@link String} if found on the <b>args</b> array, <b>def</b> otherwise.
	 *
	 * @throws NullPointerException if <b>args</b> is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	default String asString(int arg, @NotNull String[] args, @NotNull String def) {
		Objects.requireNonNull(def, "def cannot be null. Remove the parameter instead.");
		final String result = args.length > arg ? args[arg] : def;
		return removesEventPatterns() ? SkyStrings.stripEventPatterns(result) : result;
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link String}, this
	 * method won't do any actual conversion and will just return the argument if found, null if not.
	 *
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 *
	 * @return The argument as a {@link String} if found on the <b>args</b> array, null otherwise.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	default String asString(int arg, @NotNull String[] args) {
		final String result = args.length > arg ? args[arg] : null;
		return result != null && removesEventPatterns() ? SkyStrings.stripEventPatterns(result) : result;
	}

	// With modifier //

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link String}, this
	 * method will apply <b>modifier</b> to the argument only if one is found and <b>def</b> isn't {@code null}.
	 * If no argument is found on the <b>arg</b> position and <b>def</b> is {@code null},
	 * {@code null} will be returned and the <b>modifier</b> won't be applied.
	 *
	 * @param modifier A {@link Function} to modify the resulting {@link String} that will be applied
	 * only if the {@link String} is not {@code null}.
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>arg</b> is out of bounds.
	 *
	 * @return The argument as a {@link String} if found on the <b>args</b> array, {@code def} otherwise.
	 *
	 * @throws NullPointerException if <b>modifier</b> is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	default String asString(@NotNull Function<String, String> modifier, int arg, @NotNull String[] args, @NotNull String def) {
		return modifier.apply(asString(arg, args, def));
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link String}, this
	 * method will apply <b>modifier</b> to the argument only if one is found, if no argument is found on
	 * the <b>arg</b> position, {@code null} will be returned and the <b>modifier</b> won't be applied.
	 *
	 * @param modifier A {@link Function} to modify the resulting {@link String} that will be applied
	 * only if the {@link String} is not {@code null}.
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 *
	 * @return The argument as a {@link String} if found on the <b>args</b> array, null otherwise.
	 *
	 * @throws NullPointerException if <b>modifier</b> is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	default String asString(@NotNull Function<String, String> modifier, int arg, @NotNull String[] args) {
		final String result = asString(arg, args);
		return result == null ? null : modifier.apply(result);
	}

	/*
	 - Argument conversion - String ranges
	 */

	@NotNull
	private String buildRange(@Nullable Function<String, String> modifier, int fromArg, @NotNull String[] args, @NotNull String first) {
		final StringBuilder builder = new StringBuilder(first);
		final boolean removeEvents = removesEventPatterns();
		Function<String, String> finalModifier = null;
		if (modifier == null && removeEvents)
			finalModifier = SkyStrings::stripEventPatterns;
		else if (modifier != null)
			finalModifier = str -> modifier.apply(SkyStrings.stripEventPatterns(str));
		for (int i = fromArg + 1; i < args.length; i++)
			builder.append(' ').append(finalModifier == null ? args[i] : finalModifier.apply(args[i]));
		return builder.toString();
	}

	/**
	 * Gets a range of arguments starting at <b>fromArg</b> all the way to
	 * the end of <b>args</b>, then, converts that range of arguments to a
	 * {@link String}, separating them by a space character, exactly as typed
	 * by the user that ran the command.
	 *
	 * @param fromArg the array position of the first argument to get a range from, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>fromArg</b> is out of bounds.
	 *
	 * @return A {@link String} of the range of arguments from <b>fromArg</b> to the end of <b>args</b>,
	 * <b>def</b> if <b>fromArg</b> is out of bounds.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	default String asStringRange(int fromArg, @NotNull String[] args, @NotNull String def) {
		final String first = asString(fromArg, args);
		return first == null ? def : buildRange(null, fromArg, args, first);
	}

	/**
	 * Gets a range of arguments starting at <b>fromArg</b> all the way to
	 * the end of <b>args</b>, then, converts that range of arguments to a
	 * {@link String}, separating them by a space character, exactly as typed
	 * by the user that ran the command.
	 *
	 * @param fromArg the array position of the first argument to get a range from, can be out of bounds.
	 * @param args the array of arguments to use.
	 *
	 * @return A {@link String} of the range of arguments from <b>fromArg</b> to the end of <b>args</b>,
	 * null if <b>fromArg</b> is out of bounds.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	default String asStringRange(int fromArg, @NotNull String[] args) {
		final String first = asString(fromArg, args);
		return first == null ? null : buildRange(null, fromArg, args, first);
	}

	/**
	 * Gets a range of arguments starting at <b>fromArg</b> all the way to
	 * the end of <b>args</b>, then, converts that range of arguments to a
	 * {@link String}, separating them by a space character, exactly as typed
	 * by the user that ran the command. If the range isn't out of bounds, then
	 * <b>modifier</b> will be applied to the resulting {@link String}.
	 *
	 * @param modifier the {@link Function} to apply to the resulting {@link String}. This will not be applied
	 * on <b>def</b> upon returning it.
	 * @param fromArg the array position of the first argument to get a range from, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>fromArg</b> is out of bounds.
	 *
	 * @return A {@link String} of the range of arguments from <b>fromArg</b> to the end of <b>args</b>,
	 * <b>def</b> if <b>fromArg</b> is out of bounds.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	default String asStringRange(@NotNull Function<String, String> modifier, int fromArg, @NotNull String[] args, @NotNull String def) {
		final String first = asString(fromArg, args);
		return first == null ? def : buildRange(modifier, fromArg, args, first);
	}

	/**
	 * Gets a range of arguments starting at <b>fromArg</b> all the way to
	 * the end of <b>args</b>, then, converts that range of arguments to a
	 * {@link String}, separating them by a space character, exactly as typed
	 * by the user that ran the command. If the range isn't out of bounds, then
	 * <b>modifier</b> will be applied to the resulting {@link String}.
	 *
	 * @param modifier the {@link Function} to apply to the resulting {@link String}.
	 * @param fromArg the array position of the first argument to get a range from, can be out of bounds.
	 * @param args the array of arguments to use.
	 *
	 * @return A {@link String} of the range of arguments from <b>fromArg</b> to the end of <b>args</b>,
	 * {@code null} if <b>fromArg</b> is out of bounds.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	default String asStringRange(@NotNull Function<String, String> modifier, int fromArg, @NotNull String[] args) {
		final String first = asString(fromArg, args);
		return first == null ? null : buildRange(modifier, fromArg, args, first);
	}

	/*
	 - Argument conversion - List ranges
	 */

	@Nullable
	default <T> List<T> asListRange(@NotNull Function<String, T> modifier, int fromArg, @NotNull String[] args, @Nullable List<T> def) {
		if (fromArg > args.length)
			return def;
		final List<T> lst = new ArrayList<>(args.length - fromArg);
		for (int i = fromArg + 1; i < args.length; i++)
			lst.add(modifier.apply(args[i]));
		return lst;
	}

	@Nullable
	default <T> List<T> asListRange(@NotNull Function<String, T> modifier, int fromArg, @NotNull String[] args) {
		return asListRange(modifier, fromArg, args, null);
	}

	@Nullable
	default List<String> asStringListRange(int fromArg, @NotNull String[] args, @Nullable List<String> def) {
		if (fromArg > args.length)
			return def;
		final List<String> lst = new ArrayList<>(args.length - fromArg);
		for (int i = fromArg + 1; i < args.length; i++)
			lst.add(SkyStrings.stripEventPatterns(args[i]));
		return lst;
	}

	@Nullable
	default List<String> asStringListRange(int fromArg, @NotNull String[] args) {
		return asStringListRange(fromArg, args, null);
	}

	/*
	 - Argument conversion - Numbers
	 */

	/**
	 * Converts the specified {@code arg} of the {@code args} array to a {@link Number}
	 * (See {@link JNumbers#asNumber(CharSequence, Number)} for more details).
	 *
	 * @param <T> The type of {@link Number} to return.
	 * @param arg The array position of the argument to get, can be out of bounds.
	 * @param args The array of arguments to use.
	 * @param def The default value to return if {@code arg} is out of bounds or the argument isn't a valid number.
	 *
	 * @return The argument as a {@link Number} if found on the {@code args} array, {@code def} otherwise.
	 *
	 * @throws NullPointerException If {@code args} or {@code def} is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see JNumbers#asNumber(CharSequence, Number)
	 */
	@NotNull
	default <T extends Number> T asNumber(int arg, @NotNull String[] args, @NotNull T def) {
		return asGeneric(str -> JNumbers.asNumber(str, def), arg, args, def);
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link Number}
	 * (See {@link JNumbers#asNumber(CharSequence, Class)} for more details).
	 *
	 * @param <T> the type of {@link Number} to return.
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 *
	 * @return The argument as an {@link Number} if found on the <b>args</b> array, null otherwise.
	 *
	 * @throws NullPointerException If {@code args} or {@code type} is {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 *
	 * @see JNumbers#asNumber(CharSequence, Class)
	 */
	@Nullable
	default <T extends Number> T asNumber(int arg, @NotNull String[] args, @NotNull Class<T> type) {
		return asGeneric(str -> JNumbers.asNumber(str, type), arg, args);
	}

	/*
	 - Argument conversion - Enums
	 */

	/**
	 * Converts the specified {@code arg} of the {@code args} array to an {@link Enum}.
	 * <p>
	 * The argument is converted {@link String#toUpperCase() to upper case} as enum
	 * constants must be upper case, so you don't have to check if the argument is
	 * upper case or not.
	 *
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param enumClass the class of the {@link Enum} to get the constant from.
	 *
	 * @return The argument as an {@link Enum} if found on the {@code args} array, null otherwise.
	 *
	 * @throws NullPointerException if {@code args} or {@code enumClass} are {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@Nullable
	default <T extends Enum<T>> T asEnum(int arg, @NotNull String[] args, @NotNull Class<T> enumClass) {
		// TODO Maybe find a better way to do this...
		return asGeneric(str -> {
			try {
				return Enum.valueOf(enumClass, str);
			} catch (IllegalArgumentException ex) {
				return null;
			}
		}, arg, args);
	}

	/**
	 * Converts the specified {@code arg} of the {@code args} array to an {@link Enum}.
	 * <p>
	 * The argument is converted {@link String#toUpperCase() to upper case} as enum
	 * constants must be upper case, so you don't have to check if the argument is
	 * upper case or not.
	 *
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if {@code arg} is out of bounds or the argument
	 * isn't a valid enum constant of the same class.
	 *
	 * @return The argument as an {@link Enum} if found on the {@code args} array, {@code def} otherwise.
	 *
	 * @throws NullPointerException if {@code args} or {@code def} are {@code null}.
	 *
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	default <T extends Enum<T>> T asEnum(int arg, @NotNull String[] args, @NotNull T def) {
		return asGeneric(str -> {
			final String name = str.toUpperCase();
			for (T constant : def.getDeclaringClass().getEnumConstants())
				if (name.equals(str))
					return constant;
			return null;
		}, arg, args, def);
	}
}
