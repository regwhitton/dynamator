package type.util;

import java.util.function.Consumer;

import type.Image;
import type.Size;
import util.Verify;

public class ImageBuilder {
	private final Size size;
	private final int scanStride;
	protected final byte[] pixels;

	private ImageBuilder(Size size, int scanStride) {
		this.size = Verify.argNotNull("size", size);
		this.scanStride = Verify.isAtLeast("scanStride", scanStride, 3 * size.getWidth());
		this.pixels = new byte[size.getHeight() * scanStride];
	}

	/**
	 * Create the builder for an Image.
	 * 
	 * @see Image#getSize()
	 * @see Image#getScanStride()
	 */
	public static ImageBuilder create(Size size, int scanStride) {
		return new ImageBuilder(size, scanStride);
	}

	/**
	 * Sets the colour of a pixel.
	 * 
	 * @param rgb
	 *            the colour to set. The red green and blue components range
	 *            from 0 to 255, and would be composed thus: (red << 16) |
	 *            (green << 8) | blue. Bits other than the lowest 24 are ignored
	 *            (such as alpha values).
	 */
	public ImageBuilder setRgbPixel(int x, int y, int rgb) {
		Verify.argInRange("x", x, 0, size.getWidth());
		Verify.argInRange("y", y, 0, size.getHeight());
		int i = y * scanStride + 3 * x;
		// Blue
		pixels[i++] = (byte) (rgb & 0xff);
		// Green
		rgb >>= 8;
		pixels[i++] = (byte) (rgb & 0xff);
		// Red
		rgb >>= 8;
		pixels[i] = (byte) (rgb & 0xff);
		return this;
	}

	/**
	 * Uses the passed function to copy data into the internal pixel byte array.
	 * Allows an external object to directly copy from it's encapsulated byte
	 * array into this object encapsulated byte array (without requiring an
	 * intermediate array).
	 * <p>
	 * The data is expected to in the format described for {@link Image#copyPixelData()}. 
	 * <p>
	 * Designed for use with OpenCV's Mat - which will copy it's internal buffer
	 * into a provided array. Mat buffers are JNI managed resources, and can be
	 * de-allocated by native code without the garbage collector being aware.
	 * </p>
	 * 
	 * <pre>
	 * Mat mat = ...;
	 * ImageBuilder b = new ImageBuilder(size, scanStride);
	 * b.copyPixelData(buffer -> mat.arrayData().get(buffer));
	 * Image i = b.build();
	 * </pre>
	 * 
	 * Trusts that the function will not keep the reference to the internal
	 * buffer.
	 */
	public ImageBuilder copyPixelData(Consumer<byte[]> copyPixelDataFunction) {
		copyPixelDataFunction.accept(pixels);
		return this;
	}

	public Image build() {
		return new ImageImpl(size, scanStride, pixels);
	}
}
