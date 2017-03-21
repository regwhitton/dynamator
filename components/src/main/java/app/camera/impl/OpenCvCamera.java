package app.camera.impl;

import static org.bytedeco.javacpp.opencv_videoio.CAP_PROP_FRAME_HEIGHT;
import static org.bytedeco.javacpp.opencv_videoio.CAP_PROP_FRAME_WIDTH;
import static util.Verify.argNotNull;

import java.util.Optional;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_videoio.VideoCapture;

import opencv.util.MatToImageConverter;
import opencv.util.VideoCaptureFactory;
import type.Image;
import type.Size;
import type.util.Sizes;

/**
 * Wraps {@link VideoCapture} to work around various issues with it.
 */
class OpenCvCamera {

	private static final int BIGGER_THAN_ANY_CAMERA = 32000;

	private final VideoCaptureFactory videoCaptureFactory;
	private final Mat matrix;
	private final MatToImageConverter matToImageConverter;

	private VideoCapture videoCapture;

	OpenCvCamera(VideoCaptureFactory videoCaptureFactory, Mat matrix, MatToImageConverter matToImageConverter) {
		this.videoCaptureFactory = argNotNull(videoCaptureFactory);
		this.matrix = argNotNull(matrix);
		this.matToImageConverter = argNotNull(matToImageConverter);
	}

	void commission() {
		videoCapture = videoCaptureFactory.create();
	}

	void decommission() {
		videoCapture.release();
		matrix.release();
	}

	Image getImage() {
		// Flush waiting unwanted extra image from stream.
		// Is this just a windows 7 & 10 issue?
		videoCapture.read(matrix);

		if (!videoCapture.read(matrix)) {
			throw new CameraException("cannot read from camera");
		}
		return matToImageConverter.toImage(matrix);
	}

	void setConfig(Optional<CameraConfigurationImpl> config) {
		if (config.isPresent()) {
			CameraConfigurationImpl cfg = config.get();
			setVideoCapture(cfg.getCameraIndex(), cfg.getSize());
		} else {
			// Need to release to stop using device, but this also releases the
			// internal buffer, so need to create a new one.
			videoCapture.release();
			videoCapture = videoCaptureFactory.create();
		}
	}

	private void setVideoCapture(int cameraIndex, Size size) {
		if (!videoCapture.open(cameraIndex)) {
			throw new CameraException("OpenCV cannot open camera " + cameraIndex);
		}

		// Set large first:
		// http://stackoverflow.com/questions/19448078/python-opencv-access-webcam-maximum-resolution#30785395
		if (videoCapture.get(CAP_PROP_FRAME_WIDTH) < size.getWidth()
				|| videoCapture.get(CAP_PROP_FRAME_HEIGHT) < size.getWidth()) {
			videoCapture.set(CAP_PROP_FRAME_WIDTH, BIGGER_THAN_ANY_CAMERA);
			videoCapture.set(CAP_PROP_FRAME_HEIGHT, BIGGER_THAN_ANY_CAMERA);
		}
		videoCapture.set(CAP_PROP_FRAME_WIDTH, size.getWidth());
		videoCapture.set(CAP_PROP_FRAME_HEIGHT, size.getHeight());
	}

	boolean canOpen(int cameraIndex) {
		return videoCapture.open(cameraIndex);
	}

	Size maxImageSize() {
		videoCapture.set(CAP_PROP_FRAME_WIDTH, 16000);
		videoCapture.set(CAP_PROP_FRAME_HEIGHT, 16000);
		return currentSize();
	}

	boolean canSetSize(Size size) {
		Size oldSize = currentSize();
		videoCapture.set(CAP_PROP_FRAME_WIDTH, size.getWidth());
		videoCapture.set(CAP_PROP_FRAME_HEIGHT, size.getHeight());
		Size newSize = currentSize();
		return !oldSize.equals(newSize);
	}

	private Size currentSize() {
		return Sizes.sz((int) videoCapture.get(CAP_PROP_FRAME_WIDTH), (int) videoCapture.get(CAP_PROP_FRAME_HEIGHT));
	}
}
