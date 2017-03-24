package jfx.ui.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;

import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

public class JfxAssert {

	private JfxAssert() {
	}

	/**
	 * Asserts that refactoring has not moved or renamed the JavaFX controller
	 * class without the reference in the FXML file being updated. Assumes the
	 * fxmlFile is located in the same package.
	 */
	public static void assertThatFxmlLoadsControllerClass(String fxmlFile, Class<?> controllerClass) throws Exception {
		URL url = controllerClass.getResource(fxmlFile);
		InputSource fxmlSource = new InputSource(url.openStream());
		String controllerClassInFxml = XPathFactory.newInstance().newXPath()
				.evaluate("//@*[local-name() = 'controller']", fxmlSource);
		assertThat(controllerClassInFxml).isEqualTo(controllerClass.getName());
	}
}
