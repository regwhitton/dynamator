package provider;

/**
 * Used by the application surrounding a component to signal when it is starting
 * up.
 */
public interface Commissioner {

	/**
	 * Sets up any internal components and resources.
	 * <p>
	 * Implementations may use references to other external application
	 * components (injected into the provider's constructor or via setters), but
	 * will not use any methods on them. The external components cannot be
	 * assumed to be in a commissioned state. Further external component set up
	 * must be arranged using specific methods for that purpose (such as show(),
	 * open(), begin() etc.).
	 * </p>
	 */
	void commission() throws Exception;
}
