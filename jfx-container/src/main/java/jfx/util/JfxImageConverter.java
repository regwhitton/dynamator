package jfx.util;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import type.Image;

public class JfxImageConverter {

	public WritableImage convert(Image image) {
		int width = image.getSize().getWidth();
		int height = image.getSize().getHeight();
		WritableImage jfxImage = new WritableImage(width, height);
		PixelWriter pixelWriter = jfxImage.getPixelWriter();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixelWriter.setArgb(x, y, image.getArgbPixel(x, y));
			}
		}
		return jfxImage;
	}
}
