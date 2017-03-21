package app.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;

import type.Image;

public interface ProjectStore {

	/**
	 * Create new {@link ProjectStore}.
	 */
	void create() throws IOException;

	/**
	 * Open and read details from the existing {@link ProjectStore}.
	 */
	void open() throws IOException;

	/**
	 * @return the number of frames in the store
	 */
	int size();

	/**
	 * Append a frame to the store.
	 */
	void append(Image frame);

	/**
	 * @return the current project directory.
	 */
	Path getProjectDirectory();

	/**
	 * @return an iterator of frames in the project.
	 */
	Iterator<Image> iterator();

	/**
	 * @return the last frame in the project (if one).
	 */
	Optional<Image> lastImage();
	
	/**
	 * Change the number of frames per second.
	 */
	void setFps(int fps);

	/**
	 * Get the current number of frames per second.
	 */
	int getFps();
}
