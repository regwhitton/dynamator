package app.videoencoder.impl;

import app.videoencoder.VideoEncoder;
import opencv.util.ImageToMatConverter;
import opencv.util.MatFactory;
import opencv.util.VideoWriterFactory;
import provider.Commissioner;
import provider.Decommissioner;
import provider.Provider;
import provider.util.Commissioners;
import provider.util.Decommissioners;

public class VideoEncoderProvider implements Provider<VideoEncoder> {

	@Override
	public VideoEncoder get() {
		return new OpenCvVideoEncoder(new VideoWriterFactory(), new ImageToMatConverter(new MatFactory()),
				new FourCcFinder());
	}

	@Override
	public Commissioner getCommissioner(VideoEncoder videoEncoder) {
		return Commissioners.nullCommissioner();
	}

	@Override
	public Decommissioner getDecommissioner(VideoEncoder videoEncoder) {
		return Decommissioners.nullDecommissioner();
	}
}