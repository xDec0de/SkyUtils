package net.codersky.skyutils.java;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Utility class containing methods related to {@link File files}.
 *
 * @since SkyUtils 1.0.0
 *
 * @author xDec0de_
 */
public class SkyFiles {

	/**
	 * Ensures a {@link File} is created, creating any necessary
	 * parent directories as well as the file itself if it doesn't
	 * already exist.
	 *
	 * @param file The {@link File} to create.
	 * @param err The {@link Consumer} that will accept any exceptions
	 * thrown by this method. Caught exceptions are only of type
	 * {@link IOException} or {@link SecurityException}.
	 *
	 * @return {@code true} if the {@code file} created successfully.
	 * {@code false} if the {@code file} already exists or if an error occurred.
	 *
	 * @since SkyUtils 1.0.0
	 */
	public static boolean create(@NotNull File file, @NotNull Consumer<Exception> err) {
		if (file.exists())
			return false;
		final File parent = file.getParentFile();
		if (parent != null)
			parent.mkdirs();
		try {
			file.createNewFile();
			return true;
		} catch (IOException | SecurityException e) {
			err.accept(e);
			return false;
		}
	}

	/**
	 * Ensures a {@link File} is created, creating any necessary
	 * parent directories as well as the file itself if it doesn't
	 * already exist.
	 * <p>
	 * Any exceptions produced will just be {@link Exception#printStackTrace() printed}.
	 * If you want to override this behaviour use {@link #create(File, Consumer)}.
	 *
	 * @param file The {@link File} to create.
	 *
	 * @return {@code true} if the {@code file} created successfully.
	 * {@code false} if the {@code file} already exists or if an error occurred.
	 *
	 * @since SkyUtils 1.0.0
	 */
	public static boolean create(@NotNull File file) {
		return create(file, Exception::printStackTrace);
	}
}
