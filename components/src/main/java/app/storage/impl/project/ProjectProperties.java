package app.storage.impl.project;

import static util.Verify.argNotNull;

import java.io.IOException;
import java.nio.file.Path;

import util.PropertiesFile;

class ProjectProperties {

	private final PropertiesFile props;

	private final static String FPS_PROPERTY = "fps";

	ProjectProperties(Path projectDirectory) {
		argNotNull(projectDirectory);
		this.props = new PropertiesFile(projectDirectory.resolve("dynamator-project.properties"), "Dynamator project settings",
				"/defaultproject.properties");
	}

	void initialise() throws IOException {
		props.initialise();
	}

	void setFps(int fps) {
		props.setInt(FPS_PROPERTY, fps);
	}

	int getFps() {
		return props.getInt(FPS_PROPERTY);
	}
}