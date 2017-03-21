package jfx.ui.openproject.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import jfx.ui.openproject.OpenProjectDialog;
import jfx.ui.openproject.impl.OpenProjectDialogProvider;

public class TestOpenProjectDialog extends Application {

	private TextField defaultProjectDir = new TextField(System.getProperty("user.home"));
	private Label defaultProjectDirLabel = new Label("Default project directory");
	private CheckBox createHint = new CheckBox("Create");
	private Button openButton = new Button("Open");

	private Optional<Path> previousProjectPath = Optional.empty();

	private OpenProjectDialogProvider provider;
	private OpenProjectDialog dialog;

	// TODO: Need to make text fields grow within vbox - see
	// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/HBox.html

	@Override
	public void start(Stage stage) throws Exception {
		provider = new OpenProjectDialogProvider(stage);
		dialog = provider.get();
		provider.getCommissioner(dialog).commission();

		openButton.setOnAction(new DialogOpener());
		defaultProjectDirLabel.setLabelFor(defaultProjectDir);

		stage.setScene(
				new Scene(new FlowPane(10D, 10D, defaultProjectDirLabel, defaultProjectDir, createHint, openButton)));
		stage.setTitle("OpenProjectDialog");
		stage.sizeToScene();
		stage.show();
	}

	@Override
	public void stop() {
		provider.getDecommissioner(dialog).decommission();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private class DialogOpener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String defPath = defaultProjectDir.getText();
			System.out.println("promptForProject(" + defPath + ", " + previousProjectPath + ")");
			Optional<Path> dir = dialog.promptForProject(Paths.get(defPath), previousProjectPath);
			System.out.println("result=" + dir);
			System.out.println();
			previousProjectPath = dir;
		}
	}
}