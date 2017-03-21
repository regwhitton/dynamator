package app.videoencoder.impl;

import static util.Verify.argNotNull;

import java.nio.file.Path;
import java.util.Iterator;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_videoio.VideoWriter;

import app.videoencoder.VideoEncoder;
import opencv.util.ImageToMatConverter;
import opencv.util.VideoWriterFactory;
import type.Image;
import type.Size;

class OpenCvVideoEncoder implements VideoEncoder {

	private final VideoWriterFactory videoWriterFactory;
	private final ImageToMatConverter imageToMatConverter;
	private final FourCcFinder fourCcFinder;
	
	OpenCvVideoEncoder(VideoWriterFactory videoWriterFactory, ImageToMatConverter imageToMatConverter, FourCcFinder fourCcFinder) {
		this.videoWriterFactory = argNotNull(videoWriterFactory);
		this.imageToMatConverter = argNotNull(imageToMatConverter);
		this.fourCcFinder = argNotNull(fourCcFinder);
	}

	@Override
	public void write(Path file, Iterator<Image> frames) {
		argNotNull(file);
		argNotNull(frames);
		
		if (frames.hasNext()) {
			writeFrames(file, frames);
		}
	}

	private void writeFrames(Path file, Iterator<Image> frames) {
		Image firstFrame = frames.next();
		double fps = 8;

		try (VideoWriter vw = openVideoWriter(file, fps, firstFrame.getSize())) {
			writeFrame(vw, firstFrame);
			while (frames.hasNext()) {
				writeFrame(vw, frames.next());
			}
		}
	}

	private VideoWriter openVideoWriter(Path file, double fps, Size size) {
		int fourCcCode = fourCcFinder.fourCcCode();
		opencv_core.Size sz = new opencv_core.Size(size.getWidth(), size.getHeight());

		VideoWriter vw = videoWriterFactory.create(file, fourCcCode, fps, sz, true);
		if (!vw.isOpened()) {
			throw new VideoWriteException("could not write video: " + file.toAbsolutePath());
		}
		return vw;
	}

	private void writeFrame(VideoWriter vw, Image frame) {
		vw.write(imageToMatConverter.toMat(frame));
	}
}
