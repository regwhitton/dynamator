package app.impl;

import static util.Verify.argNotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import app.App;
import app.CameraConfiguration;
import app.Project;
import app.camera.CameraAccess;
import app.storage.ProjectStore;
import app.storage.ProjectStoreManager;
import type.Seq;

class AppImpl implements App {

	private final CameraAccess cameraAccess;
	private final ProjectStoreManager projectStoreManager;
	private final ProjectFactory projectFactory;

	public AppImpl(CameraAccess cameraAccess, ProjectStoreManager projectStoreManager, ProjectFactory projectFactory) {
		this.cameraAccess = argNotNull(cameraAccess);
		this.projectStoreManager = argNotNull(projectStoreManager);
		this.projectFactory = argNotNull(projectFactory);
	}

	@Override
	public Optional<Project> openLastProject() throws IOException {
		Optional<Path> lastPath = projectStoreManager.lastProjectPath();
		return lastPath.isPresent() ? Optional.of(openProject(lastPath.get())) : Optional.empty();
	}

	@Override
	public Path projectsHomeDirectory() throws IOException {
		return projectStoreManager.projectsHomeDirectory();
	}


	@Override
	public Seq<CameraConfiguration> getCameraConfigurations() {
		return cameraAccess.findCameraConfigurations();
	}

	@Override
	public void setCameraConfiguration(CameraConfiguration cameraConfiguration) {
		argNotNull(cameraConfiguration);
		cameraAccess.setCameraConfiguration(cameraConfiguration);
	}

	@Override
	public Project createProject(Path projectDir) throws IOException {
		argNotNull(projectDir);
		return createProject(projectStoreManager.createProjectStore(projectDir));
	}

	@Override
	public Project openProject(Path projectDir) throws IOException {
		argNotNull(projectDir);
		return createProject(projectStoreManager.openProjectStore(projectDir));
	}

	private Project createProject(ProjectStore projectStore) {
		// TODO - open camera on opening project (or maybe on first use).
		ProjectImpl project = projectFactory.createProject(projectStore);
		project.open();
		return project;
	}
}
