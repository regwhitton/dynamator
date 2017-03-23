package jfx.ui.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;

import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.xml.sax.InputSource;

public class UiControllerTest {

	@Test
	public void fxmlShouldReferenceTheCorrectControllerClass() throws Exception {
		// Refactoring that moves classes (where the FXML isn't also updated)
		// breaks loading in confusing way.
		URL url = UiController.class.getResource("Ui.fxml");
		InputSource source = new InputSource(url.openStream());
		String controllerClassInFxml = XPathFactory.newInstance().newXPath()
				.evaluate("//@*[local-name() = 'controller']", source);
		assertThat(controllerClassInFxml).isEqualTo(UiController.class.getName());
	}
}
