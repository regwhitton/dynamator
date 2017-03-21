package app.impl;

import static util.Verify.argNotNull;

import app.App;
import app.camera.CameraAccess;
import app.imagetransformer.ImageTransformer;
import app.storage.ProjectStoreManager;
import app.videoencoder.VideoEncoder;
import provider.Commissioner;
import provider.Decommissioner;
import provider.Provider;
import provider.util.Commissioners;
import provider.util.Decommissioners;

public class AppProvider implements Provider<App> {

	private final CameraAccess cameraAccess;
	private final ProjectStoreManager projectStoreManager;
	private final ProjectFactory projectFactory;

	public AppProvider(ProjectStoreManager projectStoreManager, CameraAccess cameraAccess,
			ImageTransformer imageTransformer, VideoEncoder videoEncoder) {
		this.cameraAccess = argNotNull(cameraAccess);
		this.projectStoreManager = argNotNull(projectStoreManager);
		this.projectFactory = new ProjectFactory(cameraAccess, argNotNull(imageTransformer), argNotNull(videoEncoder));
	}

	@Override
	public App get() {
		return new AppImpl(cameraAccess, projectStoreManager, projectFactory);
	}

	@Override
	public Commissioner getCommissioner(App app) {
		return Commissioners.nullCommissioner();
	}

	@Override
	public Decommissioner getDecommissioner(App app) {
		return Decommissioners.nullDecommissioner();
	}
}
