package jfx.ui.openproject.impl;

import static util.Verify.argNotNull;

import java.nio.file.Path;
import java.util.Optional;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import jfx.ui.openproject.OpenProjectDialog;
import provider.Commissioner;

class OpenProjectDialogImpl implements OpenProjectDialog, Commissioner {

	private final Stage parentWindow;
	private final OpenProjectController controller;
	private final Parent rootPanel;

	private Stage dialog;

	OpenProjectDialogImpl(Stage parentWindow, OpenProjectController controller, Parent rootPanel) {
		this.parentWindow = argNotNull(parentWindow);
		this.controller = argNotNull(controller);
		this.rootPanel = argNotNull(rootPanel);
	}

	@Override
	public void commission() {
		dialog = createDialog();
		controller.setWindow(dialog);
		controller.commission();
	}

	private Stage createDialog() {
		Stage dialog = new Stage();
		dialog.setScene(new Scene(rootPanel));
		dialog.setTitle("Dynamator");
		dialog.initOwner(parentWindow);
		dialog.initModality(Modality.APPLICATION_MODAL);
		return dialog;
	}

	@Override
	public Optional<Path> promptForProject(Path defaultParentLocation, Optional<Path> previousProjectDirectory) {
		controller.setupPrompt(argNotNull(defaultParentLocation), argNotNull(previousProjectDirectory));
		dialog.showAndWait();
		return controller.getResult();
	}
}
