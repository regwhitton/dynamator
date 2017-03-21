package app.imagetransformer.impl;

import app.imagetransformer.ImageTransformer;
import opencv.util.ImageToMatConverter;
import opencv.util.MatFactory;
import opencv.util.MatToImageConverter;
import provider.Commissioner;
import provider.Decommissioner;
import provider.Provider;
import provider.util.Commissioners;
import provider.util.Decommissioners;

public class ImageTransformerProvider implements Provider<ImageTransformer> {

	@Override
	public ImageTransformer get() {
		MatFactory matFactory = new MatFactory();
		return new OpenCvImageTransformer(matFactory, new ImageToMatConverter(matFactory), new MatToImageConverter());
	}

	@Override
	public Commissioner getCommissioner(ImageTransformer imageTransformer) {
		return Commissioners.nullCommissioner();
	}

	@Override
	public Decommissioner getDecommissioner(ImageTransformer imageTransformer) {
		return Decommissioners.nullDecommissioner();
	}
}
