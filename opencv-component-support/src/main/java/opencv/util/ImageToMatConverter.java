package opencv.util;

import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static util.Verify.argNotNull;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import type.Image;
import type.Size;
import type.util.ImageBuilder;
import type.util.Sizes;

public class ImageToMatConverter {

	private final MatFactory matFactory;
	
	public ImageToMatConverter(MatFactory matFactory){
		this.matFactory = argNotNull(matFactory);
	}
	
	public Image toImage(Mat mat) {
		validate(mat);

		Size sz = Sizes.sz(mat.cols(), mat.rows());
		int scanStride = (int) mat.step();
		ImageBuilder ib = ImageBuilder.create(sz, scanStride);
		ib.copyPixelData(buffer -> mat.arrayData().get(buffer));
		return ib.build();
	}

	private void validate(Mat mat) {
		if (mat.depth() != CV_8U) {
			throw new ImageConversionException("Mat is not 8 bit");
		}

		int nChannels = mat.channels();
		if (nChannels != 3) {
			throw new ImageConversionException("Mat does not have 3 channels");
		}
	}

	public Mat toMat(Image image) {
		int nRows = image.getSize().getHeight();
		int nCols = image.getSize().getWidth();
		BytePointer bp = new BytePointer(image.copyPixelData());
		return matFactory.create(nRows, nCols, opencv_core.CV_8UC3, bp, image.getScanStride());
	}
}