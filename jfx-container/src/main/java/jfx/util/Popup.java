package jfx.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import util.Msg;

public class Popup {

	public static void applicationError(String message, Exception ex) {
		ex.printStackTrace();
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Application Error");
		alert.setContentText(Msg.extract(message, ex));
		alert.showAndWait();
	}

	public static void userError(String message) {
		Alert alert = new Alert(AlertType.NONE);
		alert.getButtonTypes().add(ButtonType.CLOSE);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public static void userError(String message, Exception ex) {
		Alert alert = new Alert(AlertType.NONE);
		alert.getButtonTypes().add(ButtonType.CLOSE);
		alert.setContentText(Msg.extract(message, ex));
		alert.showAndWait();
	}
}
