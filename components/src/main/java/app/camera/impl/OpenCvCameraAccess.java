package app.camera.impl;

import static util.Verify.argNotNull;

import java.util.Optional;

import app.CameraConfiguration;
import app.camera.CameraAccess;
import provider.Commissioner;
import provider.Decommissioner;
import type.Image;
import type.Seq;

class OpenCvCameraAccess implements CameraAccess, Commissioner, Decommissioner {

	/**
	 * The camera device has to be shared with the configFinder, and to be time
	 * efficient it has to be able to leave it any state.
	 */
	private final OpenCvCamera openCvCamera;
	private final OpenCvConfigurationFinder configFinder;

	private boolean findingConfig;
	private Optional<CameraConfigurationImpl> currentConfig = Optional.empty();

	public OpenCvCameraAccess(OpenCvCamera openCvCamera, OpenCvConfigurationFinder configFinder) {
		this.openCvCamera = argNotNull(openCvCamera);
		this.configFinder = argNotNull(configFinder);
	}

	@Override
	public void commission() {
		openCvCamera.commission();
	}

	@Override
	public void decommission() {
		openCvCamera.decommission();
	}

	@Override
	public Optional<Image> getImage() {
		if (!findingConfig && currentConfig.isPresent()) {
			synchronized (openCvCamera) {
				return Optional.of(openCvCamera.getImage());
			}
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Seq<CameraConfiguration> findCameraConfigurations() {
		Seq<CameraConfiguration> configs;
		synchronized (openCvCamera) {
			findingConfig = true;
			configs = configFinder.findConfigurations(openCvCamera);
			openCvCamera.setConfig(currentConfig);
			findingConfig = false;
		}
		return configs;
	}

	@Override
	public void setCameraConfiguration(CameraConfiguration cameraConfiguration) {
		CameraConfigurationImpl config = (CameraConfigurationImpl) argNotNull(cameraConfiguration);
		synchronized (openCvCamera) {
			currentConfig = Optional.of(config);
			openCvCamera.setConfig(currentConfig);
		}
	}

	@Override
	public Optional<CameraConfiguration> getCameraConfiguration() {
		return currentConfig.map(cc -> cc);
	}
}
