package app;

import type.Size;

@SuppressWarnings("serial")
public class FrameSizeMismatchException extends Exception {

	private final Size expectedFrameSize;

	public FrameSizeMismatchException(Size expectedFrameSize) {
		this.expectedFrameSize = expectedFrameSize;
	}

	public Size getExpectedFrameSize() {
		return expectedFrameSize;
	}
}
