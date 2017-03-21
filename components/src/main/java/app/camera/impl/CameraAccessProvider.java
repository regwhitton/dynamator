package app.camera.impl;

import static util.Verify.argNotNull;

import org.bytedeco.javacpp.opencv_core.Mat;

import app.camera.CameraAccess;
import opencv.util.MatToImageConverter;
import opencv.util.VideoCaptureFactory;
import provider.Commissioner;
import provider.Decommissioner;
import provider.Provider;

public class CameraAccessProvider implements Provider<CameraAccess> {

	@Override
	public CameraAccess get() {
		OpenCvCamera openCvCamera = new OpenCvCamera(new VideoCaptureFactory(), new Mat(), new MatToImageConverter());
		return new OpenCvCameraAccess(openCvCamera, new OpenCvConfigurationFinder());
	}

	@Override
	public Commissioner getCommissioner(CameraAccess cameraAccess) {
		return (OpenCvCameraAccess) argNotNull(cameraAccess);
	}

	@Override
	public Decommissioner getDecommissioner(CameraAccess cameraAccess) {
		return (OpenCvCameraAccess) argNotNull(cameraAccess);
	}
}
