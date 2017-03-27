package app.camera.impl;

import static util.Verify.argNotNull;
import static util.Verify.argNotNullOrBlank;

import java.util.Optional;

import app.CameraConfiguration;
import type.Seq;
import type.Size;
import type.util.SeqBuilder;
import type.util.Sizes;
import util.Order;

class OpenCvConfigurationFinder {

	/*
	 * See: http://stackoverflow.com/questions/19448078/python-opencv-access-webcam-maximum-resolution#20120262
	 * and https://en.wikipedia.org/wiki/List_of_common_resolutions
	 */
	private static final Seq<Resolution> COMMON_RESOLUTIONS = SeqBuilder.seq(
			res(7680, 4320, "7680×4320 (16:9) 4320p(UHDTV)"),
			res(3840, 2160, "3840×2160 (16:9) 2160p(UHDTV)"),
			res(1920, 1080, "1920×1080 (16:9) 1080i,1080p(HDTV,Blueray)"),
			res(1366,  767, "1366×768 (16:9) 720p(HDTV,FWXGA)"),
			res(1280,  720, "1280×720 (16:9) 720p(HDTV)"),
			res( 852,  480, "852×480 (16:9) 480i,480p(SDTV)"),
			res( 768,  576, "768×576 (4:3) SDTV"),
			res( 640,  480, "640x480 (4:3) 480i,480p(SDTV)"),

			res( 720,  576, "720×576 DVD(PAL)"),
			res( 720,  480, "720×480 DVD(NTSC)"),
			res( 480,  576, "480×576 SVCD(PAL)"),
			res( 480,  480, "480×480 SVCD(NTSC)"),

			res( 704,  576, "704×576"),
			res( 704,  480, "704×480"),
			res( 544,  576, "544×576"),

			res( 352,  576, "352×576 China Video Disc(PAL)"),
			res( 352,  480, "352×480 China Video Disc(NTSC)"),
			res( 352,  288, "352×288 Video CD(PAL)"),
			res( 352,  240, "352×240 Video CD(NTSC)")
			);

	/**
	 * Probes for possible camera devices and sizes. Opens each attached device
	 * in turn and tries to set common resolutions. This leaves the
	 * {@link OpenCvCamera} is an undefined state - the caller is expected to
	 * manage this.
	 */
	Seq<CameraConfiguration> findConfigurations(OpenCvCamera openCvCamera) {
		SeqBuilder<CameraConfiguration> configs = SeqBuilder.create();
		for (int cameraIndex = 0; openCvCamera.canOpen(cameraIndex); cameraIndex++) {
			Seq<Resolution> resolutions = findAvailableResolutions(openCvCamera);
			long cameraFingerprint = calculateCameraFingerprint(resolutions);
			for (Resolution res : resolutions) {
				configs.add(toCameraConfig(cameraIndex, cameraFingerprint, res));
			}
		}
		return configs.build();
	}

	private Seq<Resolution> findAvailableResolutions(OpenCvCamera openCvCamera) {
		Size maxSize = openCvCamera.maxImageSize();
		SeqBuilder<Resolution> resolutions = SeqBuilder.create();
		resolutions.add(findOrCreateResolutionForSize(maxSize));
		COMMON_RESOLUTIONS.stream().sorted(Order.reversed()) //
				.filter(res -> res.smallerThan(maxSize)) //
				.forEach(res -> resolutions.add(res));
		return resolutions.build();
	}

	private Resolution findOrCreateResolutionForSize(Size maxSize) {
		Optional<Resolution> resolutionMatchingMaxSize = COMMON_RESOLUTIONS.stream() //
				.filter(imgDesc -> imgDesc.sameSizeAs(maxSize)) //
				.findFirst();
		return resolutionMatchingMaxSize.orElseGet(() -> new Resolution(maxSize, maxSize.toString()));
	}

	private CameraConfiguration toCameraConfig(int cameraIndex, long cameraFingerprint, Resolution res) {
		String description = String.format("Camera %d: %s", cameraIndex + 1, res.description);
		return new CameraConfigurationImpl(cameraIndex, cameraFingerprint, description, res.size);
	}

	/**
	 * Creates a fingerprint (rather than the index) to identify a camera, in
	 * case one is unplugged or a new one plugged in. This is based on the
	 * resolutions it supports.
	 */
	private long calculateCameraFingerprint(Seq<Resolution> resolutions) {
		long fp = 0;
		int count = 1;
		for (Resolution res : resolutions) {
			long i = (res.size.getWidth() << 32) | res.size.getHeight();
			fp = fp * i + count;
		}
		return fp;
	}

	private static Resolution res(int width, int height, String description) {
		return new Resolution(Sizes.sz(width, height), description);
	}

	private static class Resolution implements Comparable<Resolution> {
		final Size size;
		final String description;

		Resolution(Size size, String description) {
			this.size = argNotNull(size);
			this.description = argNotNullOrBlank(description);
		}

		@Override
		public int compareTo(Resolution other) {
			return size.compareTo(other.size);
		}

		boolean smallerThan(Size size) {
			return this.size.compareTo(size) < 0;
		}

		boolean sameSizeAs(Size size) {
			return this.size.equals(size);
		}
	}
}
