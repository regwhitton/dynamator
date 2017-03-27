package app.storage.impl;

import static util.Verify.argNotNull;
import static util.Verify.argNotNullOrBlank;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import util.PropertiesFile;

class AppProperties {

	private final PropertiesFile props;

	private final static String LAST_PROJECT_PROPERTY = "last.project";

	AppProperties(Path projectDirectory) {
		argNotNull(projectDirectory);
		this.props = new PropertiesFile(projectDirectory.resolve("app.properties"), "Dynamator application settings",
				"/defaultapp.properties");
	}

	void initialise() throws IOException {
		props.initialise();
	}

	void setLastProjectPath(String lastProjectPath) {
		props.set(LAST_PROJECT_PROPERTY, argNotNullOrBlank(lastProjectPath));
	}

	Optional<String> getLastProjectPath() {
		return props.getOptional(LAST_PROJECT_PROPERTY);
	}
}