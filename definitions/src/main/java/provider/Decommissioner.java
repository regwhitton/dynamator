package provider;

/**
 * Used by the application surrounding a component to signal when it is shutting
 * down.
 */
public interface Decommissioner {

	/**
	 * Release any internal resources.
	 * <p>
	 * Implementations may use references to other external application
	 * components (injected into the provider's constructor or via setters), but
	 * will not use methods on them. The external components cannot be assumed
	 * to be in a commissioned state.
	 * </p>
	 */
	void decommission();
}