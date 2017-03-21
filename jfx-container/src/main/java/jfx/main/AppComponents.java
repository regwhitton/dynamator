package jfx.main;

import static util.Verify.argNotNull;

import javafx.stage.Stage;

import app.App;
import app.camera.CameraAccess;
import app.camera.impl.CameraAccessProvider;
import app.imagetransformer.ImageTransformer;
import app.imagetransformer.impl.ImageTransformerProvider;
import app.impl.AppProvider;
import app.storage.ProjectStoreManager;
import app.storage.impl.ProjectStoreManagerProvider;
import app.videoencoder.VideoEncoder;
import app.videoencoder.impl.VideoEncoderProvider;
import component.Components;
import component.util.ComponentsBuilder;
import jfx.ui.Ui;
import jfx.ui.impl.UiProvider;
import jfx.ui.openproject.OpenProjectDialog;
import jfx.ui.openproject.impl.OpenProjectDialogProvider;

class AppComponents {

	private final Stage window;
	private final ComponentsBuilder blr = ComponentsBuilder.create();

	AppComponents(Stage window) {
		this.window = argNotNull(window);
	}

	Components build() {
		blr.add(ProjectStoreManager.class, new ProjectStoreManagerProvider());
		blr.add(CameraAccess.class, new CameraAccessProvider());
		blr.add(ImageTransformer.class, new ImageTransformerProvider());
		blr.add(VideoEncoder.class, new VideoEncoderProvider());

		blr.add(App.class, new AppProvider(get(ProjectStoreManager.class), get(CameraAccess.class),
				get(ImageTransformer.class), get(VideoEncoder.class)));

		blr.add(OpenProjectDialog.class, new OpenProjectDialogProvider(window));
		blr.add(Ui.class, new UiProvider(window, get(App.class), get(OpenProjectDialog.class)));
		return blr.build();
	}

	private <T> T get(Class<T> type) {
		return blr.get(type);
	}
}
