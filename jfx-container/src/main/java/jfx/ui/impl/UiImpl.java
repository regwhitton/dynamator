package jfx.ui.impl;

import static util.Verify.argNotNull;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import jfx.ui.Ui;
import provider.Commissioner;
import provider.Decommissioner;

class UiImpl implements Ui, Commissioner, Decommissioner {

	private final Stage window;
	private final Parent rootPanel;
	private final UiController uiController;
	private final Scheduler scheduler;

	UiImpl(Stage window, Parent rootPanel, UiController uiController, Scheduler scheduler) {
		this.window = argNotNull(window);
		this.rootPanel = argNotNull(rootPanel);
		this.uiController = argNotNull(uiController);
		this.scheduler = argNotNull(scheduler);
	}

	@Override
	public void commission() throws Exception {
		setupWindow();
		uiController.commission();
		scheduler.commission();
	}

	@Override
	public void decommission() {
		scheduler.decommission();
		uiController.decommission();
	}

	private void setupWindow() {
		window.setTitle("Dynamator");
		window.setScene(new Scene(rootPanel));
		window.sizeToScene();
	}

	@Override
	public void show() throws Exception {
		uiController.selectInitialProject();
		window.show();
		scheduler.start();
	}

	@Override
	public void hide() {
		scheduler.stop();
		window.hide();
	}
}