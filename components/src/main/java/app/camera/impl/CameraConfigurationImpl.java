package app.camera.impl;

import static util.Verify.isNotNeg;
import static util.Verify.isNotNull;

import app.CameraConfiguration;
import type.Size;

class CameraConfigurationImpl implements CameraConfiguration {

	private final int cameraIndex;
	private final long fingerprint;
	private final String description;
	private final Size size;

	CameraConfigurationImpl(int cameraIndex, long fingerprint, String description, Size size) {
		this.cameraIndex = isNotNeg("camera device id", cameraIndex);
		this.fingerprint = fingerprint;
		this.description = isNotNull(description);
		this.size = isNotNull("camera image size", size);
	}

	int getCameraIndex() {
		return cameraIndex;
	}

	@Override
	public long getFingerprint() {
		return fingerprint;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Size getSize() {
		return size;
	}
	
	@Override
	public String toString() {
		return description;
	}
}
