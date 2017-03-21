package app.storage.impl.project;

import static util.Verify.argNotNull;

import java.nio.file.Path;

import app.storage.ProjectStore;
import provider.Commissioner;
import provider.Decommissioner;
import provider.Provider;
import provider.util.Commissioners;
import provider.util.Decommissioners;

public class ProjectStoreProvider implements Provider<ProjectStore> {

	private final Path projectDirectory;

	public ProjectStoreProvider(Path projectDirectory) {
		this.projectDirectory = argNotNull(projectDirectory);
	}

	@Override
	public ProjectStore get() {
		return new ProjectStoreImpl(new FrameStore(new BufferedImageConverter(), projectDirectory),
				new ProjectProperties(projectDirectory), projectDirectory);
	}

	@Override
	public Commissioner getCommissioner(ProjectStore projectStore) {
		return Commissioners.nullCommissioner();
	}

	@Override
	public Decommissioner getDecommissioner(ProjectStore projectStore) {
		return Decommissioners.nullDecommissioner();
	}
}
