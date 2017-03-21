package app.storage.impl.project;

import static util.Verify.argNotNull;

import java.io.File;
import java.util.Iterator;

import type.Image;

class FrameIterator implements Iterator<Image> {

	private final Iterator<File> frameFileIterator;
	private final FrameStore frameDao;

	FrameIterator(Iterator<File> frameFileIterator, FrameStore frameDao) {
		this.frameFileIterator = argNotNull(frameFileIterator);
		this.frameDao = argNotNull(frameDao);
	}

	@Override
	public boolean hasNext() {
		return frameFileIterator.hasNext();
	}

	@Override
	public Image next() {
		return frameDao.readFrame(frameFileIterator.next());
	}
}
