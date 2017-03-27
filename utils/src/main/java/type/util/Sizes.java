package type.util;

import type.Size;

public class Sizes {

	private Sizes() {
	}

	public static Size sz(int width, int height) {
		return new SizeImpl(width, height);
	}

	/**
	 * Parses a string representing the width and height. e.g. "10x5" when width
	 * is 10 and height is 5.
	 * 
	 * @throws IllegalArgumentException
	 *             if argument is not in the expected format.
	 */
	public static Size sz(String size) {
		int iSeparator = size.indexOf('x');
		if (iSeparator < 0) {
			throw new IllegalArgumentException("size \"" + size + "\" not in expected format: missing 'x'");
		}
		try {
			int width = Integer.valueOf(size.substring(0, iSeparator));
			int height = Integer.valueOf(size.substring(iSeparator + 1));
			return new SizeImpl(width, height);
		} catch (RuntimeException re) {
			throw new IllegalArgumentException("size \"" + size + "\" not in expected format", re);
		}
	}
}
