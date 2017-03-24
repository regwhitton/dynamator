package jfx.ui.openproject.impl;

import static jfx.ui.test.JfxAssert.assertThatFxmlLoadsControllerClass;

import org.junit.Test;

public class OpenProjectControllerTest {

	@Test
	public void fxmlShouldReferenceTheCorrectControllerClass() throws Exception {
		assertThatFxmlLoadsControllerClass("OpenProject.fxml", OpenProjectController.class);
	}
}
