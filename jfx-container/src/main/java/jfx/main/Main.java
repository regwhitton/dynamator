package jfx.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import component.Components;
import jfx.ui.Ui;
import jfx.util.Popup;

public class Main extends Application {

	private Components components;
	private Ui ui;

	@Override
	public void start(Stage stage) {
		try {
			startImpl(stage);
		} catch (Exception ex) {
			Popup.applicationError("Could not start up", ex);
			Platform.exit();
		}
	}

	private void startImpl(Stage window) throws Exception {
		components = createComponents(window);
		components.commission();
		ui = components.get(Ui.class);
		ui.show();
	}

	@Override
	public void stop() {
		if (ui != null) {
			ui.hide();
		}
		components.decommission();
	}

	public static void main(String[] args) {
		launch(args);
	}

	protected Components createComponents(Stage window) {
		return new AppComponents(window).build();
	}
}