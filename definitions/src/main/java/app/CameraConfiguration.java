package app;

import type.Size;

public interface CameraConfiguration {
	
	long getFingerprint();
	
	String getDescription();

	Size getSize();
}
