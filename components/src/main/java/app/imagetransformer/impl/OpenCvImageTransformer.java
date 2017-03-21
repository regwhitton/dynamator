package app.imagetransformer.impl;

import static util.Verify.argNotNull;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import app.imagetransformer.ImageTransformer;
import opencv.util.ImageToMatConverter;
import opencv.util.MatFactory;
import opencv.util.MatToImageConverter;

import org.bytedeco.javacpp.opencv_imgproc;

import type.Image;

class OpenCvImageTransformer implements ImageTransformer {

	private final MatFactory matFactory;
	private final ImageToMatConverter imageToMatConverter;
	private final MatToImageConverter matToImageConverter;

	OpenCvImageTransformer(MatFactory matFactory, ImageToMatConverter imageToMatConverter,
			MatToImageConverter matToImageConverter) {
		this.matFactory = argNotNull(matFactory);
		this.imageToMatConverter = argNotNull(imageToMatConverter);
		this.matToImageConverter = argNotNull(matToImageConverter);
	}

	@Override
	public Image overlay(Image image, Image toOverlay) {
		Mat imageMat = imageToMatConverter.toMat(argNotNull(image));
		Mat overlayMat = imageToMatConverter.toMat(argNotNull(toOverlay));

		Mat combinedMat = matFactory.create();
		opencv_core.bitwise_xor(imageMat, overlayMat, combinedMat);

		imageMat.release();
		overlayMat.release();
		return convertAndRelease(combinedMat);
	}

	@Override
	public Image toOutline(Image image) {
		Mat mat = imageToMatConverter.toMat(argNotNull(image));
		opencv_imgproc.cvtColor(mat, mat, opencv_imgproc.COLOR_BGR2GRAY);
		// Gaussian blur with size 5x5 and sigma 1.3 suggested on wikipedia
		// https://en.wikipedia.org/wiki/Canny_edge_detector#Process_of_Canny_edge_detection_algorithm
		// 3x3 suggested here, with canny kernal size of 3:
		// http://docs.opencv.org/doc/tutorials/imgproc/imgtrans/canny_detector/canny_detector.html
		opencv_imgproc.GaussianBlur(mat, mat, new opencv_core.Size(5, 5), 1.3);
		opencv_imgproc.Canny(mat, mat, 40, 60);
		opencv_imgproc.cvtColor(mat, mat, opencv_imgproc.COLOR_GRAY2BGR);
		return convertAndRelease(mat);
	}

	private Image convertAndRelease(Mat mat) {
		Image image = matToImageConverter.toImage(mat);
		mat.release();
		return image;
	}
}
