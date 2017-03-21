package app.storage.impl;

import app.storage.ProjectStoreManager;
import provider.Commissioner;
import provider.Decommissioner;
import provider.Provider;
import provider.util.Commissioners;
import provider.util.Decommissioners;

public class ProjectStoreManagerProvider implements Provider<ProjectStoreManager> {

	public ProjectStoreManager get() {
		return new ProjectStoreManagerImpl();
	}

	@Override
	public Commissioner getCommissioner(ProjectStoreManager projectStoreManager) {
		return Commissioners.nullCommissioner();
	}

	@Override
	public Decommissioner getDecommissioner(ProjectStoreManager projectStoreManager) {
		return Decommissioners.nullDecommissioner();
	}
}
