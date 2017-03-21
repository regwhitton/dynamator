package type;

public interface Image {

	/**
	 * @return the width and height size of the image in pixels.
	 */
	Size getSize();

	/**
	 * @return the number of bytes between the start of the data for each row of
	 *         pixels. This will be: 3 x width + [zero or more padding bytes].
	 */
	int getScanStride();

	/**
	 * @return a copy of the pixel byte data which the caller can modify at
	 *         whim. The size of the array will be height x scanStride. Each
	 *         pixel is encoded as 3 bytes in the order: blue, green, red. The
	 *         byte values are in the range 0 to 255. The pixels are ordered in
	 *         rows from top to bottom, and within that from left to right.
	 *         There may padding bytes at the end of each row.
	 */
	byte[] copyPixelData();

	/**
	 * @return a pixel's colour encoded as
	 *         {@code Alpha << 24 | Red << 16 | Green << 8 | Blue}, where the
	 *         alpha value will be 255.
	 */
	int getArgbPixel(int x, int y);
}