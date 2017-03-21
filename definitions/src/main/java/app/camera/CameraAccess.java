package app.camera;

import java.util.Optional;

import app.CameraConfiguration;
import type.Image;
import type.Seq;

public interface CameraAccess {

	/**
	 * Get an image from the current camera (if one, and if it is currently available).
	 */
	Optional<Image> getImage();

	/**
	 * Get the current camera device.
	 */
	Optional<CameraConfiguration> getCameraConfiguration();

	/**
	 * Probe for the list of camera devices and size formats they support.
	 */
	Seq<CameraConfiguration> findCameraConfigurations();

	/**
	 * Switch to one of the configurations returned by
	 * {@link #getConfigurations()}
	 */
	void setCameraConfiguration(CameraConfiguration cameraConfiguration);
}