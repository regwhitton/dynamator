package app.storage.impl;

import static util.Verify.argNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import app.storage.ProjectStore;
import app.storage.ProjectStoreManager;
import app.storage.impl.project.ProjectStoreProvider;

class ProjectStoreManagerImpl implements ProjectStoreManager {

	private static final String APP_HOME_DIR = ".dynamator";

	private AppProperties appProperties;

	@Override
	public Path projectsHomeDirectory() throws IOException {
		return findOrCreateProjectsHomeDirectory();
	}

	@Override
	public ProjectStore createProjectStore(Path projectDirectory) throws IOException {
		argNotNull(projectDirectory);

		ProjectStore store = new ProjectStoreProvider(projectDirectory).get();
		store.create();

		recordLastProject(projectDirectory);
		return store;
	}

	@Override
	public ProjectStore openProjectStore(Path projectDirectory) throws IOException {
		argNotNull(projectDirectory);

		ProjectStore store = new ProjectStoreProvider(projectDirectory).get();
		store.open();

		recordLastProject(projectDirectory);
		return store;
	}

	@Override
	public Optional<Path> lastProjectPath() throws IOException {
		findOrCreateProjectsHomeDirectory();
		return appProperties.getLastProjectPath().map(p -> Paths.get(p));
	}

	private void recordLastProject(Path lastProjectPath) {
		appProperties.setLastProjectPath(lastProjectPath.toString());
	}

	private Path findOrCreateProjectsHomeDirectory() throws IOException {
		Path projectsDir = findHomeDir().resolve(APP_HOME_DIR);
		if (Files.notExists(projectsDir)) {
			Files.createDirectory(projectsDir);
		}
		validateWritable(projectsDir);
		appProperties = new AppProperties(projectsDir);
		appProperties.initialise();
		return projectsDir;
	}

	private Path findHomeDir() {
		Path homeDir = Paths.get(System.getProperty("user.home"));
		if (Files.notExists(homeDir)) {
			new StorageException("home directory \"" + homeDir + "\" does not exist");
		}
		return homeDir;
	}

	private void validateWritable(Path dir) {
		if (!Files.isWritable(dir)) {
			new StorageException("directory \"" + dir + "\" is not writable");
		}
	}
}
