package jfx.ui.openproject;

import java.nio.file.Path;
import java.util.Optional;

public interface OpenProjectDialog {

	/**
	 * Implements {@link jfx.ui.Ui#promptForProjectDirectory(Path, Optional)}.
	 */
	Optional<Path> promptForProject(Path defaultParentLocation, Optional<Path> previousProjectDirectory);
}
