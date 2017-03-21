package type.util;

import type.Size;

public class Sizes {

	private Sizes() {
	}

	public static Size sz(int width, int height) {
		return new SizeImpl(width, height);
	}
}
