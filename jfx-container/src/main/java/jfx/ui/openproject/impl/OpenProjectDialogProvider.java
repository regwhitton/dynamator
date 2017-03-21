package jfx.ui.openproject.impl;

import static util.Verify.argNotNull;

import javafx.stage.Stage;

import jfx.ui.openproject.OpenProjectDialog;
import jfx.util.Fxml;
import provider.Commissioner;
import provider.Decommissioner;
import provider.Provider;
import provider.util.Decommissioners;

public class OpenProjectDialogProvider implements Provider<OpenProjectDialog> {

	private final Stage parentWindow;

	public OpenProjectDialogProvider(Stage parentWindow) {
		this.parentWindow = argNotNull(parentWindow);
	}

	@Override
	public OpenProjectDialog get() {
		Fxml<OpenProjectController> fxml = Fxml.load(getClass().getResource("OpenProject.fxml"));
		return new OpenProjectDialogImpl(parentWindow, fxml.getController(), fxml.getRootPanel());
	}

	@Override
	public Commissioner getCommissioner(OpenProjectDialog openProjectDialog) {
		return (OpenProjectDialogImpl) argNotNull(openProjectDialog);
	}

	@Override
	public Decommissioner getDecommissioner(OpenProjectDialog openProjectDialog) {
		return Decommissioners.nullDecommissioner();
	}
}