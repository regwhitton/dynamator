package app.storage.impl.project;

import static util.Verify.argInRange;
import static util.Verify.argNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;

import app.storage.ProjectStore;
import type.Image;
import type.Size;

class ProjectStoreImpl implements ProjectStore {

	private final FrameStore frameStore;
	private final ProjectProperties projectProperties;
	private final Path projectDirectory;

	private int currentFrame;

	ProjectStoreImpl(FrameStore frameStore, ProjectProperties projectProperties, Path projectDirectory) {
		this.frameStore = argNotNull(frameStore);
		this.projectProperties = argNotNull(projectProperties);
		this.projectDirectory = argNotNull(projectDirectory);
	}

	@Override
	public void create() throws IOException {
		Files.createDirectory(projectDirectory);
		if (!Files.isWritable(projectDirectory)) {
			new IOException("directory \"" + projectDirectory + "\" is not writable");
		}
		frameStore.create();
		projectProperties.initialise();
		currentFrame = 0;
	}

	@Override
	public void open() throws IOException {
		// TODO - fail if another process has this open - perhaps using file
		// lock.
		projectProperties.initialise();
		this.currentFrame = frameStore.countFrameFiles();
	}

	@Override
	public void append(Image frame) {
		argNotNull(frame);
		frameStore.writeFrame(frame, currentFrame++);
	}

	@Override
	public Optional<Image> lastImage() {
		return frameStore.lastFrame();
	}

	@Override
	public Path getProjectDirectory() {
		return projectDirectory;
	}

	@Override
	public Iterator<Image> iterator() {
		return new FrameIterator(frameStore.frameFileIterator(), frameStore);
	}

	@Override
	public void setFramesPerSecond(int framesPerSecond) {
		argInRange("frames per second", framesPerSecond, 1, 100);
		projectProperties.setFramesPerSecond(framesPerSecond);
	}

	@Override
	public int getFramesPerSecond() {
		return projectProperties.getFramesPerSecond();
	}

	@Override
	public void setFrameSize(Size size) {
		argNotNull("frame size", size);
		projectProperties.setFrameSize(size);
	}

	@Override
	public Optional<Size> getFrameSize() {
		return projectProperties.getFrameSize();
	}
}
