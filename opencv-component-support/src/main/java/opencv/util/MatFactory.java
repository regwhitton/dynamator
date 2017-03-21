package opencv.util;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.Mat;

/**
 * Used to create OpenCV Matrixes.
 */
public class MatFactory {

	public Mat create() {
		return new Mat();
	}

	public Mat create(int nRows, int nCols, int type, BytePointer data, int scanStride) {
		return new Mat(nRows, nCols, type, data, scanStride);
	}
}