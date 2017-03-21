package app.imagetransformer;

import type.Image;

public interface ImageTransformer {
	
	Image overlay(Image image, Image toOverlay) ;
	
	Image toOutline(Image image); 
}
