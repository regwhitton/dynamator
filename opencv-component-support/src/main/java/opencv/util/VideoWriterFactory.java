package opencv.util;

import static util.Verify.argNotNull;

import java.nio.file.Path;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_videoio.VideoWriter;

public class VideoWriterFactory {

	public VideoWriter create(Path file, int fourcc, double fps, opencv_core.Size size, boolean isColor) {
		argNotNull(file);
		argNotNull(size);
		return new VideoWriter(file.toAbsolutePath().toString(), fourcc, fps, size, isColor);
	}
}