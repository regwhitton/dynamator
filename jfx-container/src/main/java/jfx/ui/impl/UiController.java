package jfx.ui.impl;

import static util.Verify.argNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

import app.App;
import app.CameraConfiguration;
import app.FrameSizeMismatchException;
import app.Project;
import jfx.ui.openproject.OpenProjectDialog;
import jfx.util.JfxImageConverter;
import jfx.util.Popup;
import type.Image;

/**
 * JavaFX controller - public only because JavaFX/Scenebuilder requires it to be
 * so.
 */
public class UiController {

	private static final int IMAGEVIEWER_PADDING = 3;

	// Dependencies to be injected
	private App app;
	private OpenProjectDialog openProjectDialog;
	private JfxImageConverter imageConverter;

	@FXML
	private Button grabFrameButton;

	@FXML
	private ComboBox<CameraConfiguration> cameraChoice;

	@FXML
	private ImageView imageViewer;

	@FXML
	private ScrollPane imageViewerScrollPane;

	@FXML
	private CheckBox fitImageToPaneCheckBox;

	private boolean bFitImageViewToPane;

	private Project project;

	void setDependencies(App app, OpenProjectDialog openProjectDialog, JfxImageConverter imageConverter) {
		this.app = argNotNull(app);
		this.openProjectDialog = argNotNull(openProjectDialog);
		this.imageConverter = argNotNull(imageConverter);
	}

	void commission() {
		fitImageCheckboxAction();

		imageViewerScrollPane.widthProperty().addListener(new SizeChangeListener());
		imageViewerScrollPane.heightProperty().addListener(new SizeChangeListener());
	}

	void decommission() {
	}

	void selectInitialProject() throws Exception {
		Optional<Project> lastProject = app.openLastProject();
		if (lastProject.isPresent()) {
			project = lastProject.get();
			return;
		}

		Optional<Path> projectPath = openProjectDialog.promptForProject(app.projectsHomeDirectory(), Optional.empty());
		if (projectPath.isPresent()) {
			openOrCreateProject(projectPath.get());
		} else {
			throw new UiException("No project selected");
		}
	}

	void updatePreviewImage() {
		Optional<Image> pv = project.previewImage();
		if (pv.isPresent()) {
			imageViewer.setImage(imageConverter.convert(pv.get()));
			grabFrameButton.setDisable(false);
		} else {
			imageViewer.setImage(null);
			grabFrameButton.setDisable(true);
		}
	}

	@FXML
	public void grabFrameButtonAction() {
		try {
			project.captureImage();
		} catch (FrameSizeMismatchException ex) {
			Popup.userError("Frame size for project previously set to " + ex.getExpectedFrameSize());
		}
	}

	@FXML
	public void onCameraChoiceShowAction() {
		ObservableList<CameraConfiguration> cameraConfigs = cameraChoice.getItems();
		cameraConfigs.clear();
		app.getCameraConfigurations().forEach(cc -> cameraConfigs.add(cc));
	}

	@FXML
	public void onCameraSelectAction() {
		CameraConfiguration cameraConfig = cameraChoice.getValue();
		if (cameraConfig != null) {
			app.setCameraConfiguration(cameraChoice.getValue());
		}
	}

	@FXML
	public void fitImageCheckboxAction() {
		bFitImageViewToPane = fitImageToPaneCheckBox.isSelected();
		if (bFitImageViewToPane) {
			fitImageToScrollPaneSize();
		} else {
			showImageAtFullSize();
		}
	}

	@FXML
	public void changeProjectAction() {
		try {
			changeProject();
		} catch (IOException ex) {
			Popup.applicationError("Could not change project", ex);
		}
	}

	private void changeProject() throws IOException {
		Optional<Path> projectPath = openProjectDialog.promptForProject(app.projectsHomeDirectory(),
				Optional.of(project.getProjectDirectory()));
		if (projectPath.isPresent()) {
			openOrCreateProject(projectPath.get());
		}
	}

	private void openOrCreateProject(Path projectDir) throws IOException {
		// TODO: Prompt for camera size (and/or output size) on new project.
		// Control - route through app? or just use cameraAccess direct? route.
		project = Files.exists(projectDir) ? app.openProject(projectDir) : app.createProject(projectDir);
	}

	@FXML
	public void createVideo() {
		project.createVideo();
	}

	private void fitImageToScrollPaneSize() {
		imageViewerScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		imageViewer.setFitWidth(imageViewerScrollPane.widthProperty().doubleValue() - IMAGEVIEWER_PADDING);
		imageViewer.setFitHeight(imageViewerScrollPane.heightProperty().doubleValue() - IMAGEVIEWER_PADDING);
	}

	private void showImageAtFullSize() {
		imageViewerScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		imageViewer.setFitWidth(0);
		imageViewer.setFitHeight(0);
	}

	private class SizeChangeListener implements ChangeListener<Number> {
		@Override
		public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
			if (bFitImageViewToPane) {
				fitImageToScrollPaneSize();
			}
		}
	}
}