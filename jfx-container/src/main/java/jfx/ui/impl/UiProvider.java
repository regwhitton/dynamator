package jfx.ui.impl;

import static util.Verify.argNotNull;

import javafx.scene.Parent;
import javafx.stage.Stage;

import app.App;
import jfx.ui.Ui;
import jfx.ui.openproject.OpenProjectDialog;
import jfx.util.Fxml;
import jfx.util.JfxImageConverter;
import provider.Commissioner;
import provider.Decommissioner;
import provider.Provider;

public class UiProvider implements Provider<Ui> {

	private final Stage window;
	private final App app;
	private final OpenProjectDialog openProjectDialog;

	public UiProvider(Stage window, App app, OpenProjectDialog openProjectDialog) {
		this.window = argNotNull(window);
		this.app = argNotNull(app);
		this.openProjectDialog = argNotNull(openProjectDialog);
	}

	@Override
	public Ui get() {
		Fxml<UiController> fxml = Fxml.load(getClass().getResource("Ui.fxml"));
		UiController uiController = fxml.getController();
		Parent rootPanel = fxml.getRootPanel();

		uiController.setDependencies(app, openProjectDialog, new JfxImageConverter());

		return new UiImpl(window, rootPanel, uiController, new Scheduler(uiController));
	}

	@Override
	public Commissioner getCommissioner(Ui ui) {
		return (UiImpl) argNotNull(ui);
	}

	@Override
	public Decommissioner getDecommissioner(Ui ui) {
		return (UiImpl) argNotNull(ui);
	}
}
