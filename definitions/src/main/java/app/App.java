package app;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import type.Seq;

/**
 * The business logic application.
 */
public interface App {

	/**
	 * Re-opens the last project (if one).
	 */
	Optional<Project> openLastProject() throws IOException;

	/**
	 * @return the directory where new projects should be created in by default.
	 */
	Path projectsHomeDirectory() throws IOException;

	/**
	 * @return a list of available camera devices and size formats they support.
	 */
	Seq<CameraConfiguration> getCameraConfigurations();

	/**
	 * Changes the camera configuration to use with the current project.
	 */
	void setCameraConfiguration(CameraConfiguration cameraConfiguration);

	/**
	 * Creates and opens a new project.
	 */
	Project createProject(Path projectDirectoryPath) throws IOException;

	/**
	 * Opens a project.
	 */
	Project openProject(Path projectDirectoryPath) throws IOException;
}
