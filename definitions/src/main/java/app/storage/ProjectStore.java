package app.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;

import type.Image;
import type.Size;

public interface ProjectStore {

	/**
	 * Create the project directory and property file this {@link ProjectStore}
	 * will represent on disc.
	 */
	void create() throws IOException;

	/**
	 * Open the existing project directory and property file this
	 * {@link ProjectStore} represents on disc, and read in the details.
	 */
	void open() throws IOException;

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
	void setFramesPerSecond(int fps);

	/**
	 * Get the current number of frames per second.
	 */
	int getFramesPerSecond();

	/**
	 * Set the size of frame for this project.
	 */
	void setFrameSize(Size size);

	/**
	 * Get the size of frames in this project, if it has been set.
	 */
	Optional<Size> getFrameSize();
}
