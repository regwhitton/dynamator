package app.videoencoder.impl;

import org.bytedeco.javacpp.opencv_videoio;

class FourCcFinder {

	int fourCcCode(){
		//int fourcc = opencv_videoio.CV_FOURCC((byte) 'M', (byte) 'J', (byte) 'P', (byte) 'G'); // MJPG Works with .avi, but only for VLC
		//fourcc = -1; // Pops up window.  iYUV and uncompressed works.
		// IYUV seems to be good from Windows XP: https://support.microsoft.com/en-us/kb/899113
		int fourcc = opencv_videoio.CV_FOURCC((byte) 'I', (byte) 'Y', (byte) 'U', (byte) 'V'); // Works on win 10 player.

		
		// H264 should work if openH264 dll installed: but definately doesn't if not. DLL not available in maven central.
		// Downloading and putting in natives - doesn't work.
		// https://github.com/cisco/openh264/releases
		// http://answers.opencv.org/question/104346/how-to-encode-a-h264-video-on-windows/
		// http://answers.opencv.org/question/103267/h264-with-videowriter-windows/
		// x264 - does not work off the bat.
		//  Fourcc codes from MS: https://msdn.microsoft.com/en-us/library/windows/desktop/bb970509(v=vs.85).aspx
		return fourcc;
	}
}
