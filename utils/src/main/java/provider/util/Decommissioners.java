package provider.util;

import provider.Decommissioner;

public class Decommissioners {

	private final static Decommissioner nullDecommissioner = new NullDecommissioner();

	private Decommissioners() {
	}

	/**
	 * @returns a {@link Decommissioner} that does nothing.
	 */
	public static Decommissioner nullDecommissioner() {
		return nullDecommissioner;
	}

	private static class NullDecommissioner implements Decommissioner {
		public void decommission() {
		};
	}
}