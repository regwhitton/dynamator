package app.impl;

import static util.Verify.argNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import app.Project;
import app.camera.CameraAccess;
import app.imagetransformer.ImageTransformer;
import app.storage.ProjectStore;
import app.videoencoder.VideoEncoder;
import type.Image;

class ProjectImpl implements Project {

	private final ProjectStore projectStore;
	private final CameraAccess cameraAccess;
	private final ImageTransformer imageTransformer;
	private final VideoEncoder videoEncoder;

	private Optional<Image> curFrameOutline = Optional.empty();

	public ProjectImpl(ProjectStore projectStore, CameraAccess cameraAccess, ImageTransformer imageTransformer,
			VideoEncoder videoEncoder) {
		this.projectStore = argNotNull(projectStore);
		this.cameraAccess = argNotNull(cameraAccess);
		this.imageTransformer = argNotNull(imageTransformer);
		this.videoEncoder = argNotNull(videoEncoder);
	}

	void open() {
		curFrameOutline = projectStore.lastImage().map(img -> imageTransformer.toOutline(img));
	}

	@Override
	public Path getProjectDirectory() {
		return projectStore.getProjectDirectory();
	}

	@Override
	public void captureImage() {
		cameraAccess.getImage().ifPresent(frame -> {
			projectStore.append(frame);
			updateCurFrameOutline(frame);
		});
	
		/*-
		 TODO - If wrong size, then prompt user to say so, then ignore.
		Display expected size somewhere.
		*/
		// TODO -Check image is expected size. which would be fixed when project
		// created, or once images recorded.
		// Or we could scale to size (allows use of another computer). Perhaps
		// alert if changed - could just
		// not show outline to avoid processing problems.
	}

	private void updateCurFrameOutline(Image frame) {
		curFrameOutline = Optional.of(imageTransformer.toOutline(frame));
	}

	@Override
	public Optional<Image> previewImage() {
		return cameraAccess.getImage().map(frame -> combineWithCurFrameOutline(frame));
	}

	private Image combineWithCurFrameOutline(Image frame) {
		return curFrameOutline.map(outline -> imageTransformer.overlay(frame, outline)).orElse(frame);
	}

	@Override
	public void createVideo() {
		videoEncoder.write(outputFile(), projectStore.iterator());
	}

	private Path outputFile() {
		Path projectsDir = projectStore.getProjectDirectory();
		String projectName = projectsDir.getFileName() + ".avi";
		return Paths.get(projectsDir.toString(), projectName);
	}
}
