package util;

import static util.Verify.argNotNull;
import static util.Verify.notNullOrBlank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

public class PropertiesFile {

	private final Path propertiesFilePath;
	private final String headerComment;
	private final String defaultPropertiesPathname;

	private Properties properties;

	public PropertiesFile(Path propertiesFilePath, String headerComment, String defaultPropertiesPathname) {
		this.propertiesFilePath = argNotNull(propertiesFilePath);
		this.headerComment = argNotNull(headerComment);
		this.defaultPropertiesPathname = argNotNull(defaultPropertiesPathname);
	}

	/**
	 * If the properties file exists, it is loaded. If any properties are
	 * missing compared to the default properties file in resources, then they
	 * are added and the file saved.
	 * <p/>
	 * If the properties file is missing, it is created with a copy of the
	 * default properties.
	 */
	public void initialise() throws IOException {
		if (Files.exists(propertiesFilePath)) {
			loadAndUpdate();
		} else {
			create();
		}
	}

	private void loadAndUpdate() throws IOException {
		Properties props = loadFromFilesystem(propertiesFilePath);
		Properties defaults = loadFromResources(defaultPropertiesPathname);
		boolean propUpdated = false;
		for (String key : defaults.stringPropertyNames()) {
			if (!props.containsKey(key)) {
				props.setProperty(key, defaults.getProperty(key));
				propUpdated = true;
			}
		}
		properties = props;
		if (propUpdated) {
			saveOnInitialise();
		}
	}

	private Properties loadFromFilesystem(Path path) throws IOException {
		Properties props = new Properties();
		try (BufferedReader br = Files.newBufferedReader(path)) {
			props.load(br);
		}
		return props;
	}

	private Properties loadFromResources(String resourcePathname) throws IOException {
		Properties props = new Properties();
		try (InputStream is = getClass().getResourceAsStream(resourcePathname)) {
			if (is == null) {
				throw new IOException("Cannot find resource properties: " + resourcePathname);
			}
			props.load(is);
		}
		return props;
	}

	private void saveOnInitialise() throws IOException {
		try (BufferedWriter bw = Files.newBufferedWriter(propertiesFilePath)) {
			properties.store(bw, headerComment);
		}
	}

	private void create() throws IOException {
		Properties props = new Properties();
		Properties defaults = loadFromResources(defaultPropertiesPathname);
		for (String key : defaults.stringPropertyNames()) {
			props.setProperty(key, defaults.getProperty(key));
		}
		properties = props;
		saveOnInitialise();
	}

	/**
	 * @throws NullPointerException
	 *             if a property is requested that doesn't exist. If the
	 *             property only optionally exists then use
	 *             {@link #getOpt(String)}.
	 * @throws IllegalStateException
	 *             if the property is blank. If the property only optionally
	 *             holds a value then use {@link #getOpt(String)}.
	 */
	public String get(String key) {
		return notNullOrBlank(properties.getProperty(argNotNull(key)));
	}

	/**
	 * Sets property and saves file.
	 */
	public void set(String key, String value) {
		properties.setProperty(argNotNull(key), argNotNull(value));
		saveOnPropertyUpdate();
	}

	/**
	 * Unsets property and saves file.
	 */
	public void unset(String key) {
		properties.remove(argNotNull(key));
		saveOnPropertyUpdate();
	}

	private void saveOnPropertyUpdate() {
		try (BufferedWriter bw = Files.newBufferedWriter(propertiesFilePath)) {
			properties.store(bw, headerComment);
		} catch (IOException ioe) {
			throw new PropertiesException(Msg.extract("Cannot update properties file", ioe));
		}
	}

	public int getInt(String key) {
		return Integer.parseInt(get(key));
	}

	public void setInt(String key, int i) {
		set(key, "" + i);
	}

	/**
	 * Gets the optional value of the property, returning empty if it is not set
	 * or blank.
	 */
	public Optional<String> getOptional(String key) {
		String prop = properties.getProperty(argNotNull(key));
		return prop == null || prop.chars().allMatch(Character::isWhitespace) ? Optional.empty() : Optional.of(prop);
	}

	@SuppressWarnings("serial")
	private class PropertiesException extends RuntimeException {
		PropertiesException(String message) {
			super(message);
		}
	}
}
