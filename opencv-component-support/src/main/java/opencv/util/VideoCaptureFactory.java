package opencv.util;

import org.bytedeco.javacpp.opencv_videoio.VideoCapture;

public class VideoCaptureFactory {

	public VideoCapture create() {
		return new VideoCapture();
	}
}