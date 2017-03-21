package jfx.ui.impl;

/**
 * Periodically calls {@link UiController#updatePreviewImage()} on the JFX
 * application thread (when active between {@link #start()} and {@link #stop()}.
 */
class Scheduler extends AbstractScheduler {

	Scheduler(UiController uiController) {
		super(() -> uiController.updatePreviewImage());
	}
}