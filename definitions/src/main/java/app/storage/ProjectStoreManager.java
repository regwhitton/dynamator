package app.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface ProjectStoreManager {

	Path projectsHomeDirectory() throws IOException;
	
	ProjectStore createProjectStore(Path projectDirectory) throws IOException;
	
	ProjectStore openProjectStore(Path projectDirectory) throws IOException;

	Optional<Path> lastProjectPath() throws IOException;
}