package type.util;

import static util.Verify.argNotNeg;

import type.Size;

class SizeImpl implements Size {

	private final int width;
	private final int height;

	SizeImpl(int width, int height) {
		this.width = argNotNeg(width);
		this.height = argNotNeg(height);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int compareTo(Size s) {
		return width != s.getWidth() ? width - s.getWidth() : height - s.getHeight();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Size)) {
			return false;
		}
		Size s = (Size) other;
		return width == s.getWidth() && height == s.getHeight();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + this.width;
		hash = 67 * hash + this.height;
		return hash;
	}

	@Override
	public String toString() {
		return "" + width + 'x' + height;
	}
}
