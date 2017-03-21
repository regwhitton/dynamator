package app.storage.impl.project;

import static util.Verify.argNotNull;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import type.Image;

class FrameStore {

	private static final String FRAMES_SUB_DIR = "frames";
	private static final String FRAME_FILENAME_FORMAT = "%08d.png";
	private static final Pattern FRAME_FILENAME_REGEXP = Pattern.compile("^\\d{8}.png$");
	private static final String FRAME_FILE_FORMAT = "PNG";

	private final BufferedImageConverter converter;
	private final Path frameDirectory;
	private final File frameDirectoryFile;

	FrameStore(BufferedImageConverter bufferedImageConverter, Path projectDirectory) {
		this.converter = argNotNull(bufferedImageConverter);
		this.frameDirectory = argNotNull(projectDirectory).resolve(FRAMES_SUB_DIR);
		this.frameDirectoryFile = frameDirectory.toFile();
	}

	void create() throws IOException {
		Files.createDirectory(frameDirectory);
		if (!Files.isWritable(frameDirectory)) {
			new IOException("directory \"" + frameDirectory + "\" is not writable");
		}
	}

	void writeFrame(Image frame, int frameNumber) {
		argNotNull(frame);
		File file = createFile(frameNumber);
		BufferedImage bufferedImage = converter.toBufferedImage(frame);
		write(bufferedImage, file);
	}

	private File createFile(int frameNumber) {
		return new File(frameDirectoryFile, String.format(FRAME_FILENAME_FORMAT, frameNumber));
	}

	private void write(BufferedImage bufferedImage, File file) {
		try {
			if (!ImageIO.write(bufferedImage, FRAME_FILE_FORMAT, file)) {
				new ProjectStoreException("cannot encode file: " + file.getAbsolutePath());
			}
		} catch (IOException e) {
			new ProjectStoreException("cannot write file: " + file.getAbsolutePath());
		}
	}

	Image readFrame(File frameFile) {
		argNotNull(frameFile);
		try {
			return converter.toImage(ImageIO.read(frameFile));
		} catch (IOException e) {
			throw new ProjectStoreException("cannot read file: " + frameFile.getAbsolutePath());
		}
	}

	int countFrameFiles() {
		return frameFilenames(frameDirectoryFile).length;
	}

	Optional<Image> lastFrame() {
		Iterator<String> filenames = sortedFrameFilenameIterator(frameDirectoryFile);
		if (!filenames.hasNext()) {
			return Optional.empty();
		}
		String filename = filenames.next();
		while (filenames.hasNext()) {
			filename = filenames.next();
		}
		return Optional.of(readFrame(new File(frameDirectoryFile, filename)));
	}

	/**
	 * @return iterator that returns the frame files in name order.
	 */
	Iterator<File> frameFileIterator() {
		return new Iterator<File>() {
			Iterator<String> filenames = sortedFrameFilenameIterator(frameDirectoryFile);

			@Override
			public boolean hasNext() {
				return filenames.hasNext();
			}

			@Override
			public File next() {
				return new File(frameDirectoryFile, filenames.next());
			}
		};
	}

	private Iterator<String> sortedFrameFilenameIterator(File framesDir) {
		return Arrays.stream(frameFilenames(framesDir)).sorted().iterator();
	}

	private String[] frameFilenames(File framesDir) {
		return framesDir.list(new FrameFilenameFilter());
	}

	private class FrameFilenameFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return FRAME_FILENAME_REGEXP.matcher(name).matches();
		}
	}
}
