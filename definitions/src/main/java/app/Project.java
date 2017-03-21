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
	 * Capture a new frame image and add it to the current project.
	 */
	void captureImage();

	/**
	 * Get a preview image to display to the user (if camera available).
	 */
	Optional<Image> previewImage();

	/**
	 * Create video from the captured frames.
	 */
	void createVideo();
}
