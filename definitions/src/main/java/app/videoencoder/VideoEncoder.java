package app.videoencoder;

import java.nio.file.Path;
import java.util.Iterator;

import type.Image;

public interface VideoEncoder {

	void write(Path file, Iterator<Image> frames);
}
