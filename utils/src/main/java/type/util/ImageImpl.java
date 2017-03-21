package type.util;

import java.util.Arrays;

import type.Image;
import type.Size;

class ImageImpl implements Image {

	private final Size size;
	private final int scanStride;
	private final byte[] pixels;

	ImageImpl(Size size, int scanStride, byte[] pixels) {
		this.size = size;
		this.scanStride = scanStride;
		this.pixels = pixels;
	}

	@Override
	public Size getSize() {
		return size;
	}

	@Override
	public int getScanStride() {
		return scanStride;
	}

	@Override
	public byte[] copyPixelData() {
		return Arrays.copyOf(pixels, pixels.length);
	}

	@Override
	public int getArgbPixel(int x, int y) {
		int i = y * scanStride + 3 * x;
		return 0xff000000 | ((pixels[i + 2] & 0xff) << 16) | ((pixels[i + 1] & 0xff) << 8) | (pixels[i] & 0xff);
	}
}
