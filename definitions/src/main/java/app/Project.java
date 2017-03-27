package app;

import java.nio.file.Path;
import java.util.Optional;

import type.Image;

public interface Project {

	/**
	 * @return the directory where the project is stored.
	 */
	Path getProjectDirectory();

	/**
	 * If an image is available, capture it and add it to the project.
	 */
	void captureImage() throws FrameSizeMismatchException;

	/**
	 * Get a preview image to display to the user (if camera available).
	 */
	Optional<Image> previewImage();

	/**
	 * Create video from the captured frames.
	 */
	void createVideo();
}
