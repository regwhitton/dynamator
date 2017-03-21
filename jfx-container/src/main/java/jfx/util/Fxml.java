package jfx.util;

import static util.Verify.argNotNull;
import static util.Verify.isNotNull;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import util.Msg;

public class Fxml<C> {

	private final C controller;
	private final Parent rootPanel;

	private Fxml(C controller, Parent rootPanel) {
		this.controller = argNotNull(controller);
		this.rootPanel = argNotNull(rootPanel);
	}

	public C getController() {
		return controller;
	}

	public Parent getRootPanel() {
		return rootPanel;
	}

	public static <C> Fxml<C> load(URL fxmlUrl) {
		isNotNull("null URL: cannot locate FXML file", fxmlUrl);
		FXMLLoader loader = executedLoader(fxmlUrl);
		return new Fxml<C>(loader.getController(), loader.getRoot());
	}

	private static FXMLLoader executedLoader(URL fxmlUrl) {
		FXMLLoader loader = new FXMLLoader(fxmlUrl);
		try {
			loader.load();
		} catch (IOException ioe) {
			throw new FxmlException(Msg.extract("Could not load FXML", ioe));
		}
		return loader;
	}
}