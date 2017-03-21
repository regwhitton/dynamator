package provider.util;

import provider.Commissioner;

public class Commissioners {

	private final static Commissioner nullCommissioner = new NullCommissioner();

	private Commissioners() {
	}

	/**
	 * @returns a {@link Commissioner} that does nothing.
	 */
	public static Commissioner nullCommissioner() {
		return nullCommissioner;
	}

	private static class NullCommissioner implements Commissioner {
		public void commission() {
		};
	}
}