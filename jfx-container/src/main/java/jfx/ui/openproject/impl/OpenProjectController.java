package jfx.ui.openproject.impl;

import static util.Verify.argNotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import jfx.util.Popup;
import util.Msg;

/**
 * JavaFX controller - public only because JavaFX/Scenebuilder requires it to be
 * so.
 */
public class OpenProjectController {

	private static final int CREATE_TAB_INDEX = 0;
	private static final int OPEN_TAB_INDEX = 1;
	private static final Pattern FILENAME_CHARACTERS = Pattern.compile("[^<>:\"/\\\\|\\?\\*]");

	@FXML
	private TextField projectName;

	@FXML
	private CheckBox useDefaultLocation;

	@FXML
	private Node locationControls;

	@FXML
	private TextField projectLocation;

	@FXML
	private TabPane createOpenTabPane;

	@FXML
	private TextField openProjectFolder;

	private Stage window;

	private Path defaultParentLocation;

	private Path previousOrDefaultParentLocation;

	private Optional<Path> result;

	void setWindow(Stage window) {
		this.window = argNotNull(window);
	}

	void commission() {
		useDefaultLocation.setSelected(true);
		locationControls.setDisable(true);
	}

	void setupPrompt(Path defaultParentLocation, Optional<Path> previousProjectDirectory) {
		this.defaultParentLocation = argNotNull(defaultParentLocation);
		Optional<Path> previousParent = previousProjectDirectory.map(p -> p.getParent());
		this.previousOrDefaultParentLocation = previousParent.orElse(defaultParentLocation);

		selectTab(previousProjectDirectory.isPresent() ? OPEN_TAB_INDEX : CREATE_TAB_INDEX);
		projectName.setText("");
		openProjectFolder.setText("");
		projectLocation.setText(previousOrDefaultParentLocation.toString());
	}

	private void selectTab(int tab) {
		createOpenTabPane.getSelectionModel().select(tab);
	}

	Optional<Path> getResult() {
		return result;
	}

	@FXML
	public void projectNameKeyFilter(KeyEvent keyEvent) {
		String ch = keyEvent.getCharacter();
		if (!FILENAME_CHARACTERS.matcher(ch).matches()) {
			keyEvent.consume();
		}
	}

	@FXML
	public void cancelButtonAction(ActionEvent event) {
		result = Optional.empty();
		window.hide();
	}

	@FXML
	public void toggleUseDefaultLocationAction() {
		locationControls.setDisable(useDefaultLocation.isSelected());
	}

	@FXML
	public void browseForProjectLocationAction() {
		DirectoryChooser dc = new DirectoryChooser();
		dc.setTitle("Select folder to create project within");
		dc.setInitialDirectory(toBestBrowseLocation(projectLocation.getText()));
		File file = dc.showDialog(window);
		if (file != null) {
			projectLocation.setText(file.getAbsolutePath());
		}
	}

	private File toBestBrowseLocation(String possibleDirectoryPath) {
		try {
			return asExistingDirectory(possibleDirectoryPath).toFile();
		} catch (NotValidPathException nvpe) {
			return previousOrDefaultParentLocation.toFile();
		}
	}

	@FXML
	public void createButtonAction(ActionEvent event) {
		Path project;
		try {
			Path parent = selectedParentLocation();
			project = parent.resolve(projectName.getText());
		} catch (NotValidPathException nvpe) {
			Popup.userError(Msg.extract("Invalid project location", nvpe));
			return;
		} catch (InvalidPathException ipe) {
			Popup.userError(Msg.extract("Invalid project name", ipe));
			return;
		}

		if (Files.exists(project)) {
			Popup.userError("Project directory path already exists");
			return;
		}

		result = Optional.of(project);
		window.hide();
	}

	private Path selectedParentLocation() throws NotValidPathException {
		return useDefaultLocation.isSelected() ? defaultParentLocation : asExistingDirectory(projectLocation.getText());
	}

	@FXML
	public void openButtonAction(ActionEvent event) {
		Path projectPath;
		try {
			projectPath = asExistingDirectory(openProjectFolder.getText());
		} catch (NotValidPathException nvpe) {
			Popup.userError(nvpe.getMessage());
			return;
		}

		if (!isProject(projectPath)) {
			Popup.userError("Directory is not a Dynamator project");
			return;
		}

		result = Optional.of(projectPath);
		window.hide();
	}

	private boolean isProject(Path projectPath) {
		// Not a good test - should inject tester function from project store.
		return Files.exists(projectPath.resolve("dynamator-project.properties"));
	}

	@FXML
	public void browseForExistingProjectAction() {
		DirectoryChooser dc = new DirectoryChooser();
		dc.setTitle("Select project folder to open");
		dc.setInitialDirectory(toBestBrowseLocation(openProjectFolder.getText()));
		File file = dc.showDialog(window);
		if (file != null) {
			openProjectFolder.setText(file.getAbsolutePath());
		}
	}

	private Path asExistingDirectory(String possibleDirectoryPath) throws NotValidPathException {
		Path dir = asValidPath(possibleDirectoryPath);
		if (!Files.isDirectory(dir)) {
			throw new NotValidPathException("Directory path is not an existing directory");
		}
		return dir;
	}

	private Path asValidPath(String pathToValidate) throws NotValidPathException {
		String trimmedPath = pathToValidate.trim();
		if (trimmedPath.isEmpty()) {
			throw new NotValidPathException("Directory path is empty");
		}
		try {
			return Paths.get(trimmedPath);
		} catch (InvalidPathException ipe) {
			throw new NotValidPathException(Msg.extract("Not a valid directory path", ipe));
		}
	}

	@SuppressWarnings("serial")
	private class NotValidPathException extends Exception {
		NotValidPathException(String message) {
			super(message);
		}
	}
}