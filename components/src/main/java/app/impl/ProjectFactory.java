package app.impl;

import static util.Verify.argNotNull;

import app.camera.CameraAccess;
import app.imagetransformer.ImageTransformer;
import app.storage.ProjectStore;
import app.videoencoder.VideoEncoder;

class ProjectFactory {

	private final CameraAccess cameraAccess;
	private final ImageTransformer imageTransformer;
	private final VideoEncoder videoEncoder;

	ProjectFactory(CameraAccess cameraAccess, ImageTransformer imageTransformer, VideoEncoder videoEncoder) {
		this.cameraAccess = argNotNull(cameraAccess);
		this.imageTransformer = argNotNull(imageTransformer);
		this.videoEncoder = argNotNull(videoEncoder);
	}

	ProjectImpl createProject(ProjectStore projectStore) {
		return new ProjectImpl(projectStore, cameraAccess, imageTransformer, videoEncoder);
	}
}
