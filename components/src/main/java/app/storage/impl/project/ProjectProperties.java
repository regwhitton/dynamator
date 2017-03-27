package app.storage.impl.project;

import static util.Verify.argNotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import type.Size;
import type.util.Sizes;
import util.PropertiesFile;

class ProjectProperties {

	private final PropertiesFile props;

	private final static String FRAMES_PER_SECOND_PROPERTY = "frames.per.second";
	private final static String FRAME_SIZE_PROPERTY = "frame.size";

	ProjectProperties(Path projectDirectory) {
		argNotNull(projectDirectory);
		this.props = new PropertiesFile(projectDirectory.resolve("dynamator-project.properties"),
				"Dynamator project settings", "/defaultproject.properties");
	}

	void initialise() throws IOException {
		props.initialise();
	}

	void setFramesPerSecond(int framesPerSecond) {
		props.setInt(FRAMES_PER_SECOND_PROPERTY, framesPerSecond);
	}

	int getFramesPerSecond() {
		return props.getInt(FRAMES_PER_SECOND_PROPERTY);
	}

	void setFrameSize(Size size) {
		props.set(FRAME_SIZE_PROPERTY, size.toString());
	}

	Optional<Size> getFrameSize() {
		return props.getOptional(FRAME_SIZE_PROPERTY).map(value -> Sizes.sz(value));
	}
}