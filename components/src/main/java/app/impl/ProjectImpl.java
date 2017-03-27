package app.impl;

import static util.Verify.argNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import app.FrameSizeMismatchException;
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
	public void captureImage() throws FrameSizeMismatchException {
		Optional<Image> frame = cameraAccess.getImage();
		if(frame.isPresent()){
			addFrameToProject(frame.get());
		}
	}

	private void addFrameToProject(Image frame) throws FrameSizeMismatchException {
		if (projectHasNoFrameSizeYet()) {
			projectStore.setFrameSize(frame.getSize());
		} else {
			ensureFrameSizeMatchesProject(frame);
		}
		projectStore.append(frame);
		updateCurFrameOutline(frame);
		curFrameOutline = Optional.of(imageTransformer.toOutline(frame));
	}

	private boolean projectHasNoFrameSizeYet() {
		return !projectStore.getFrameSize().isPresent();
	}

	private void ensureFrameSizeMatchesProject(Image frame) throws FrameSizeMismatchException {
		if (!frameSizeMatchesProject(frame)) {
			throw new FrameSizeMismatchException(projectStore.getFrameSize().get());
		}
	}

	private void updateCurFrameOutline(Image frame) {
		curFrameOutline = Optional.of(imageTransformer.toOutline(frame));
	}

	@Override
	public Optional<Image> previewImage() {
		return cameraAccess.getImage().map(cameraImage -> buildPreviewImage(cameraImage));
	}

	private Image buildPreviewImage(Image cameraImage) {
		return curFrameOutline.map(outline -> frameSizesMatch(cameraImage, outline)
				? combineWithCurFrameOutline(cameraImage) : cameraImage).orElse(cameraImage);
	}

	private boolean frameSizesMatch(Image frame1, Image frame2) {
		return frame1.getSize().equals(frame2.getSize());
	}

	private boolean frameSizeMatchesProject(Image frame) {
		return frame.getSize().equals(projectStore.getFrameSize().get());
	}

	private Image combineWithCurFrameOutline(Image frame) {
		return imageTransformer.overlay(frame, curFrameOutline.get());
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
