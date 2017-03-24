package jfx.ui.impl;

import static jfx.ui.test.JfxAssert.assertThatFxmlLoadsControllerClass;

import org.junit.Test;

public class UiControllerTest {

	@Test
	public void fxmlShouldReferenceTheCorrectControllerClass() throws Exception {
		assertThatFxmlLoadsControllerClass("Ui.fxml", UiController.class);
	}
}
