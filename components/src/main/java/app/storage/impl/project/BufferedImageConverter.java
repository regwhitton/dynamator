package app.storage.impl.project;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import type.Image;
import type.Size;
import type.util.ImageBuilder;
import type.util.Sizes;

class BufferedImageConverter {

	BufferedImage toBufferedImage(Image image) {
		int width = image.getSize().getWidth();
		int height = image.getSize().getHeight();
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		byte[] targetBgrBytes = findInternalByteArray(bi);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int argb = image.getArgbPixel(x, y);
				int i = y * width * 3 + x * 3;
				targetBgrBytes[i++] = (byte) (argb & 0xFF);
				targetBgrBytes[i++] = (byte) ((argb >> 8) & 0xFF);
				targetBgrBytes[i] = (byte) ((argb >> 16) & 0xFF);
			}
		}
		return bi;
	}

	Image toImage(BufferedImage bufferedImage) {
		if (bufferedImage.getType() != BufferedImage.TYPE_3BYTE_BGR) {
			throw new ProjectStoreException("expected java BufferedImage in RGB format");
		}

		int scanSize = bufferedImage.getWidth() * 3;
		int bufferSize = scanSize * bufferedImage.getHeight();
		byte[] sourceBgrBytes = findInternalByteArray(bufferedImage);
		if (sourceBgrBytes.length != bufferSize){
			throw new ProjectStoreException("unexpected buffer size in java BufferedImage");
		}
		
		Size sz = Sizes.sz(bufferedImage.getWidth(), bufferedImage.getHeight());
		ImageBuilder imb = ImageBuilder.create(sz, scanSize);
		imb.copyPixelData(buffer -> System.arraycopy(sourceBgrBytes, 0, buffer, 0, bufferSize));
		return imb.build();
	}
	
	private byte[] findInternalByteArray(BufferedImage bi) {
		return ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
	}
}